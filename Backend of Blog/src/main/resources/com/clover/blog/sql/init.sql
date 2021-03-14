create table blog_info
(
    id          int auto_increment
        primary key,
    title       varchar(255)                         not null,
    user_id     int                                  not null,
    preview     varchar(255)                         null,
    likes       int        default 0                 not null,
    comments    int        default 0                 not null,
    views       int        default 0                 not null,
    blog_file   varchar(255)                         null,
    category    varchar(20)                          not null,
    visible     tinyint(1) default 1                 null,
    tags        json                                 null,
    upload_time timestamp  default CURRENT_TIMESTAMP null
)
    comment '博客信息表';

create fulltext index fulltext_article
    on blog_info (title, preview);

create index user_index
    on blog_info (user_id);

create table comment
(
    id           int auto_increment
        primary key,
    parent_id    int         null,
    blog_id      int         not null,
    user_id      int         not null,
    content      text        not null,
    blog_user_id int         not null,
    by_user      varchar(10) not null
)
    comment '评论表';

create table user
(
    id            int auto_increment
        primary key,
    username      varchar(10)                                                        not null,
    password      varchar(20)                                                        not null,
    contact       json                                                               null,
    email         varchar(50)                                                        not null,
    avatar        varchar(255) default 'https://www.luckyclover.top/imgs/avatar.jpg' null,
    register_time timestamp    default CURRENT_TIMESTAMP                             not null on update CURRENT_TIMESTAMP,
    user_bg       varchar(255) default 'https://www.luckyclover.top/imgs/bg.jpg'     null,
    user_top      varchar(255) default 'https://www.luckyclover.top/imgs/head.jpg'   null,
    song          json                                                               null,
    comments      int          default 0                                             null,
    views         int          default 0                                             null,
    blog_count    int          default 0                                             null,
    notice        text                                                               null,
    api           varchar(255) default ''                                            null,
    constraint user_username_uindex
        unique (username)
)
    comment '用户信息表';

