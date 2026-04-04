package API;

import java.net.http.HttpClient;

public class ApiClient {
    private HttpClient client;
    private String baseUrl;

    public ApiClient(){
        client = HttpClient.newHttpClient();
        baseUrl = "https://www.themealdb.com/api/json/v1/1"; //delen som är gemensam för alla
    }

}
