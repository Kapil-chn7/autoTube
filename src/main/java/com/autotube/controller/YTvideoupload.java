package com.autotube.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.autotube.client.Auth;
import com.autotube.service.YouTubeService;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;

import jakarta.servlet.http.HttpSession;

@RestController
public class YTvideoupload {

	
	 @GetMapping("/authorize")
	    public RedirectView authorize() {
	        String viewUrl = Auth.getAuthorizationUrl();
	        return new RedirectView(viewUrl);
	    }

	    @GetMapping("/Callback")
	    public String callback(@RequestParam("code") String code, HttpSession session) throws IOException {
	        TokenResponse tokenResponse = Auth.exchangeCode(code);
	        Credential credential = Auth.getCredentialsFromTokenResponse(tokenResponse);
	        System.out.println("this is the tokenResponse "+tokenResponse);
	        
	        ClassLoader classLoader = YTvideoupload.class.getClassLoader(); 
	        URL resource = classLoader.getResource("10secondVideo.mp4");
	        if (resource != null) {
	            File videoFile = new File(resource.getFile());
	            String videoId = YouTubeService.uploadVideo(credential, videoFile, "Video Title", "Video Description");
	           
	            System.out.println("Uploaded video ID: " + videoId);
	            return videoId;
	        } else {
	            System.err.println("File not found in resources!");
	        }

//
	        return "File Not Found";
	    }
}
