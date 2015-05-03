package com.evedev.VKLib;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Eveler on 03.05.2015.
 */
public abstract class HttpConnectionAgent {

    public static HttpResponse connectResponse(URIBuilder uriBuilder) throws IOException {
        HttpResponse response = null;

        try {
            URI uri = uriBuilder.build();
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(uri);
            response = client.execute(request);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return response;
    }
}