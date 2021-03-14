package com.clover.blog.service;

import com.clover.blog.dao.BlogDao;
import com.clover.blog.elasticsearch.entity.EsBlog;
import com.clover.blog.entity.Blog;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
@Transactional
public class BlogServiceImp implements BlogService{

    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    private final BlogDao blogDao;

    public BlogServiceImp(BlogDao blogDao) {
        this.blogDao = blogDao;
    }

    public boolean findBlogId(String blogId) {
        return blogId!=null && !blogId.equals("") && isInteger(blogId) && blogDao.findBlogById(blogId) != null;
    }

    @Override
    public Blog findBlogById(String blogId) {
        return findBlogId(blogId) ? blogDao.findBlogById(blogId) : null;
    }

    @Override
    public void addBlog(Blog blog) {
        blogDao.insertBlog(blog);
    }

    @Override
    public Blog[] findBlogByUser(String userId) {
        return blogDao.findBlogByUser(userId);
    }

    @Override
    public int addView(String blogId) {
        return findBlogId(blogId) ? blogDao.updateBlogView(blogId) : 0;
    }

    @Override
    public int changeBlogVisible(String blogId, boolean visible) {
        return findBlogId(blogId) ? blogDao.updateBlogVisible(blogId, visible) : 0;
    }

    @Override
    public int changeLike(String blogId, boolean add) {
        return add ? blogDao.addBlogLike(blogId) : blogDao.decBlogLike(blogId);
    }

    // es
    @Resource
    private RestHighLevelClient restHighLevelClient;

    @Override
    public List<EsBlog> getSearchResult(Integer id, String keyword) throws IOException {
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        builder.filter(QueryBuilders.matchQuery("userId", id));
        builder.must(QueryBuilders.multiMatchQuery(keyword, "title", "preview", "content", "tags"));

        SearchRequest searchRequest = new SearchRequest("blog");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(builder)
                .highlighter(new HighlightBuilder().field("*").requireFieldMatch(false).preTags("<span style='color:red'>").postTags("</span>"));
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] hits = searchResponse.getHits().getHits();
        List<EsBlog> blogs = new ArrayList<>();
        for (SearchHit hit : hits) {
            Map<String, Object> map = hit.getSourceAsMap();
            EsBlog blog = new EsBlog();
            blog.setBlogId(hit.getId());
            blog.setUserId(Integer.parseInt(map.get("userId").toString()));
            blog.setPreview(map.get("preview").toString());
            blog.setTitle(map.get("title").toString());
            blog.setTags(map.get("tags").toString());
            blog.setContent(map.get("content").toString());

            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            if (highlightFields.containsKey("preview"))
                blog.setPreview(highlightFields.get("preview").fragments()[0].toString());
            if (highlightFields.containsKey("title"))
                blog.setTitle(highlightFields.get("title").fragments()[0].toString());
            if (highlightFields.containsKey("tags"))
                blog.setTags(highlightFields.get("tags").fragments()[0].toString());
            if (highlightFields.containsKey("content"))
                blog.setContent(highlightFields.get("content").fragments()[0].toString());

            blogs.add(blog);
        }
        return blogs;
    }

    @Override
    public Blog[] findAll() {
        return blogDao.findAll();
    }
}
