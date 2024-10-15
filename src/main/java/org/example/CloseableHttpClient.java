package org.example;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

public class CloseableHttpClient {
    public static void main(String[] args) {
        try {
            List<CatFact> catFacts = fetchCatFacts();
            List<CatFact> filteredCatFacts = catFacts.stream()
                    .filter(fact -> fact.getUpvotes() != null && fact.getUpvotes() > 0)
                    .collect(Collectors.toList());

            filteredCatFacts.forEach(fact -> System.out.println(fact.getText()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<CatFact> fetchCatFacts() throws IOException {
        org.apache.http.impl.client.CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();

    HttpGet request = new HttpGet("https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats");

    try ( CloseableHttpResponse response = httpClient.execute(request);
    InputStream inputStream = response.getEntity().getContent())
    {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(inputStream, new TypeReference<List<CatFact>>() {
        });
    }
    }


}
