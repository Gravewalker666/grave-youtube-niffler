package com.gravewalker.graveyoutubeniffler.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@CrossOrigin(origins = "*")
@RestController
public class NifflerController {

    static String api_key = System.getenv().get("API_KEY");

    @GetMapping("/playlist")
    @ResponseStatus(HttpStatus.OK)
    public void getPlaylist(@RequestParam String playlistId, HttpServletResponse response) throws IOException {
        String url = "https://www.googleapis.com/youtube/v3/playlistItems?" +
                "part=snippet&" +
                "playlistId=" + playlistId +
                "&key=" + api_key;

        HttpClient httpClient = HttpClients.createDefault();
        HttpGet executor = new HttpGet(url);
        try {
            HttpResponse httpResponse = httpClient.execute(executor);
            BufferedReader br = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(),
                    StandardCharsets.UTF_8));
            StringBuilder processedResponse = new StringBuilder();
            String row;
            while ((row = br.readLine()) != null) {
                processedResponse.append(row);
            }
            br.close();
            response.setContentType("application/json");
            response.getWriter().write(processedResponse.toString());
        } catch (IOException e) {
            response.setStatus(500);
        }
    }

    @GetMapping("/videos")
    @ResponseStatus(HttpStatus.OK)
    public void getVideos(@RequestParam String[] videoIds, HttpServletResponse response) throws IOException {
        StringBuilder url = new StringBuilder("https://www.googleapis.com/youtube/v3/videos" +
                "?part=snippet" +
                "&part=statistics" +
                "&part=contentDetails");
        for (String id: videoIds) {
            url.append("&id=").append(id);
        }
        url.append("&key=").append(api_key);
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet executor = new HttpGet(url.toString());
        try {
            HttpResponse httpResponse = httpClient.execute(executor);
            BufferedReader br = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(),
                    StandardCharsets.UTF_8));
            StringBuilder processedResponse = new StringBuilder();
            String row;
            while ((row = br.readLine()) != null) {
                processedResponse.append(row);
            }
            br.close();
            response.setContentType("application/json");
            response.getWriter().write(processedResponse.toString());
        } catch (IOException e) {
            response.setStatus(500);
        }
    }
}
