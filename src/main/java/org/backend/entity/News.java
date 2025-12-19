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
@Table(name = "t_news")
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(length = 50)
    private String title;
    @Column(length = 5000)
    private String content;
    @Column(length = 100)
    private String category;
    @Column(length = 500)
    private String imageUrl;
    @CreationTimestamp
    private Timestamp createdTime;
    @UpdateTimestamp
    private Timestamp updatedTime;
    @Column(columnDefinition = "INT DEFAULT 0")
    private int views;

}
