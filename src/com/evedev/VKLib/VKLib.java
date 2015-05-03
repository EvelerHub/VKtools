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
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Eveler on 03.05.2015.
 */
public class VKLib {

    private String access_token;
    private URIBuilder uriBuilder;

    public VKLib() {
        try {
            getFollowersList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getFollowersList() throws IOException {
        Followers followers = new Followers("tproger");
        ArrayList<JSONObject> jsonObjects = new ArrayList<JSONObject>();
        while (followers.hasNext()){
            jsonObjects.addAll(followers.next());
        }
        System.out.println(jsonObjects.size());
    }
}
