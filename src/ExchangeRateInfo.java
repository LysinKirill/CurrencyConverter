import java.util.Date;
import java.util.HashMap;

public class ExchangeRateInfo {
    private Date updateDate;
    private String currencyCode;
    private HashMap<String, Double> exchangeRates;

    public ExchangeRateInfo(String currencyCode, Date updateDate, HashMap<String, Double> exchangeRates) {
        this.currencyCode = currencyCode;
        this.updateDate = updateDate;
        this.exchangeRates = exchangeRates;
    }

    public boolean hasEntry(String code) {
        return exchangeRates.containsKey(code);
    }

    public double getExchangeRate(String code) {
        return exchangeRates.get(currencyCode + code);
    }

    public Date updateDate() {
        return updateDate;
    }
}
