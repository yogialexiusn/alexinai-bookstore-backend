package org.backend.service.impl;

import org.backend.constant.ResponseCode;
import org.backend.entity.News;
import org.backend.repository.NewsRepository;
import org.backend.request.NewsRequest;
import org.backend.response.ViewNewsListResponse;
import org.backend.response.embedded.CreateNewsResponse;
import org.backend.response.embedded.ViewNewsIdResponse;
import org.backend.response.embedded.ViewNewsResponse;
import org.backend.service.INews;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NewsImpl implements INews {
    private final NewsRepository newsRepository;

    public NewsImpl(NewsRepository newsRepository){
        this.newsRepository = newsRepository;
    }

    @Override
    public CreateNewsResponse createNews(NewsRequest request) {
        CreateNewsResponse.DTO dto = CreateNewsResponse.DTO.builder()
                .title(request.getTitle())
                .build();

        News news = new News();
        news.setCategory(request.getCategory());
        news.setTitle(request.getTitle());
        news.setImageUrl(request.getImageUrl());
        news.setContent(request.getContent());
        newsRepository.save(news);

        return CreateNewsResponse.buildResponse(dto, ResponseCode.SUCCESS);
    }

    @Override
    public ViewNewsIdResponse viewNewsById(int id){
        Optional<News> optNews = newsRepository.findById(id);
        if(optNews.isPresent()){
            News news = optNews.get();
            news.setViews(news.getViews() + 1);
            newsRepository.save(news);
            ViewNewsIdResponse.DTO dto = ViewNewsIdResponse.DTO.builder()
                    .id(news.getId())
                    .title(news.getTitle())
                    .content(news.getContent())
                    .category(news.getCategory())
                    .imageUrl(news.getImageUrl())
                    .createdTime(news.getCreatedTime())
                    .updatedTime(news.getUpdatedTime())
                    .build();
            return ViewNewsIdResponse.buildResponse(dto, ResponseCode.SUCCESS);
        }
        return ViewNewsIdResponse.buildResponse(null, ResponseCode.NEWS_NOTFOUND);
    }

    @Override
    public ViewNewsListResponse viewNewsByCategory(String category){
        int pageSize = 10; // Define the page size (10 items per page)
        Pageable pageable = PageRequest.of(0, pageSize);
        Page<News> news = newsRepository.findByCategory(category, pageable);
        List<News> newsList = news.getContent(); // Retrieve the news items from the page
        int totalPages = news.getTotalPages();
        if(newsList.isEmpty()) {
            return ViewNewsListResponse.buildResponse(null, ResponseCode.NEWS_NOTFOUND);
        }
        List<ViewNewsResponse> viewNewsResponses = newsList.stream()
                .map(news1 -> new ViewNewsResponse(
                        news1.getId(),
                        news1.getTitle(),
                        news1.getContent(),
                        news1.getCategory(),
                        news1.getImageUrl(),
                        news1.getCreatedTime(),
                        news1.getUpdatedTime()
                )).toList();

        return ViewNewsListResponse.buildResponse(viewNewsResponses, ResponseCode.SUCCESS);
    }

}
