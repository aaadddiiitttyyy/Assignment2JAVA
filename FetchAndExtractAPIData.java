import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FetchAndExtractAPIData {
    public static void main(String[] args) {
        try {
            // Step 1: Fetch API data using GET request
            URL url = new URL("https://randomuser.me/api/?results=5"); // API URL
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP Error Code : " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder apiResponse = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                apiResponse.append(line);
            }
            conn.disconnect();

            String jsonResponse = apiResponse.toString();

            // Step 2: Apply regex patterns to extract emails and websites
            System.out.println("Fetched Data from API:\n" + jsonResponse + "\n");

            // Regex for emails
            Pattern emailPattern = Pattern.compile("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b");
            Matcher emailMatcher = emailPattern.matcher(jsonResponse);

            System.out.println("Extracted Emails:");
            while (emailMatcher.find()) {
                System.out.println(emailMatcher.group());
            }

            // Regex for websites (URLs starting with http/https)
            Pattern websitePattern = Pattern.compile("\\bhttps?://[A-Za-z0-9._%+-/]+\\b");
            Matcher websiteMatcher = websitePattern.matcher(jsonResponse);

            System.out.println("\nExtracted Websites:");
            while (websiteMatcher.find()) {
                System.out.println(websiteMatcher.group());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
