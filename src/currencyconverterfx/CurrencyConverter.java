package currencyconverterfx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONObject;

/**
 *
 * @author rinku
 */
public class CurrencyConverter {

    /**
     * @param args the command line arguments
     * @throws java.net.ProtocolException
     */
    static String red = "\u001B[31m";
    static String green = "\u001B[32m";
    static HashMap<String, String> currencyCodes;

    public static ArrayList<String> currencyCodesHashMethod() {
        currencyCodes = new HashMap<>();

        currencyCodes.put("United States Dollar", "USD");
        currencyCodes.put("Canadian Dollar", "CAD");
        currencyCodes.put("Euro", "EUR");
        currencyCodes.put("Hong Kong Dollar", "HKD");
        currencyCodes.put("Indian Rupee", "INR");

        currencyCodes.put("Chinese Yuan", "CNY");
        currencyCodes.put("Philippine peso", "PHP");
        currencyCodes.put("Pound sterling", "GBP");
        currencyCodes.put("Singapore Dollar", "SGD");
        currencyCodes.put("Australian Dollar", "AUD");

        ArrayList<String> list = new ArrayList<>();
        for (String key : currencyCodes.keySet()) {
            list.add(key);
        }
        return list;
    }

    public static double sendHttpGETRequest(String fromCode, String toCode, double amount) throws MalformedURLException, ProtocolException, IOException {
        String api_key = "fca_live_fLWtfQEbvTkiaUAHKfrQjNj3Yw8o4qd0zbpikSMO";
        String api = "https://api.freecurrencyapi.com/v1/latest?apikey=" + api_key + "&base_currency=" + toCode;
        URL url = new URL(api);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        int responseCode = httpURLConnection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            StringBuilder response;
            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String inputLine;
            response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject obj = new JSONObject(response.toString());
            double exchangeRate = obj.getJSONObject("data").getDouble(fromCode);
            DecimalFormat f = new DecimalFormat("00.00");
            System.out.println();
            System.out.println(green + f.format(amount) + fromCode + " = " + f.format(amount / exchangeRate) + toCode);
            return amount / exchangeRate;
        } else {
            System.out.println(red + "Http request failed!!");
        }
        return -1;
    }

}
