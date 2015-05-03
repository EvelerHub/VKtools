package com.evedev.VKLib;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.utils.URIBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;

/**
 * Created by Eveler on 03.05.2015.
 */
public class Followers implements Iterator<JSONArray> {

    private String group_id;
    private int offset;
    private int count;
    private URIBuilder uriBuilder;
    private HttpResponse response;

    public Followers(String group_id) throws IOException {
        this.group_id = group_id;
        offset = 0;
        getCount();
    }

    private void getCount(){

        try {
            response = HttpConnectionAgent.connectResponse(buildURI(offset));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Integer status = response.getStatusLine().getStatusCode();

        if (status == 200) {
            StringWriter content = new StringWriter();

            try {
                IOUtils.copy(response.getEntity().getContent(), content);
            } catch (IOException e) {
                e.printStackTrace();
            }

            JSONParser parser = new JSONParser();
            try {
                JSONObject jsonResp = (JSONObject) parser.parse(content.toString());
                JSONObject postsList = (JSONObject) jsonResp.get("response");
                count = Integer.valueOf(postsList.get("count").toString());
                System.out.println(count);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private URIBuilder buildURI(int offset) {
        uriBuilder = new URIBuilder();

        uriBuilder.setScheme("https").setHost("api.vk.com").setPath("/method/groups.getMembers")
                .setParameter("group_id", group_id)
                .setParameter("fields", "first_name,last_name")
                .setParameter("access_token", "c1f003743da92c1c46d7343ba4e4aed26e42240907351df085644ab76a7d1cae297ce869f989336b08091")
                .setParameter("offset",String.valueOf(offset))
                .setParameter("count", "1000");

        return uriBuilder;
    }

    @Override
    public boolean hasNext() {
        return offset < count;
    }

    @Override
    public JSONArray next() {

        try {
            response = HttpConnectionAgent.connectResponse(buildURI(offset));
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONArray users = null;

        Integer status = response.getStatusLine().getStatusCode();
        if (status == 200) {
            StringWriter content = new StringWriter();

            try {
                IOUtils.copy(response.getEntity().getContent(), content);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                JSONParser parser = new JSONParser();
                JSONObject jsonResp = (JSONObject) parser.parse(content.toString());
                JSONObject postsList = (JSONObject) jsonResp.get("response");
                users = (JSONArray) postsList.get("users");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        System.out.println(offset);

        offset+=1000;

        return users;
    }

}
