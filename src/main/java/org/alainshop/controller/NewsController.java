package org.alainshop.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.alainshop.service.NewsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Controller
@RequestMapping("/news-feed")
@RequiredArgsConstructor
public class NewsController {
    NewsService newsService;

    @GetMapping
    public String getNewsPage(Model model) {
        model.addAttribute("posts", newsService.getAllNews());
        return "news-feed";
    }
}
