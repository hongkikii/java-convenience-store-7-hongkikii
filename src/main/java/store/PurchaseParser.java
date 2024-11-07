package store;

import java.util.HashMap;
import java.util.Map;

public class PurchaseParser {
    public Map<String, Integer> execute(String input) {
        Map<String, Integer> result = new HashMap<>();
        String[] productInfos = input.split(",");
        for(String productInfo : productInfos) {
            String[] info = productInfo.substring(1, productInfo.length() - 1).split("-");
            result.put(info[0], Integer.parseInt(info[1]));
        }
        return result;
    }
}
