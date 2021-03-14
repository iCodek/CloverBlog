package com.clover.blog.utils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class VerifyCode {
    //定义一些变量
    private int x = 200;
    private int y = 80;
    private int fontSize = 70;
    private StringBuilder sb = new StringBuilder();
    private Random random = new Random();
    private Color bgColor = new Color(255,255,255);
    private String[] fontsName = {"宋体", "华文楷体", "黑体", "华文新魏", "华文隶书", "微软雅黑", "楷体_GB2312"};
    private String codes = "23456789abcdefghjklmnopqrstuvwxyzABCDEFGHJKLMNOPQRSTUVWXYZ";
    //获取随机的字体
    private String getFont(){
        int index = random.nextInt(fontsName.length);
        String fontName = fontsName[index];
        return fontName;
    }
    //获取字母
    private String getChar(){
        int index = random.nextInt(codes.length());
        String ch = codes.charAt(index)+"";
        return ch;
    }
    //获取颜色
    private Color getColor(){
        int red = random.nextInt(150);
        int green = random.nextInt(150);
        int blue = random.nextInt(150);
        return new Color(red,green,blue);
    }
    //设置缓冲区
    private BufferedImage getBufferedImage(){
        BufferedImage bi = new BufferedImage(x,y,BufferedImage.TYPE_INT_RGB);
        Graphics2D pen = (Graphics2D)bi.getGraphics();
        pen.setColor(this.bgColor);
        pen.fillRect(0,0,x,y);
        return bi;
    }
    //给缓冲区添加字符串,添加干扰线
    private BufferedImage addCharAndLine(){
        BufferedImage bi = getBufferedImage();
        Graphics2D pen = (Graphics2D)bi.getGraphics();
        for(int i=0;i<4;i++){
            String font = getFont();
            int style = random.nextInt(4);
            pen.setColor(getColor());
            pen.setFont(new Font(font,style,fontSize));
            String s = getChar();
            sb.append(s);
            pen.drawString(s, 10+i*50, 65);
        }
        int lineNumber = 3;
        pen.setColor(Color.BLUE);
//		pen.setStroke(new BasicStroke(1.5F));
        pen.setStroke(new BasicStroke(1.5F,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER));
        for(int i=0;i<lineNumber;i++){
            int x1 = random.nextInt(x);
            int y1 = random.nextInt(y);
            int x2 = random.nextInt(x);
            int y2 = random.nextInt(y);
            pen.drawLine(x1, y1, x2, y2);
        }
        return bi;
    }
    //获取验证码的值
    public String getText(){
        return sb.toString();
    }
    //获取一张验证码图片
    public BufferedImage getImage(){
        return addCharAndLine();
    }

}