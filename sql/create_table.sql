-- auto-generated definition
create table user
(
    id           bigint auto_increment comment '主键id'
        primary key,
    userName     varchar(256)                       null comment '用户名',
    userAccount  varchar(256)                       null comment '账号',
    avatarUrl    varchar(1024)                      null comment '头像',
    gender       tinyint  default 0                 null comment '性别 ：0:表示男',
    userPassword varchar(512)                       null comment '密码',
    phone        varchar(128)                       null comment '手机号',
    email        varchar(512)                       null comment '邮箱',
    userStatus   int      default 0                 null comment '状态 0：表示正常',
    createTime   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP null comment '更新时间',
    isDelete     tinyint  default 0                 null comment '是否删除，0表示正常 1：表示删除',
    userRole     int      default 0                 null comment '用户权限 0:表示普通用户；1:表示管理员',
    planetCode   varchar(512)                       null comment '星球编码'
);

