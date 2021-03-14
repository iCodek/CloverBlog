package com.clover.blog.controller;

import com.clover.blog.elasticsearch.dao.BlogRepository;
import com.clover.blog.elasticsearch.entity.EsBlog;
import com.clover.blog.entity.Blog;
import com.clover.blog.entity.Result;
import com.clover.blog.service.BlogService;
import com.clover.blog.utils.OtherUtils;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("blog")
@CrossOrigin
@Slf4j
public class BlogController {

    private Random random = new Random();
    @Value("${user.blog-path}")
    private String blogPath;
    @Value("${blogBox.path}")
    private String blogBoxPath;

    private final BlogService blogService;
    private final StringRedisTemplate stringRedisTemplate;

    static String[] styles = new String[]{"anime", "Ghibli", "cyberpunk", "nature"};
    public BlogController(BlogService blogService, StringRedisTemplate stringRedisTemplate) {
        this.blogService = blogService;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @PostMapping("getBlog")
    public void getBlog(String blogId, HttpServletResponse response) throws Exception {
        Blog blog = blogService.findBlogById(blogId);
        if (blog == null) return;
        blogService.addView(blogId);
        response.setHeader("Content-Disposition", "attachment;filename=" + blog.getBlogFile());
        response.setContentType("application/octet-stream;charset=UTF-8");
        OutputStream outputStream = response.getOutputStream();
        File file = new File(blogPath, blog.getUserId() +"/"+ blog.getBlogFile());
        if (!file.exists()) return ;
        InputStream inputStream = new FileInputStream(file);
        try {
            byte[] buf = new byte[1024];
            int bytesRead = 0;
            while ((bytesRead = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, bytesRead);
            }
        } finally {
            inputStream.close();
            outputStream.close();
        }
    }

    @PostMapping("getBlogList")
    @ResponseBody
    public Result<Blog[]> getBlogList(HttpServletRequest request, String id) {
        try {
            Blog[] blogs = blogService.findBlogByUser(id);
            return Result.success("返回博客成功",blogs);
        } catch (Exception e){
            return Result.fail(e.getMessage());
        }
    }

    @GetMapping("changeLikes")
    @ResponseBody
    public Result<String> changeLikes(String blogId, boolean add) {
        blogService.changeLike(blogId, add);
        return Result.success();
    }

    @GetMapping("getImage")
    public void getImage(HttpServletRequest request,  HttpServletResponse response) throws IOException {
        String type = stringRedisTemplate.opsForValue().get(OtherUtils.getIpAddr(request));
        if (type == null) {
            type = styles[random.nextInt(styles.length)];
            stringRedisTemplate.opsForValue().set(OtherUtils.getIpAddr(request), type, 10 , TimeUnit.SECONDS);
        }
        File file = new File(blogBoxPath + type);
        File [] files = file.listFiles();
        assert files != null;
        int i = random.nextInt(files.length);
        File img = files[i];
        BufferedImage image = ImageIO.read(img);
        response.setContentType("image/jpeg");
        ImageIO.write(image,"jpg",response.getOutputStream());
    }


    @GetMapping("search")
    @ResponseBody
    public Result<List<EsBlog>> search(Integer userId, String keyword) {
        if (userId == null)
            userId = 0;
        if (keyword == null)
            keyword = "";
        log.info("用户搜索中" + userId + keyword);
        try {
            List<EsBlog> searchResult = blogService.getSearchResult(userId, keyword);
            return Result.success(searchResult);
        } catch (IOException e) {
            return Result.fail(e.getMessage());
        }
    }

    @Autowired
    BlogRepository blogRepository;

    @GetMapping("deleteAll")
    @ResponseBody
    public Result<String> deleteAll() {
        log.info("deleteAll");
        try {
            Iterable<EsBlog> all =blogRepository.findAll();
            if (all.iterator().hasNext()) {
                blogRepository.deleteAll();
            }
            return Result.success();
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }


    @Value("${user.blog-path}")
    private String userBlogPath;

    @GetMapping("saveAll")
    @ResponseBody
    public Result<String> saveAll() {
        log.info("saveAll");
        try {
            Blog[] blogs = blogService.findAll();
            List<EsBlog> esBlogs = new ArrayList<>();
            for (Blog blog : blogs) {
                String path = userBlogPath.concat(blog.getUserId()+"/"+ blog.getBlogFile());
                File file = new File(path);
                EsBlog esBlog = new EsBlog();
                esBlog.setContent(txt2String(file));
                esBlog.setUserId(Integer.parseInt(blog.getUserId()));
                esBlog.setTags(blog.getTags().toString());
                esBlog.setTitle(blog.getTitle());
                esBlog.setPreview(blog.getPreview());
                esBlog.setBlogId(blog.getBlogId());
                esBlogs.add(esBlog);
            }
            blogRepository.saveAll(esBlogs);
            return Result.success();
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    @Scheduled(cron = "0 0 0/12 1/1 * ? *")
    public void reset() {
        deleteAll();
        saveAll();
    }

    public static String txt2String(File file){
        StringBuilder result = new StringBuilder();
        String newline = "<br>";
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
}
