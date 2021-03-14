package com.clover.blog.elasticsearch.dao;

import com.clover.blog.elasticsearch.entity.EsBlog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface BlogRepository extends ElasticsearchRepository<EsBlog, Integer> {
    List<EsBlog> findAllByTitle(String title);
}
