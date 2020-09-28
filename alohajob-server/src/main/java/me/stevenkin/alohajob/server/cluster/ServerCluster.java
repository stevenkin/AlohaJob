package me.stevenkin.alohajob.server.cluster;

import me.stevenkin.alohajob.common.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ServerCluster {
    private static final String ping_url = "http://%s/server/ping";
    @Autowired
    private RestTemplate restTemplate;

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
