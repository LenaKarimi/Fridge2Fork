package API;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiClient {
    private HttpClient client;
    private String baseUrl;

    public ApiClient(){
        client = HttpClient.newHttpClient();
        baseUrl = "https://www.themealdb.com/api/json/v1/1"; //delen som är gemensam för alla
    }

    public String filterByIngredient(String ingredient) throws IOException, InterruptedException {
        String url = baseUrl + "/filter.php?i=" + ingredient; //här bygger jag URL

        //skapar "brevet som ska skickas, mpste bestå av: adressen, att vi vill hämra och skapa den
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url)) //URI gör om från sträng till en adress i form av objekt
                .GET()
                .build();

        //här skickar vi förfrågan och inväntar svar
        HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());
                                                                //denna metod ovan gör så vi får det som en sträng
        return response.body();
    }

    public String filterByLookUpId(String id) throws InterruptedException, IOException {
        String url = baseUrl + "/lookup.php?i=" + id;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

}
