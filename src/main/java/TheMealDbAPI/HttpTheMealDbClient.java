package TheMealDbAPI;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

//detta klass pratar med internet, skickar HTTP anrop, och får text i form av rå json text
//vi har två metoder, en som söker på ingrediens och en som söker på specifikt id
public class HttpTheMealDbClient {
    private HttpClient client;
    private String baseUrl;

    public HttpTheMealDbClient(){
        client = HttpClient.newHttpClient();
        baseUrl = "https://www.themealdb.com/api/json/v1/1"; //delen som är gemensam för alla
    }

    public String filterByIngredient(String mainIngredient) throws IOException, InterruptedException {
        String url = baseUrl + "/filter.php?i=" + mainIngredient; //här bygger jag URL
        return sendRequest(url);
    }

    public String filterByLookUpId(String id) throws InterruptedException, IOException {
        //i denna metod skickar jag förfrågan om det specifika receptet och får tillbaka text
        String url = baseUrl + "/lookup.php?i=" + id;
        return sendRequest(url);

    }
    private String sendRequest(String url) throws IOException, InterruptedException{
        //skapar "brevet som ska skickas, mpste bestå av: adressen, att vi vill hämra och skapa den
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url)) //URI gör om från sträng till en adress i form av objekt
                .GET()
                .build();

        //här skickar vi förfrågan och inväntar svar
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode()<200 || response.statusCode()>= 300){ //200 ok, 202-hittades eh, 500-server fel
            throw new IOException("API-request failed");
        }
        return response.body(); //body är json strängen
    }
}
