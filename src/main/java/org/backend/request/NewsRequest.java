package org.backend.request;

import lombok.Data;

@Data
public class NewsRequest {
    private String title;
    private String content;
    private String category;
    private String imageUrl;
}