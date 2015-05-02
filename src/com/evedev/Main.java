package com.evedev;


import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Eveler on 30.04.2015.
 */

abstract class HttpConnectionAgent {

    public static HttpResponse connectResponse(URIBuilder uriBuilder) {

        URI uri = null;

        try {
            uri = uriBuilder.build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(uri);
        HttpResponse response = null;

        try {
            response = client.execute(request);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        return response;
    }
}

public class Main {
    public static void main(String[] args) {

        URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.setScheme("https").setHost("api.vk.com").setPath("/method/wall.get")
                .setParameter("domain", "mhkoff")
                        // .setParameter("access_token", ACCESS_TOKEN)
                .setParameter("count", "10");

        HttpResponse response = HttpConnectionAgent.connectResponse(uriBuilder);
        Integer status = response.getStatusLine().getStatusCode();

        if (status == 200) {
            StringWriter content = new StringWriter();

            try {
                IOUtils.copy(response.getEntity().getContent(), content);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(-1);
            }

            JSONParser parser = new JSONParser();

            try {

                JSONObject jsonResp = (JSONObject) parser.parse(content.toString());
                JSONArray postsList = (JSONArray) jsonResp.get("response");
                JSONObject unicPost = null;

                for (int i = 1; i < postsList.size(); i++) {
                    unicPost = (JSONObject) postsList.get(i);
                    System.out.print(i + " ");
                    try {
                        JSONArray attachments = (JSONArray) unicPost.get("attachments");
                        for (Object attachment : attachments) {
                            JSONObject attach = (JSONObject) attachment;
                            JSONObject photo = (JSONObject) attach.get("photo");
                            Object src_big = photo.get("src_big");

                            System.out.println(src_big);
                        }


                    } catch(NullPointerException e){
                        System.out.println("NullPointerException");
                    }

                    System.out.println("*******************************");
                }

            } catch (ParseException e) {
                e.printStackTrace();
                System.exit(-1);
            } catch (org.json.simple.parser.ParseException e) {
                e.printStackTrace();
            }
        }
    }
}
