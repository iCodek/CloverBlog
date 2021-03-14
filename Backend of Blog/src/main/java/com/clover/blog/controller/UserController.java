package com.clover.blog.controller;

import ch.qos.logback.classic.spi.EventArgUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.clover.blog.entity.Blog;
import com.clover.blog.entity.Result;
import com.clover.blog.entity.User;
import com.clover.blog.service.BlogService;
import com.clover.blog.service.UserService;
import com.clover.blog.utils.JwtUtils;
import com.clover.blog.utils.VerifyCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;

@Controller
@RequestMapping("user")
@CrossOrigin
@Slf4j
public class UserController {
    private Random random = new Random();
    @Value("${user.img-path}")
    private String userImgPath;
    @Value("${web.url}"+"imgs/")
    private String userImgUrl;
    @Value("${register.img-path}")
    private String registerImgUrl;
    @Value("${user.blog-path}")
    private String userBlogPath;
    @Value("${py.path}")
    private String pyPath;
    @Value("${web.url}"+"blogs/")
    private String blogImgPth;

    private final BlogService blogService;
    private final UserService userService;

    public UserController(UserService userService, BlogService blogService) {
        this.userService = userService;
        this.blogService = blogService;
    }

    /**
     * 用户注册
     * @param code
     * @param user
     * @return
     */
    @PostMapping("register")
    @ResponseBody
    public Result<User> register(String code, String key, @RequestBody User user, HttpServletRequest request){
        log.info("接收到的验证码为:"+code);
        log.info("注册的User为:"+user);
        String keyCode = (String)request.getServletContext().getAttribute(key);
        try {
            if(code.equalsIgnoreCase(keyCode)){
                JSONObject js = new JSONObject();
                js.put("email",user.getEmail());
                user.setContact(js);
                userService.register(user);
                log.info("新增用户:"+user);
                return Result.success("注册成功，您的用户ID为" + user.getId());
            } else {
                return Result.fail("验证码错误!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(e.getMessage());
        }
    }

    @PostMapping("login")
    @ResponseBody
    public Result<User> login(@RequestBody User user, HttpServletRequest request){
        log.info("用户登录："+user);
        try {
            User userDB = userService.login(user);
            userDB.setPassword("");
            Map<String,String> payload = new HashMap<>();
            payload.put("id",userDB.getId());
            payload.put("name",userDB.getUserName());
            String token = JwtUtils.getToken(payload);
            return Result.success(token,userDB);
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 自动登录
     * @param request
     * @return
     */
    @PostMapping("saveLogin")
    @ResponseBody
    public Result<User> saveLogin(HttpServletRequest request){
        String token = request.getHeader("Token");
        DecodedJWT verify = JwtUtils.verify(token);
        String id = verify.getClaim("id").asString();
        log.info("用户"+id+"自动登录");
        try {
            User userDB = userService.findUserById(id);
            userDB.setPassword("");
            return Result.success("自动登录",userDB);
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 背景图
     * @param response
     * @throws IOException
     */
    @GetMapping("getBg")
    public void getBg(HttpServletResponse response) throws IOException {
//        int i = random.nextInt(20);
//        String path = i + ".jpg";
        //ClassPathResource classPathResource = new ClassPathResource("images.txt");
        //ClassPathResource classPathResource = new ClassPathResource("static/background/".concat(path));
        //InputStream inputStream =classPathResource.getInputStream();
//        BufferedImage image = ImageIO.read(inputStream);

        File file = new File(registerImgUrl);
        File [] files = file.listFiles();
        assert files != null;
        int i = random.nextInt(files.length);
        File img = files[i];
        BufferedImage image = ImageIO.read(img);
        response.setContentType("image/jpeg");
        ImageIO.write(image,"jpg",response.getOutputStream());
    }

    @GetMapping("getImage")
    @ResponseBody
    public Map<String,String> getImage(HttpServletRequest request) throws IOException {
        Map<String,String> result =new HashMap<>();
        //获取验证码
        VerifyCode verifyCode = new VerifyCode();
        //生成图片
        BufferedImage image = verifyCode.getImage();
        //验证码存入ServletContext
        String key = String.valueOf(System.currentTimeMillis());
        request.getServletContext().setAttribute(key,verifyCode.getText());
        //base64
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(image,"jpg",bos);
        String img = Base64Utils.encodeToString(bos.toByteArray());
        result.put("key",key);
        result.put("image",img);
        return result;
    }

    @PostMapping("saveAvatar")
    @ResponseBody
    public Result<User> saveAvatar(String id, MultipartFile file) {
        if(!userService.findId(id)) return Result.fail("用户不存在");
        String path = "avatar_of_user" + id + "_at_" + System.currentTimeMillis() + ".png";
        File localFile = new File(userImgPath , path);
        log.info("用户"+id+"保存头像中"+path);
        try {
            file.transferTo(localFile);
            if(userService.saveAvatar(id, userImgUrl + path) == 0)
                return Result.fail("保存头像失败");
            else {
                User userDB = userService.findUserById(id);
                return Result.success("保存头像成功", userDB);
            }
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    @PostMapping("saveBg")
    @ResponseBody
    public Result<User> saveBg(String id, MultipartFile file) {
        if(!userService.findId(id)) return Result.fail("用户不存在");
        String path = "bg_of_user" + id + "_at_" + System.currentTimeMillis() + ".jpg";
        File localFile = new File(userImgPath , path);
        log.info("用户"+id+"保存博客背景中"+path);
        try {
            file.transferTo(localFile);
            if(userService.saveBg(id, userImgUrl + path) == 0)
                return Result.fail("保存背景失败");
            else {
                User userDB = userService.findUserById(id);
                return Result.success("保存背景成功", userDB);
            }
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    @PostMapping("saveTop")
    @ResponseBody
    public Result<User> saveTop(String id, MultipartFile file) {
        if(!userService.findId(id)) return Result.fail("用户不存在");
        String path = "top_of_user" + id + "_at_" + System.currentTimeMillis() + ".jpg";
        File localFile = new File(userImgPath , path);
        log.info("用户"+id+"保存顶部背景中"+path);
        try {
            file.transferTo(localFile);
            if(userService.saveTop(id, userImgUrl + path) == 0)
                return Result.fail("保存顶部背景失败");
            else {
                User userDB = userService.findUserById(id);
                return Result.success("保存顶部背景成功", userDB);
            }
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    @PostMapping("saveContact")
    @ResponseBody
    public Result<User> saveContact(String id, String name, @RequestBody String json) {
        if(!userService.findId(id)) return Result.fail("用户不存在");
        JSONObject js = JSONObject.parseObject(json);
        log.info("用户"+id+"保存基本信息中:"+json);
        try {
            if(userService.saveContact(id, js) == 0)
                return Result.fail("保存联系方式失败");
            User userDB = null;
            if(userService.changeUserName(id, name) == 0){
                userDB = userService.findUserById(id);
                return Result.success("未修改昵称，保存联系方式成功", userDB);
            }
            userDB = userService.findUserById(id);
            return Result.success("修改昵称，保存基本信息成功", userDB);
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    @PostMapping("saveSong")
    @ResponseBody
    public Result<User> saveSong(String id, @RequestBody String song,HttpServletRequest request) {
        String token = request.getHeader("Token");
        DecodedJWT verify = JwtUtils.verify(token);
        String loginId = verify.getClaim("id").asString();
        if(!loginId.equals(id)) return Result.fail("非登录用户请求");
        if(!userService.findId(id)) return Result.fail("用户不存在");
        List<String> list = JSONObject.parseArray(song).toJavaList(String.class);
        log.info("用户"+id+"保存歌曲中:"+list);
        try {
            if(userService.saveSong(id, list) == 0)
                return Result.fail("保存歌曲失败");
            else {
                User userDB = userService.findUserById(id);
                return Result.success("保存歌曲成功", userDB);
            }
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    @PostMapping("saveNotice")
    @ResponseBody
    public Result<User> saveNotice(String id, @RequestBody User user, HttpServletRequest request) {
        String token = request.getHeader("Token");
        DecodedJWT verify = JwtUtils.verify(token);
        String tokenId = verify.getClaim("id").asString();
        if(!id.equals(tokenId))
            return Result.fail("干嘛呢！");
        if(!userService.findId(id)) return Result.fail("用户不存在");
        String notice = user.getNotice();
        if (notice.length() > 800)
            notice = notice.substring(0, 800);
        log.info("用户"+id+"保存公告中:"+notice);
        try {
            if(userService.saveNotice(id, notice) == 0)
                return Result.fail("保存公告失败");
            else {
                User userDB = userService.findUserById(id);
                return Result.success("保存公告成功", userDB);
            }
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    @PostMapping("saveApi")
    @ResponseBody
    public Result<User> saveApi(String id, @RequestBody User user) {
        if(!userService.findId(id)) return Result.fail("用户不存在");
        String api = user.getApi();
        log.info("用户"+id+"保存API中:"+api);
        try {
            if(userService.saveApi(id, api) == 0)
                return Result.fail("保存API失败");
            else {
                User userDB = userService.findUserById(id);
                return Result.success("保存API成功", userDB);
            }
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    @PostMapping("saveBlog")
    @ResponseBody
    public Result<Blog[]> saveBlog(MultipartFile[] mds, MultipartFile[] imgs, @RequestPart("blog") Blog blog ) throws IOException {
        int blogCount = 0;
        int imgCount = 0;
        log.info("用户"+blog.getUserId()+"保存博客");
        for(MultipartFile md : mds) {
            String path = userBlogPath.concat(blog.getUserId()+"/"+(Objects.requireNonNull(md.getOriginalFilename())));
            File file = new File(path);
            if(!file.exists()) {
                if(file.mkdirs()) {
                    md.transferTo(file);
                    blogCount ++;
                } else throw new RuntimeException("文件创建失败");
            } else {
                if(blog.getLikes()>0){
                    md.transferTo(file);
                }
                else return Result.fail("相同博客文件在服务器已存在,是否覆盖");
            }
            log.info("用户"+blog.getUserId()+"保存博客文章"+md.getOriginalFilename());
            blog.setBlogFile(md.getOriginalFilename());
            String[] arguments = new String[] {"python", pyPath+"src.py",path,blogImgPth+blog.getUserId()+"/imgs"};
            Process process = Runtime.getRuntime().exec(arguments);
        }
        if (blog.getLikes() == 0) {
            blogService.addBlog(blog);
            userService.addBlogCount(blog.getUserId());
        }
        Blog[] blogs = blogService.findBlogByUser(blog.getUserId());
        log.info("保存"+imgs.length+"张图片");
        for(MultipartFile img : imgs) {
            File file = new File(userBlogPath, blog.getUserId()+"/imgs/"+(Objects.requireNonNull(img.getOriginalFilename())));
            if(!file.exists()) {
                if (file.mkdirs()) {
                    img.transferTo(file);
                    imgCount++;
                } else throw new RuntimeException("图片保存失败");
            }
        }
        if (blogCount == 0)  return Result.success("覆盖博客成功", blogs);
        if (blogCount + imgCount > 0) {
            return Result.success("上传"+blogCount+"份博客，"+imgCount+"张图片成功", blogs);
        } else {
            return Result.fail("上传文件为空");
        }
    }


    @PostMapping("saveFile")
    @ResponseBody
    public Result<Map<String,String>> saveFile(MultipartFile[] imgs, @RequestPart("blog") Blog blog) throws IOException {
        String id = blog.getUserId();
        int fileCOunt = 0;
        log.info("保存"+imgs.length+"个文件中");
        Map<String,String> map = new HashMap<>();
        for(MultipartFile f : imgs) {
            map.put(f.getOriginalFilename(),blogImgPth+blog.getUserId()+"/imgs/"+f.getOriginalFilename());
            File file = new File(userBlogPath, id+"/imgs/"+(Objects.requireNonNull(f.getOriginalFilename())));
            if(!file.exists()) {
                if (file.mkdirs()) {
                    f.transferTo(file);
                    fileCOunt++;
                } else throw new RuntimeException("文件保存失败");
            }
        }
        if (fileCOunt > 0) {
            return Result.success("上传"+fileCOunt+"份文件成功", map);
        } else {
            return Result.fail("文件已存在");
        }
    }


    @GetMapping("saveVisible")
    @ResponseBody
    public Result<String> saveVisible(String blogId, boolean visible) {
        if (blogService.changeBlogVisible(blogId, visible) == 1) return Result.success("保存成功");
        return Result.success("保存失败");
    }

}
