import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class APIHandler {
    private final String API_KEY;

    public APIHandler(String api_key) {
        API_KEY = api_key;
    }


    private JSONObject getExchangeRateJSON(String currencyCode) {
        URLConnection connection = null;
        try {
            connection = new URL(String.format("http://apilayer.net/api/live?access_key=%s&source=%s&format=1", API_KEY, currencyCode)).openConnection();
            InputStream is = connection.getInputStream();
            JSONObject json = new JSONObject(JSONHandler.convertStreamToString(is));
            return json;
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(connection.getContentType());
        return null;
    }

    public ExchangeRateInfo getExchangeRateInfo(String currencyCode) {
        JSONObject json = getExchangeRateJSON(currencyCode);
        if(json == null)
            return null;
        JSONObject info = new JSONObject();
        info.put("currency_code", currencyCode);
        info.put("timestamp", json.getLong("timestamp"));
        info.put("rates", json.getJSONObject("quotes"));

        return JSONHandler.getExchangeRateInfoFromJSON(info);
    }
}
