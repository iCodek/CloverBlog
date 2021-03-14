package com.clover.blog.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Result<T> {
    private Boolean state = true;
    private String msg;
    private T data;
    private long timestamp = System.currentTimeMillis();

    public static <T> Result<T> success() {
        return new Result<T>().setMsg("操作成功");
    }

    public static <T> Result<T> success(String msg) {
        return new Result<T>().setMsg(msg);
    }

    public static <T> Result<T> success(T data) {
        return new Result<T>().setData(data);
    }

    public static <T> Result<T> success(String msg,T data) {
        return new Result<T>().setMsg(msg).setData(data);
    }

    public static <T> Result<T> success(T data, String msg) {
        return new Result<T>().setMsg(msg).setData(data);
    }

    public static <T> Result<T> fail() {
        return new Result<T>().setMsg("操作失败").setState(false);
    }

    public static <T> Result<T> fail(String msg) {
        return new Result<T>().setMsg(msg).setState(false);
    }

}
