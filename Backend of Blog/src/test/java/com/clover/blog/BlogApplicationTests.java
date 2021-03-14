package com.clover.blog;

import com.clover.blog.elasticsearch.dao.BlogRepository;
import com.clover.blog.elasticsearch.entity.EsBlog;
import com.clover.blog.entity.Blog;
import com.clover.blog.entity.Result;
import com.clover.blog.entity.User;
import com.clover.blog.service.BlogService;
import com.clover.blog.service.UserService;
import com.clover.blog.utils.ApplicationContextUtils;
import com.clover.blog.utils.OtherUtils;
import org.apache.ibatis.cache.Cache;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class BlogApplicationTests {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private BlogRepository blogRepository;

    @Resource
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private UserService userService;
    @Test
    void contextLoads() {
       //stringRedisTemplate.opsForValue().set("abc","123",60, TimeUnit.SECONDS);
        String[] styles = new String[]{"anime", "Ghibli", "cyberpunk", "nature"};
        System.out.println(styles[new Random().nextInt(styles.length)]);
    }

    @Test
    public void testCache() {
        for (User user : userService.allUser()) {

        }
        for (User user : userService.allUser()) {
            System.out.println(user.getId());
        }
    }

    @Test
    public void testMyCache() {
        Cache cache;
        for (User user : userService.allUser()) {

        }
        for (User user : userService.allUser()) {
            System.out.println(user.getId());
        }
    }

    @Test
    public void saveOne() {
        EsBlog esBlog = new EsBlog();
        List<String> list = new ArrayList<>();
        list.add("java");
        list.add("python");
        esBlog.setBlogId("3");
        esBlog.setContent("这是内容这是内容1111");
        esBlog.setPreview("这是摘要摘要22222");
        esBlog.setTags(list.toString());
        esBlog.setTitle("这是标题标题3333333");
        esBlog.setUserId(0);
        blogRepository.save(esBlog);
    }



    @Test
    public void findAllByUserIdAndOthers() throws IOException {
        int id = 0;
        String Keyword = "AQS";
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        builder.filter(QueryBuilders.matchQuery("userId", id));
        builder.must(QueryBuilders.multiMatchQuery(Keyword, "title", "preview", "content", "tags"));

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

        for (EsBlog blog : blogs) {
            System.out.println(blog);
        }
    }

    @Autowired
    BlogService blogService;
    @Test
    public void testSearch() {
        try {
            List<EsBlog> searchResult = blogService.getSearchResult(0, "java");
            for (EsBlog esBlog : searchResult) {
                System.out.println(esBlog);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFindAll() {
        for (Blog blog : blogService.findAll()) {
            System.out.println(blog);
        }
        File file = new File("C:\\Users\\85127\\Desktop\\md\\Linux学习笔记.md");
        System.out.println(txt2String(file));
    }

    public static String txt2String(File file){
        StringBuilder result = new StringBuilder();
        String newline = "\r\n";
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
                result.append(s);
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return result.toString();
    }

    @Test
    public void te() {
        String a = "<div style=\"background-image: url('https://www.luckyclover.top/blogs/0/imgs/g.jpg');background-size: cover;color: var(--color);border-radius: 10px;padding:4px;opacity:0.9;\">博客的前后端基本完成，欢迎大家注册体验。\uD83D\uDE0A<br>&emsp;&emsp;\n" +
                "源码放在<a style=\"color: rgb(28 210 150);opacity: var(--opacity);\" href=\"https://github.com/iCodek/CloverBlog\">Github</a>上，公告栏支持html标签，图片视频都可以放。\n" +
                "<br>&emsp;&emsp;Markdown语法可参见<a style=\"color: rgb(28 210 150);opacity: var(--opacity);\"href=\"https://www.luckyclover.top/25/37\">常在心</a>的博客。插入视频使用video或iframe标签(例如B站视频)。<br>&emsp;&emsp;每天进步一点点。";
        System.out.println(a.length());
//        Result<List<EsBlog>> res = search(0, "java");
//        System.out.println(res.toString());
    }

    public Result<List<EsBlog>> search(Integer userId, String keyword) {
        try {
            List<EsBlog> searchResult = blogService.getSearchResult(userId, keyword);
            return Result.success(searchResult);
        } catch (IOException e) {
            return Result.fail(e.getMessage());
        }
    }

    @Test
    public void testFindOne() {
        List<EsBlog> aqs = blogRepository.findAllByTitle("AQS");
        for (EsBlog aq : aqs) {
            System.out.println(aq);
        }
    }

    @Test
    public void testRedis () {
        for (int i = 0; i < 50; i++) {
            RedisTemplate<String, String> redisTemplate = getRedisTemplate();
            String count = redisTemplate.opsForValue().get("COUNT_"+ 11);
            System.out.println(count);
            if (count == null) {
                redisTemplate.opsForValue().set("COUNT_"+11, "1", 50 , TimeUnit.SECONDS);

            } else {
                int times = Integer.parseInt(count);
                count = times + 1 + "";
                redisTemplate.opsForValue().set("COUNT_"+11, count, 50 , TimeUnit.SECONDS);
            }
        }

    }

    private RedisTemplate<String, String> getRedisTemplate () {
        RedisTemplate<String, String> redisTemplate = (RedisTemplate) ApplicationContextUtils.getBean("redisTemplate");
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }
}
