package http.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final String url;
    private String apiToken;
    private HttpClient httpClient;

    public KVTaskClient(String url) {
        this.url = url;
        httpClient = HttpClient.newHttpClient();
        String registerQuery = "register";
        URI uri = URI.create(url + registerQuery);

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response;
        try {
            response = httpClient.send(request, handler);
        } catch (IOException | InterruptedException e) {
            throw new KVTaskClientException("Возникла ошибка при создании http-клиента");
        }

        apiToken = response.body();
    }

    public void put(String key, String json) {
        URI uri = URI.create(url + "save/" + key + "?API_TOKEN=" + apiToken);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            httpClient.send(request, handler);
        } catch (IOException | InterruptedException e) {
            throw new KVTaskClientException("Возникла ошибка при отправке на http-сервер");
        }
    }

    public String load(String key) {
        URI uri = URI.create(url + "load/" + key + "?API_TOKEN=" + apiToken);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response;
        try {
            response = httpClient.send(request, handler);
        } catch (IOException | InterruptedException e) {
            throw new KVTaskClientException("Возникла ошибка при загрузке с http-сервера");
        }
        return response.body();
    }
}
