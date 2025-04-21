package org.alainshop.repository;

import org.alainshop.model.NewsPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewsRepository extends JpaRepository<NewsPost, Long> {
    List<NewsPost> findAllByOrderByCreatedAtDesc();
}
