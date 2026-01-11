package org.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_books")
public class Books {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(length = 50)
    private String slug;
    @Column(length = 100)
    private String title;
    @Column(length = 50)
    private String author;
    @Column(length = 5000)
    private String coverUrl;
    @Column(length = 5000)
    private String pdfUrl;
    @Column(length = 50)
    private String category;
    private int totalReads;
    private int totalBookmarks;
    private int totalFavorites;
    private boolean bookmarked;
    @CreationTimestamp
    private Timestamp createdTime;
    @UpdateTimestamp
    private Timestamp updatedTime;

}
