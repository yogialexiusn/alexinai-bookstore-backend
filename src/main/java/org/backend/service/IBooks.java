package org.backend.service;

import org.backend.request.BooksRequest;
import org.backend.response.ViewNewsListResponse;
import org.backend.response.embedded.CreateBooksResponse;
import org.backend.response.embedded.ViewNewsIdResponse;

public interface IBooks {
    CreateBooksResponse createBooks(BooksRequest request);
    ViewNewsIdResponse viewBooksById(int id);
    ViewNewsListResponse viewBooksByCategory(String category);
//    BlockUserResponse blockUser(BlockUserRequest request);
//    CreateAccessResponse createAccess(CreateAccessRequest request);
//    GetUserResponse getUser(String username);
//    GetUserAccessListResponse getUserAccess(String username);

}
