package com.autotube.builder;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.autotube.constant.UtilConstants;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.youtube.YouTube;

@Configuration
public class YoutubeBuilder {

//	@Autowired
//	private Environment env;
//
//	
//	@Bean
//	Credential authorize() throws Exception {
//	    AuthorizationCodeFlow flow = new AuthorizationCodeFlow.Builder(
//	            BearerToken.authorizationHeaderAccessMethod(),
//	            new NetHttpTransport(),
//	            GsonFactory.getDefaultInstance(),
//	            new GenericUrl(env.getProperty(UtilConstants.TOKEN_URI)),
//	            new ClientParametersAuthentication(env.getProperty(UtilConstants.CLIENT_ID), env.getProperty(UtilConstants.CLIENT_SECRET)),
//	            env.getProperty(UtilConstants.CLIENT_ID),
//	            env.getProperty(UtilConstants.AUTH_URI)
//	    ).setScopes(Arrays.asList(
//	    	    "openid",
//	    	    "https://www.googleapis.com/auth/youtube.upload",
//	    	    "https://www.googleapis.com/auth/youtube.force-ssl"
//	    	))
//	    		 
//	     .build();
//
//	    LocalServerReceiver receiver = new LocalServerReceiver.Builder()
//	            .setPort(8081)
//	            .setCallbackPath("/Callback") 
//	            .build();
//
//	    return new AuthorizationCodeInstalledApp(flow, receiver).authorize("Kapil");
//	}
//
//	@Bean
//	YouTube createYoutubeBuilder() throws Exception {
//		HttpTransport httpTransport = new NetHttpTransport();
//		GsonFactory jsonFactory = GsonFactory.getDefaultInstance();
//
//		return new YouTube.Builder(httpTransport, jsonFactory, authorize()).setApplicationName("autotube")
//				.build();
//	}
}
