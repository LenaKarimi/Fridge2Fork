package TheMealDbAPI;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * HTTP client that interacts with TheMealDV API.
 * It is sending HTTP requests and returns response as raw JSON strings.
 * It provides functionality to search meal by ingredient, retrieve meal details by ID.
 * @author Lena
 */
public class HttpTheMealDbClient {
    private HttpClient client;
    private String baseUrl;

    /**
     * Constructs a new HttpTheMealDbClient.
     * Initializes the HTTP client with a connection timeout for 10 seconds
     */
    public HttpTheMealDbClient(){
        client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        baseUrl = "https://www.themealdb.com/api/json/v1/1";
    }

    /**
     * Fetches meals with a specific ingredient.
     * @param mainIngredient the ingredient to search for
     * @return a JSON string containing matching meals
     * @throws IOException if a network error occurs or the API request fails
     * @throws InterruptedException if the request is interrupted
     */
    public String filterByIngredient(String mainIngredient) throws IOException, InterruptedException {

        mainIngredient = mainIngredient.replace(" ", "_");

        String url = baseUrl + "/filter.php?i=" + mainIngredient; //här bygger jag URL
        return sendRequest(url);
    }

    /**
     * Fetches detailed information about a specific meal by its ID.
     * @param id the unique meal ID
     * @return a JSON string containing meal details
     * @throws InterruptedException if a network error occurs or the API request fails
     * @throws IOException if the request is interrupted
     */
    public String filterByLookUpId(String id) throws InterruptedException, IOException {
        String url = baseUrl + "/lookup.php?i=" + id;
        return sendRequest(url);

    }

    /**
     * Sends the HTTP GET request to the specified URL and returns the response body in a string.
     * @param url the full URL to send the request to
     * @return the response body as a JSON string
     * @throws IOException if the server returns a non-success status code or a network error occurs
     * @throws InterruptedException if the request is interrupted
     */
    private String sendRequest(String url) throws IOException, InterruptedException{
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(10))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode()<200 || response.statusCode()>= 300){
            throw new IOException("API-request failed");
        }
        return response.body();
    }
}