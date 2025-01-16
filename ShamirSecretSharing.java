import org.json.JSONObject;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ShamirSecretSharing {
    public static int convertBaseToDecimal(String base, String value) {
        int baseInt = Integer.parseInt(base);
        return Integer.parseInt(value, baseInt);
    }
    public static int lagrangeInterpolation(int[] xValues, int[] yValues, int k) {
        int secret = 0;
        for (int i = 0; i < k; i++) {
            int xi = xValues[i];
            int yi = yValues[i];
            int term = yi;

            for (int j = 0; j < k; j++) {
                if (i != j) {
                    int xj = xValues[j];
                    term = term * (0 - xj) / (xi - xj);
                }
            }

            secret += term;
        }

        return secret;
    }

    public static int processJsonFile(String jsonFilePath) {
        try {
            String jsonInput = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
            JSONObject jsonObject = new JSONObject(jsonInput);
            JSONObject keys = jsonObject.getJSONObject("keys");
            int n = keys.getInt("n");
            int k = keys.getInt("k");
            List<Integer> xValues = new ArrayList<>();
            List<Integer> yValues = new ArrayList<>();
            for (String key : jsonObject.keySet()) {
                if (!key.equals("keys")) {
                    JSONObject share = jsonObject.getJSONObject(key);
                    String base = share.getString("base");
                    String value = share.getString("value");
                    int y = convertBaseToDecimal(base, value);
                    int x = Integer.parseInt(key); 
                    xValues.add(x);
                    yValues.add(y);
                }
            }
            int[] xArray = xValues.stream().mapToInt(i -> i).toArray();
            int[] yArray = yValues.stream().mapToInt(i -> i).toArray();
            return lagrangeInterpolation(xArray, yArray, k);

        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }
    public static void main(String[] args) {
        String jsonFilePath1 = "input1.json"; 
        String jsonFilePath2 = "input2.json"; 
        int secret1 = processJsonFile(jsonFilePath1);
        System.out.println("The secret from file 1 is: " + secret1);
        int secret2 = processJsonFile(jsonFilePath2);
        System.out.println("The secret from file 2 is: " + secret2);
    }
}
