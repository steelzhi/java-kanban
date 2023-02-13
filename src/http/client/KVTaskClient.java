package http.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private String url;
    private String apiToken;
    private HttpClient httpClient;

    public KVTaskClient(String url) throws IOException, InterruptedException {
        this.url = url;
        httpClient = HttpClient.newHttpClient();
        String registerQuery = "register";
        URI uri = URI.create(url + registerQuery);

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = httpClient.send(request, handler);

        apiToken = response.body();
    }

    public void put(String key, String json) throws IOException, InterruptedException {
        URI uri = URI.create(url + "save/" + key + "?API_TOKEN=" + apiToken);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        httpClient.send(request, handler);
    }

    public String load(String key) throws IOException, InterruptedException {
        URI uri = URI.create(url + "load/" + key + "?API_TOKEN=" + apiToken);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = httpClient.send(request, handler);
        return response.body();
    }
}
