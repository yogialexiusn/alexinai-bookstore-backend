package org.backend.controller;

import org.backend.request.BooksRequest;
import org.backend.response.ViewNewsListResponse;
import org.backend.response.embedded.*;
import org.backend.service.IBooks;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/news")
@Validated
public class NewsController extends BaseController{

    private final IBooks iBooks;

    public NewsController(IBooks iBooks) {
        this.iBooks = iBooks;
    }


//    @GetMapping
//    public List<NewsDto> getAllNews() {
//        return newsRepository.findAll()
//                .stream()
//                .map(news -> new NewsDto(news.getId(), news.getTitle(), news.getContent(), news.getCategory(), news.getImageUrl()))
//                .collect(Collectors.toList());
//    }
//
    @GetMapping("/{id}")
    public ResponseEntity<ViewNewsIdResponse> getNewsById(@PathVariable int id) {
        return execute(iBooks.viewBooksById(id));
    }

    @GetMapping
    public ResponseEntity<ViewNewsListResponse> getNewsByCategory(@RequestParam(value="category") String category) {
        return execute(iBooks.viewBooksByCategory(category));
    }

    @PostMapping
    public ResponseEntity<CreateBooksResponse> addNews(@RequestBody BooksRequest request) {
        return execute(iBooks.createBooks(request));
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<NewsDto> updateNews(@PathVariable Long id, @RequestBody NewsDto newsDto) {
//        return newsRepository.findById(id)
//                .map(existingNews -> {
//                    existingNews.setTitle(newsDto.getTitle());
//                    existingNews.setContent(newsDto.getContent());
//                    existingNews.setCategory(newsDto.getCategory());
//                    existingNews.setImageUrl(newsDto.getImageUrl());
//                    Books updatedNews = newsRepository.save(existingNews);
//                    return ResponseEntity.ok(new NewsDto(updatedNews.getId(), updatedNews.getTitle(), updatedNews.getContent(), updatedNews.getCategory(), updatedNews.getImageUrl()));
//                })
//                .orElseGet(() -> ResponseEntity.notFound().build());
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteNews(@PathVariable Long id) {
//        if (newsRepository.existsById(id)) {
//            newsRepository.deleteById(id);
//            return ResponseEntity.noContent().build();
//        }
//        return ResponseEntity.notFound().build();
//    }

}
