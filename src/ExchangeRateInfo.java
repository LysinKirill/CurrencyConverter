import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

public class ExchangeRateInfo implements Serializable {
    private static final long serialVersionUID = 6529685098267757690L;
    private long timestamp;
    private String currencyCode;
    private HashMap<String, Double> exchangeRates;

    public ExchangeRateInfo(String currencyCode, long timestamp, HashMap<String, Double> exchangeRates) {
        this.currencyCode = currencyCode;
        this.timestamp = timestamp;
        this.exchangeRates = exchangeRates;
    }

    public boolean hasEntry(String code) {
        return exchangeRates.containsKey(code);
    }

    public double getExchangeRate(String code) {
        return exchangeRates.get(currencyCode + code);
    }

    public Long timestamp() {
        return timestamp;
    }
}
