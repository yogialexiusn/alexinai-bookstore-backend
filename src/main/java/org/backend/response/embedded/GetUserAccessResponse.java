package org.backend.response.embedded;

import lombok.*;
import org.backend.entity.Menu;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetUserAccessResponse {
    private String username;
    private String menuAccess;
    private String roleName;
    private Timestamp createdTime;
    private Timestamp updatedTime;
    private String heading;
    private String icon;
    private String text;
    private String link;
    private List<Menu> subMenu;

}
