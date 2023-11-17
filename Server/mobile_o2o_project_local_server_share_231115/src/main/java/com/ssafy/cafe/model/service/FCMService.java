package com.ssafy.cafe.model.service;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import org.apache.http.HttpHeaders;
import com.google.gson.JsonObject;
import com.ssafy.cafe.FcmMessage;
import com.ssafy.cafe.FcmMessage.Message;
import com.ssafy.cafe.FcmMessage.Notification;

import okhttp3.MediaType;
import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Service
public class FCMService {
	
	private final String API_URL = "https://fcm.googleapis.com/v1/projects/smartstore-83706/messages:send";
	
	
	private String getAccessToken() throws IOException{
		String firebaseConfigPath = "firebase/firebase_service_key.json";
		GoogleCredentials googleCredentials = GoogleCredentials
				.fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
				.createScoped(Arrays.asList("https://www.googleapis.com/auth/cloud-platform"));
		
		googleCredentials.refreshIfExpired();
		String token = googleCredentials.getAccessToken().getTokenValue();
		return token;
	}
	
	
	
	public void sendMessageTo(String targetToken, String title, String body) throws IOException{
		

		String message = makeMessage(targetToken, title, body);
		OkHttpClient client = new OkHttpClient();
		RequestBody requestBody = RequestBody.create(message, MediaType.get("application/json; charset=utf-8"));
		Request request = new Request.Builder()
				.url(API_URL)
				.post(requestBody)
				.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
				.addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
				.build();
		
		Response response = client.newCall(request).execute();
		System.out.println(response.body().string());
	
	}
	
	private String makeMessage(String targetToken, String title, String body) throws JsonProcessingException{
		Notification noti = new FcmMessage.Notification(title, body, null);
		Message message = new FcmMessage.Message(noti, targetToken);
		FcmMessage fcmMessage = new FcmMessage(false, message);
		
		return new ObjectMapper().writeValueAsString(fcmMessage);
		
	}

}
