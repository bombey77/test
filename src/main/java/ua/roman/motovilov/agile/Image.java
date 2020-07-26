package ua.roman.motovilov.agile;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Component
public class Image {

    private static final String URI = "http://interview.agileengine.com";
    private static final String API_KEY = "apiKey";
    private static final String API_VALUE = "23567b218376f79d9415";
    private static String token = "";

    public String getToken() {
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            JSONObject json = new JSONObject();
            json.put(API_KEY, API_VALUE);

            try {
                HttpPost request = new HttpPost(URI.concat("/auth"));

                StringEntity params = new StringEntity(json.toString());
                request.addHeader("content-type", "application/json");
                request.addHeader("Accept", "application/json");
                request.setEntity(params);

                HttpResponse response = httpClient.execute(request);
                HttpEntity entity = response.getEntity();

                JSONObject token = new JSONObject(getResponse(entity));
                return token.getString("token");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error, Cannot Establish Connection");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getPhotos(String token) {
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            try {
                HttpGet request = new HttpGet(URI.concat("/images"));

                request.addHeader("Authorization", "Bearer ".concat(token));
                request.addHeader("content-type", "application/json");
                request.addHeader("Accept", "application/json");

                HttpResponse response = httpClient.execute(request);
                HttpEntity entity = response.getEntity();

                return getResponse(entity);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error, Cannot Establish Connection");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getPhotoPage(String token, String page) {
        return getData(token, "/images?page=".concat(page));
    }

    public String getDetails(String token, String id) {
        return getData(token, "/images/".concat(id));
    }

    private String getResponse(HttpEntity entity) throws IOException {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(entity.getContent(), "utf-8"))) {
            StringBuilder responseBuilder = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                responseBuilder.append(responseLine.trim());
            }
            return responseBuilder.toString();
        }
    }

    private String getData(String token, String pathVariable) {
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            try {
                HttpGet request = new HttpGet(URI.concat(pathVariable));

                request.addHeader("Authorization", "Bearer ".concat(token));
                request.addHeader("content-type", "application/json");
                request.addHeader("Accept", "application/json");

                HttpResponse response = httpClient.execute(request);
                HttpEntity entity = response.getEntity();
                return getResponse(entity);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error, Cannot Establish Connection");
            } finally {
                httpClient.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
