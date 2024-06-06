CREATE TABLE `user` (
                        `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                        `third_account_id` varchar(128) NOT NULL DEFAULT '' COMMENT '第三方用户ID',
                        `user_name` varchar(64) NOT NULL DEFAULT '' COMMENT '用户名',
                        `password` varchar(128) NOT NULL DEFAULT '' COMMENT '密码',
                        `login_type` tinyint NOT NULL DEFAULT '0' COMMENT '登录方式: 0-微信登录，1-账号密码登录',
                        `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
                        `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                        `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
                        PRIMARY KEY (`id`),
                        KEY `key_third_account_id` (`third_account_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户登录表';


CREATE TABLE `user_info` (
                             `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                             `user_id` bigint unsigned NOT NULL DEFAULT '0' COMMENT '用户id',
                             `user_name` varchar(50) NOT NULL DEFAULT '' COMMENT '用户名',
                             `photo` varchar(128) NOT NULL DEFAULT '' COMMENT '用户图像',
                             `position` varchar(50) NOT NULL DEFAULT '' COMMENT '职位',
                             `company` varchar(50) NOT NULL DEFAULT '' COMMENT '公司',
                             `profile` varchar(225) NOT NULL DEFAULT '' COMMENT '个人简介',
                             `user_role` int NOT NULL DEFAULT '0' COMMENT '0 普通用户 1 超管',
                             `extend` varchar(1024) NOT NULL DEFAULT '' COMMENT '扩展字段',
                             `ip` json NOT NULL COMMENT '用户的ip信息',
                             `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
                             `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                             `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
                             PRIMARY KEY (`id`),
                             KEY `key_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户个人信息表';

CREATE TABLE `user_foot` (
                             `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                             `user_id` bigint unsigned NOT NULL DEFAULT '0' COMMENT '用户id',
                             `document_id` bigint unsigned NOT NULL DEFAULT '0' COMMENT '文档ID（文章/评论）',
                             `document_type` tinyint NOT NULL DEFAULT '1' COMMENT '文档类型：1-文章，2-评论',
                             `document_user_id` bigint unsigned NOT NULL DEFAULT '0' COMMENT '发布该文档的用户ID',
                             `collection_stat` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '收藏状态: 0-未收藏，1-已收藏，2-取消收藏',
                             `read_stat` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '阅读状态: 0-未读，1-已读',
                             `comment_stat` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '评论状态: 0-未评论，1-已评论，2-删除评论',
                             `praise_stat` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '点赞状态: 0-未点赞，1-已点赞，2-取消点赞',
                             `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                             `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
                             PRIMARY KEY (`id`),
                             UNIQUE KEY `idx_user_doucument` (`user_id`,`document_id`,`document_type`),
                             KEY `idx_doucument_id` (`document_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户足迹表';

CREATE TABLE `user_relation` (
                                 `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                 `user_id` bigint unsigned NOT NULL DEFAULT '0' COMMENT '用户id',
                                 `follow_user_id` bigint unsigned NOT NULL DEFAULT '0' COMMENT '用户id',
                                 `follow_state` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '阅读状态: 0-未关注，1-已关注，2-取消关注',
                                 `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
                                 PRIMARY KEY (`id`),
                                 UNIQUE KEY `uk_user_follow` (`user_id`,`follow_user_id`),
                                 KEY `key_follow_user_id` (`follow_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户关系表';



CREATE TABLE `article` (
                           `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                           `user_id` bigint unsigned NOT NULL DEFAULT '0' COMMENT '作者id',
                           `title` varchar(120) NOT NULL DEFAULT '' COMMENT '帖子标题',
                           `picture` varchar(128) NOT NULL DEFAULT '' COMMENT '帖子头图',
                           `category_id` int unsigned NOT NULL DEFAULT '0' COMMENT '类目ID',
                           `content` longtext COMMENT '文章内容',
                           `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
                           `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                           `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
                           PRIMARY KEY (`id`),
                           KEY `idx_category_id` (`category_id`),
                           KEY `idx_title` (`title`)
) ENGINE=InnoDB AUTO_INCREMENT=103 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='帖子表';

CREATE TABLE `category` (
                            `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                            `category_name` varchar(64) NOT NULL DEFAULT '' COMMENT '类目名称',
                            `rank` tinyint NOT NULL DEFAULT '0' COMMENT '排序',
                            `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
                            `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='类目管理表';


CREATE TABLE `comment` (
                           `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                           `article_id` bigint unsigned NOT NULL DEFAULT '0' COMMENT '文章ID',
                           `user_id` bigint unsigned NOT NULL DEFAULT '0' COMMENT '用户id',
                           `content` varchar(300) NOT NULL DEFAULT '' COMMENT '评论内容',
                           `top_comment_id` int NOT NULL DEFAULT '0' COMMENT '顶级评论ID',
                           `parent_comment_id` int unsigned NOT NULL DEFAULT '0' COMMENT '父评论ID',
                           `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
                           `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                           `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
                           PRIMARY KEY (`id`),
                           KEY `idx_article_id` (`article_id`),
                           KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='评论表';





