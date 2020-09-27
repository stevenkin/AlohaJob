package me.stevenkin.alohajob.server.cluster;

import me.stevenkin.alohajob.common.response.Response;
import org.springframework.web.client.RestTemplate;

public class ServerCluster {
    private static final String ping_url = "http://%s/server/ping";

    private RestTemplate restTemplate;

    public ServerCluster(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean ping(String serverAddress) {
        String url = String.format(ping_url, serverAddress);
        try{
            restTemplate.getForEntity(url, Response.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
