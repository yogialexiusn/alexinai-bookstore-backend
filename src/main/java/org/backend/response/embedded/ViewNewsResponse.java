package org.backend.response.embedded;

import lombok.*;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViewNewsResponse {

    private int id;
    private String title;
    private String content;
    private String category;
    private String imageUrl;
    private Timestamp createdTime;
    private Timestamp updatedTime;

}
