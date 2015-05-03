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
        uriBuilder.setScheme("https").setHost("api.vk.com").setPath("/method/groups.getMembers")
                //.setParameter("owner_id", "-57948931")
                .setParameter("group_id","57948931")
                //.setParameter("order","name")
                //.setParameter("domain","nix_solutions");
                //.setParameter("offset","1")
                .setParameter("fields", "first_name,last_name")
                .setParameter("access_token", "c1f003743da92c1c46d7343ba4e4aed26e42240907351df085644ab76a7d1cae297ce869f989336b08091")
                .setParameter("count", "5000");

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
                System.out.println(jsonResp);
                JSONObject postsList = (JSONObject) jsonResp.get("response");
                JSONArray users = (JSONArray) postsList.get("users");
                for (int i = 1; i < users.size(); i++) {
                    JSONObject unicPost = (JSONObject) users.get(i);
                    System.out.print(i + " ");
                    try {
                        Object first_name = unicPost.get("first_name");
                        Object last_name = unicPost.get("last_name");
                        System.out.println(first_name + " " + last_name);


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
