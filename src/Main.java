import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws IOException {

//        URLConnection connection = new URL("http://apilayer.net/api/live" +
//                "?access_key=ab0a3b4f052196877b37b483ccd55ad3" +
//                //"&currencies=EUR,GBP,CAD" +
//                "&source=USD" +
//                "&format=1").openConnection();
//        InputStream is = connection.getInputStream();
//        System.out.println(connection.getContentType());
//        JSONObject json = new JSONObject(JSONHandler.convertStreamToString(is));
//
//        //JSONObject json = new JSONObject("{\"terms\":\"https://currencylayer.com/terms\",\"success\":true,\"privacy\":\"https://currencylayer.com/privacy\",\"source\":\"USD\",\"timestamp\":1691863743,\"quotes\":{\"USDEUR\":0.912104,\"USDGBP\":0.787774,\"USDCAD\":1.34395}}");
//        System.out.println(json);
//        JSONObject quotes = json.getJSONObject("quotes");
        //quotes.
//        double USD_EUR = quotes.getDouble("USDEUR");
//        System.out.println(quotes);
//        System.out.println(USD_EUR);


        //JSONObject ratesObject = JSONHandler.readJsonFromFile("src/rates.txt");
        //JSONHandler.getExchangeRateInfoFromJSON(ratesObject);


        APIHandler apiHandler = new APIHandler("ab0a3b4f052196877b37b483ccd55ad3");
        String firstCurrencyCode = getCurrencyCode();
        String secondCurrencyCode = getCurrencyCode();

        ExchangeRateInfo exchangeRateInfoUSD = null;
        if(exchangeRateInfoUSD == null)
            exchangeRateInfoUSD = apiHandler.getExchangeRateInfo("USD");

        System.out.println(ConvertCurrency(firstCurrencyCode, secondCurrencyCode, 1, exchangeRateInfoUSD));

    }

    public static String getCurrencyCode() {
        Scanner scanner = new Scanner(System.in);
        return scanner.next();
    }

    public static double ConvertCurrency(String firstCurrencyCode, String secondCurrencyCode, double amount, ExchangeRateInfo exchangeRateInfoUSD) {
        if(firstCurrencyCode.equals("USD"))
            return secondCurrencyCode.equals("USD") ? 1 : amount * exchangeRateInfoUSD.getExchangeRate(secondCurrencyCode);
        if(secondCurrencyCode.equals("USD"))
            return amount * 1d / exchangeRateInfoUSD.getExchangeRate(firstCurrencyCode);
        return amount * (1d / exchangeRateInfoUSD.getExchangeRate(firstCurrencyCode)) * exchangeRateInfoUSD.getExchangeRate(secondCurrencyCode);
    }

}
