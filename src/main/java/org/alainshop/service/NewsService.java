package org.alainshop.service;

import lombok.RequiredArgsConstructor;
import org.alainshop.model.NewsPost;
import org.alainshop.repository.NewsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsService {
    private final NewsRepository newsRepository;

    public List<NewsPost> getAllNews() {
        return newsRepository.findAllByOrderByCreatedAtDesc();
    }
}
