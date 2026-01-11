package org.backend.service.impl;

import org.backend.constant.ResponseCode;
import org.backend.entity.Books;
import org.backend.repository.NewsRepository;
import org.backend.request.BooksRequest;
import org.backend.response.ViewNewsListResponse;
import org.backend.response.embedded.CreateBooksResponse;
import org.backend.response.embedded.ViewNewsIdResponse;
import org.backend.response.embedded.ViewNewsResponse;
import org.backend.service.IBooks;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BooksImpl implements IBooks {
    private final NewsRepository newsRepository;

    public BooksImpl(NewsRepository newsRepository){
        this.newsRepository = newsRepository;
    }

    @Override
    public CreateBooksResponse createBooks(BooksRequest request) {
        CreateBooksResponse.DTO dto = CreateBooksResponse.DTO.builder()
                .title(request.getTitle())
                .build();

        Books books = Books.builder()
                .slug(request.getSlug())
                .title(request.getTitle())
                .author(request.getAuthor())
                .coverUrl(request.getCoverUrl())
                .pdfUrl(request.getPdfUrl())
                .category(request.getCategory()!=null?request.getCategory():null)
                .totalReads(0)
                .totalBookmarks(0)
                .totalFavorites(0)
                .bookmarked(false)
                .build();

        newsRepository.save(books);

        return CreateBooksResponse.buildResponse(dto, ResponseCode.SUCCESS);
    }

    @Override
    public ViewNewsIdResponse viewBooksById(int id){
        Optional<Books> optNews = newsRepository.findById(id);
        if(optNews.isPresent()){
            Books books = optNews.get();
            books.setTotalReads(books.getTotalReads() + 1);
            newsRepository.save(books);
            ViewNewsIdResponse.DTO dto = ViewNewsIdResponse.DTO.builder()
                    .slug(books.getSlug())
                    .title(books.getTitle())
                    .author(books.getAuthor())
                    .coverUrl(books.getCoverUrl())
                    .pdfUrl(books.getPdfUrl())
                    .category(books.getCategory()!=null?books.getCategory():null)
                    .totalReads(books.getTotalReads())
                    .totalBookmarks(books.getTotalBookmarks())
                    .totalFavorites(books.getTotalFavorites())
                    .bookmarked(books.isBookmarked())
                    .build();
            return ViewNewsIdResponse.buildResponse(dto, ResponseCode.SUCCESS);
        }
        return ViewNewsIdResponse.buildResponse(null, ResponseCode.NEWS_NOTFOUND);
    }

    @Override
    public ViewNewsListResponse viewBooksByCategory(String category){
        int pageSize = 10; // Define the page size (10 items per page)
        Pageable pageable = PageRequest.of(0, pageSize);
        Page<Books> news = newsRepository.findByCategory(category, pageable);
        List<Books> booksList = news.getContent(); // Retrieve the news items from the page
        int totalPages = news.getTotalPages();
        if(booksList.isEmpty()) {
            return ViewNewsListResponse.buildResponse(null, ResponseCode.NEWS_NOTFOUND);
        }
        List<ViewNewsResponse> viewNewsResponses = booksList.stream()
                .map(b -> new ViewNewsResponse(
                        b.getSlug(),
                        b.getTitle(),
                        b.getAuthor(),
                        b.getCoverUrl(),
                        b.getPdfUrl(),
                        b.getCategory()!=null?b.getCategory():null,
                        b.getTotalReads(),
                        b.getTotalBookmarks(),
                        b.getTotalFavorites(),
                        b.isBookmarked()
                )).toList();

        return ViewNewsListResponse.buildResponse(viewNewsResponses, ResponseCode.SUCCESS);
    }

}
