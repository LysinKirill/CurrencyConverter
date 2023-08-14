import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.HashMap;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws IOException {
        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        final String API_KEY = "ab0a3b4f052196877b37b483ccd55ad3";

        final Dimension SCREEN_SIZE = toolkit.getScreenSize();
        final int FRAME_HEIGHT = (int)(SCREEN_SIZE.height * 0.5);
        final int FRAME_WIDTH = (int)(SCREEN_SIZE.width * 0.4);

        APIHandler apiHandler = new APIHandler(API_KEY);



        String[] currencyCodes = loadCurrencyCodes();

        JFrame jFrame = new JFrame();



        JComboBox<String> sourceCurrencyBox = new JComboBox<>(currencyCodes);
        sourceCurrencyBox.setBounds((int)(FRAME_WIDTH * 0.2), (int)(FRAME_HEIGHT * 0.3), (int)(FRAME_WIDTH * 0.15), (int)(FRAME_HEIGHT * 0.05));
        sourceCurrencyBox.setSelectedIndex(2);

        final JComboBox[] targetCurrencyBox = new JComboBox[]{new JComboBox(currencyCodes)};
        targetCurrencyBox[0].setBounds((int)(FRAME_WIDTH * 0.6), (int)(FRAME_HEIGHT * 0.3), (int)(FRAME_WIDTH * 0.15), (int)(FRAME_HEIGHT * 0.05));
        targetCurrencyBox[0].setSelectedItem(0);

        JTextField inputArea = new JTextField();
        inputArea.setBounds((int)(FRAME_WIDTH * 0.2), (int)(FRAME_HEIGHT * 0.24), (int)(FRAME_WIDTH * 0.15), (int)(FRAME_HEIGHT * 0.05));

        JTextField outputArea = new JTextField();
        outputArea.setBounds((int)(FRAME_WIDTH * 0.6), (int)(FRAME_HEIGHT * 0.24), (int)(FRAME_WIDTH * 0.15), (int)(FRAME_HEIGHT * 0.05));
        outputArea.setEditable(false);

        jFrame.add(inputArea);
        jFrame.add(sourceCurrencyBox);
        jFrame.add(outputArea);
        jFrame.add(targetCurrencyBox[0]);
        jFrame.setLayout(null);

        jFrame.setResizable(false);
        jFrame.setLocation((SCREEN_SIZE.width - FRAME_WIDTH) / 2, (SCREEN_SIZE.height - FRAME_HEIGHT) / 2);
        jFrame.setSize(FRAME_WIDTH, FRAME_HEIGHT);

        jFrame.setResizable(false);
        jFrame.setVisible(true);


        FileInputStream fileInputStream = new FileInputStream("src/EXCHANGE_RATE_INFO.txt");
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

        ExchangeRateInfo exchangeRateInfoUSD = null;
        try {
            exchangeRateInfoUSD = (ExchangeRateInfo) objectInputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }


        if(exchangeRateInfoUSD == null || ((System.currentTimeMillis() / 1000L) - exchangeRateInfoUSD.timestamp()) > 3600)
            exchangeRateInfoUSD = apiHandler.getExchangeRateInfo("USD");

        final String[] sourceCurrencyCode = {"USD"};
        final String[] targetCurrencyCode = {"RUB"};

        ExchangeRateInfo finalExchangeRateInfoUSD = exchangeRateInfoUSD;

        sourceCurrencyBox.addItemListener(itemEvent -> {
            String s = ((String)itemEvent.getItem());
            sourceCurrencyCode[0] = s.substring(0, s.length() - 1);
            updateOutput(inputArea, outputArea, sourceCurrencyCode[0], targetCurrencyCode[0], finalExchangeRateInfoUSD);
        });

        targetCurrencyBox[0].addItemListener(itemEvent -> {
            String s = ((String)itemEvent.getItem());
            targetCurrencyCode[0] = s.substring(0, s.length() - 1);
            updateOutput(inputArea, outputArea, sourceCurrencyCode[0], targetCurrencyCode[0], finalExchangeRateInfoUSD);
        });



        inputArea.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {}

            @Override
            public void keyPressed(KeyEvent keyEvent) {}

            @Override
            public void keyReleased(KeyEvent keyEvent) {
                updateOutput(inputArea, outputArea, sourceCurrencyCode[0], targetCurrencyCode[0], finalExchangeRateInfoUSD);
            }
        });
    }

    public static void updateOutput(JTextField inputArea, JTextField outputArea, String sourceCurrencyCode, String targetCurrencyCode, ExchangeRateInfo exchangeRateInfoUSD) {
        String text = inputArea.getText();
        if(text.isEmpty()) {
            outputArea.setText("0");
            return;
        }
        outputArea.setText(Double.toString(ConvertCurrency(sourceCurrencyCode, targetCurrencyCode, Double.parseDouble(inputArea.getText()), exchangeRateInfoUSD)));
    }

    public static double ConvertCurrency(String firstCurrencyCode, String secondCurrencyCode, double amount, final ExchangeRateInfo exchangeRateInfoUSD) {
        if(firstCurrencyCode.equals("USD"))
            return secondCurrencyCode.equals("USD") ? amount : amount * exchangeRateInfoUSD.getExchangeRate(secondCurrencyCode);
        if(secondCurrencyCode.equals("USD"))
            return amount * 1d / exchangeRateInfoUSD.getExchangeRate(firstCurrencyCode);
        return amount * (1d / exchangeRateInfoUSD.getExchangeRate(firstCurrencyCode)) * exchangeRateInfoUSD.getExchangeRate(secondCurrencyCode);
    }

    static String[] loadCurrencyCodes() {
        try {
            FileReader fileReader = new FileReader("src/CURRENCY_CODES.txt");
            String fileContents = JSONHandler.readAllCharactersOneByOne(fileReader);
            return fileContents.split("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String[]{"USD", "EUR", "RUB"};
    }

}
