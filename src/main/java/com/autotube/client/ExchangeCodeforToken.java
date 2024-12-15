package com.autotube.client;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.autotube.constant.UtilConstants;
import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.auth.oauth2.TokenResponseException;
import com.google.api.client.http.BasicAuthentication;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.javanet.NetHttpTransport;

@Service
public class ExchangeCodeforToken {
    
    @Autowired
    private Environment env;
    public String exchangeCodeForToken(String code) {
        String tokenUrl = "https://oauth2.googleapis.com/token";

        // Set up the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String clientId = env.getProperty(UtilConstants.CLIENT_ID);
        String clientSecret = env.getProperty(UtilConstants.CLIENT_SECRET);
        String redirectUri = "http://localhost:8081/Callback";

        String requestBody = "code=" + URLEncoder.encode(code, StandardCharsets.UTF_8) +
                "&client_id=" + URLEncoder.encode(clientId, StandardCharsets.UTF_8) +
                "&client_secret=" + URLEncoder.encode(clientSecret, StandardCharsets.UTF_8) +
                "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8) +
                "&grant_type=authorization_code";

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, request, String.class);

            System.out.println("Response Body: " + response.getBody());
            return response.getBody();
        } catch (Exception e) {
            System.err.println("=== OAuth Error ===");
            System.err.println("Status: " + e.getMessage());
            throw new RuntimeException("OAuth validation failed: " + e.getMessage(), e);
        }
    }
    
    
//    public String requestAccessToken(String code) throws IOException {
//    	 try {
////    		 356703392767-p58vj25jthljfspe2edp52hn9v7evpjg.apps.googleusercontent.com
////    		 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
////    		 http://localhost:8081/Callback
//    	        String tokenUrl = "https://oauth2.googleapis.com/token";
//    	        String clientId = env.getProperty(UtilConstants.CLIENT_ID);
//    	        String clientSecret = env.getProperty(UtilConstants.CLIENT_SECRET);
//    	 TokenResponse response =
//    	 new AuthorizationCodeTokenRequest(new NetHttpTransport(), new GsonFactory(),
//    	 new GenericUrl(tokenUrl), code)
//    	 .setRedirectUri("http://localhost:8081/Callback")
//    	 .setClientAuthentication(
//    	 new BasicAuthentication(clientId,clientSecret )).execute();
//    	 System.out.println("Access token: " + response.getAccessToken());
//    	 return response.getAccessToken();
//    	 } catch (TokenResponseException e) {
//    	 if (e.getDetails() != null) {
//    	 System.err.println("Error: " + e.getDetails().getError());
//    	 if (e.getDetails().getErrorDescription() != null) {
//    	 System.err.println(e.getDetails().getErrorDescription());
//    	 }
//    	 if (e.getDetails().getErrorUri() != null) {
//    	 System.err.println(e.getDetails().getErrorUri());
//    	 }
//    	 } else {
//    	 System.err.println(e.getMessage());
//    	 }
//    	 }
//    	 return null;
//    	 }
    
   
}