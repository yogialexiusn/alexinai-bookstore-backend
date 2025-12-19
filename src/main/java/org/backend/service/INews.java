package org.backend.service;

import org.backend.request.NewsRequest;
import org.backend.response.ViewNewsListResponse;
import org.backend.response.embedded.CreateNewsResponse;
import org.backend.response.embedded.ViewNewsIdResponse;

public interface INews {
    CreateNewsResponse createNews(NewsRequest request);
    ViewNewsIdResponse viewNewsById(int id);
    ViewNewsListResponse viewNewsByCategory(String category);
//    BlockUserResponse blockUser(BlockUserRequest request);
//    CreateAccessResponse createAccess(CreateAccessRequest request);
//    GetUserResponse getUser(String username);
//    GetUserAccessListResponse getUserAccess(String username);

}
