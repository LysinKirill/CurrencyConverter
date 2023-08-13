import org.json.JSONObject;

import java.io.*;
import java.util.Date;
import java.util.HashMap;

public class JSONHandler {
    public static String convertStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public static void writeJsonToFile(String path, JSONObject jsonObject) {
        try {
            FileWriter fileWriter = new FileWriter(path);
            fileWriter.write(jsonObject.toString());
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static JSONObject readJsonFromFile(String path) throws IOException {
        try {
            FileReader fileReader = new FileReader(path);
            String fileContents = readAllCharactersOneByOne(fileReader);
            return new JSONObject(fileContents);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    public static String readAllCharactersOneByOne(Reader reader) throws IOException {
        StringBuilder content = new StringBuilder();
        int nextChar;
        while ((nextChar = reader.read()) != -1) {
            content.append((char) nextChar);
        }
        return String.valueOf(content);
    }

    public static ExchangeRateInfo getExchangeRateInfoFromJSON(JSONObject jsonObject) {
        String currencyCode = jsonObject.getString("currency_code");
        long timestamp = jsonObject.getLong("timestamp");
        HashMap<String, Double> exchangeRatesHashMap = new HashMap<>();

        JSONObject exchangeRates = jsonObject.getJSONObject("rates");
        for (Object currency_code : exchangeRates.names()) {
            String code = (String)currency_code;
            exchangeRatesHashMap.put(code, exchangeRates.getDouble(code));
        }
        return new ExchangeRateInfo(currencyCode, timestamp, exchangeRatesHashMap);
    }
}
