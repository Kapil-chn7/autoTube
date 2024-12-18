package com.autotube.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class YouTubeService {
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

   
    private static YouTube initYouTube(Credential credential) {
        return new YouTube.Builder(
            HTTP_TRANSPORT,
            JSON_FACTORY,
            credential
        ).setApplicationName("YouTube API Application").build();
    }

   
    private static String getUploadsPlaylistId(YouTube youtube) throws IOException {
        YouTube.Channels.List channelsListRequest = youtube.channels()
            .list("contentDetails");

        channelsListRequest.setMine(true);

        ChannelListResponse channelsListResponse = channelsListRequest.execute();

        if (channelsListResponse.getItems().isEmpty()) {
            return null;
        }

        Channel channel = channelsListResponse.getItems().get(0);
        return channel.getContentDetails()
            .getRelatedPlaylists()
            .getUploads();
    }

   
    private static List<String> getVideoIds(YouTube youtube, String playlistId) throws IOException {
        YouTube.PlaylistItems.List playlistRequest = youtube.playlistItems().list("snippet");
        playlistRequest.setPlaylistId(playlistId);
        PlaylistItemListResponse playlistResponse = playlistRequest.execute();

        return playlistResponse.getItems().stream()
            .map(item -> item.getSnippet().getResourceId().getVideoId())
            .collect(Collectors.toList());
    }

    
    private static String formatVideoList(List<Video> videos) {
        StringBuilder sb = new StringBuilder();
        for (Video video : videos) {
            VideoSnippet snippet = video.getSnippet();
            VideoStatistics stats = video.getStatistics();
            sb.append("<p>Video ID: ").append(video.getId()).append("</p>")
                .append("<p>Title: ").append(snippet.getTitle()).append("</p>")
                .append("<p>Published At: ").append(snippet.getPublishedAt()).append("</p>")
                .append("<p>View Count: ").append(stats.getViewCount()).append("</p>")
                .append("<p>Like Count: ").append(stats.getLikeCount()).append("</p>")
                .append("<p>Dislike Count: ").append(stats.getDislikeCount()).append("</p>")
                .append("<p>Comment Count: ").append(stats.getCommentCount()).append("</p>")
                .append("<hr>");
        }
        return sb.toString();
    }

    
    public static String getVideoList(Credential credential) throws IOException {
        YouTube youtube = initYouTube(credential);
        String playlistId = getUploadsPlaylistId(youtube);
        List<String> videoIds = getVideoIds(youtube, playlistId);

        YouTube.Videos.List videoRequest = youtube.videos().list("snippet,statistics");
        videoRequest.setId(String.join(",", videoIds));
        VideoListResponse videoResponse = videoRequest.execute();

        return formatVideoList(videoResponse.getItems());
    }

    
    public static String uploadVideo(
        Credential credential,
        File videoFile,
        String title,
        String description
    ) throws IOException {
        YouTube youtube = initYouTube(credential);

        // Define video metadata
        Video videoObject = new Video();
        VideoStatus status = new VideoStatus();
        status.setPrivacyStatus("private"); 
        videoObject.setStatus(status);

        VideoSnippet snippet = new VideoSnippet();
        snippet.setTitle(title);
        snippet.setDescription(description);
        videoObject.setSnippet(snippet);

        // Prepare the video file for upload
        InputStreamContent mediaContent = new InputStreamContent(
            "video/*",
            new BufferedInputStream(new FileInputStream(videoFile))
        );

        mediaContent.setLength(videoFile.length());

        // Upload video
        YouTube.Videos.Insert videoInsert = youtube.videos().insert(
            "snippet,statistics,status",
            videoObject,
            mediaContent
        );

        Video returnedVideo = videoInsert.execute();

        // Return video ID of the uploaded video
        return "Video Uploaded Successfully. Video ID is " + returnedVideo.getId();
    }
}