package org.backend.request;

import lombok.Data;

@Data
public class BooksRequest {
    private String slug;
    private String title;
    private String author;
    private String coverUrl;
    private String pdfUrl;
    private String category;
    private int totalReads;
    private int totalBookmarks;
    private int totalFavorites;
    private boolean bookmarked;
}