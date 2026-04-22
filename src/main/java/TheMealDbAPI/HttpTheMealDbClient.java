package TheMealDbAPI;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

//detta klass pratar med internet, skickar HTTP anrop, och får text i form av rå json text
//vi har två metoder, en som söker på ingrediens och en som söker på specifikt id
public class HttpTheMealDbClient {
    private HttpClient client;
    private String baseUrl;

    public HttpTheMealDbClient(){
        // bygg klient med connect-timeout så den inte hänger för evigt
        client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        baseUrl = "https://www.themealdb.com/api/json/v1/1"; //delen som är gemensam för alla
    }

    public String filterByIngredient(String mainIngredient) throws IOException, InterruptedException {
        if (mainIngredient == null){
            mainIngredient = "";
            //Trimma och URL-enkoda så ingredienser ej har mellanslag eller specialtecken
        }
        String encoded = URLEncoder.encode(mainIngredient.trim(), StandardCharsets.UTF_8);
        String url = baseUrl + "/filter.php?i=" + encoded; //här bygger jag URL
        return sendRequest(url);
    }

    public String filterByLookUpId(String id) throws InterruptedException, IOException {
        //i denna metod skickar jag förfrågan om det specifika receptet och får tillbaka text
        String encodedId = id == null ? "" : URLEncoder.encode(id.trim(), StandardCharsets.UTF_8);
        String url = baseUrl + "/lookup.php?i=" + encodedId;
        return sendRequest(url);

    }
    private String sendRequest(String url) throws IOException, InterruptedException{
        //logga URL för felsökning
        System.out.println("HttpTheMealDbClient -> sendRequest URL = " + url);

        //skapar "brevet som ska skickas, mpste bestå av: adressen, att vi vill hämra och skapa den
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url)) //URI gör om från sträng till en adress i form av objekt
                .timeout(Duration.ofSeconds(10)) // lägg till request-timeout
                .GET()
                .build();

        //här skickar vi förfrågan och inväntar svar
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("HttpTheMealDbClient -> response status = " + response.statusCode() +
                ", body length = " + (response.body() == null ? 0 : response.body().length()));

        if (response.statusCode()<200 || response.statusCode()>= 300){ //200 ok, 202-hittades eh, 500-server fel
            throw new IOException("API-request failed: HTTP " + response.statusCode());
        }
        return response.body(); //body är json strängen
    }
}