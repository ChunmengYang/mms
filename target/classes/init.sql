/*
 Navicat Premium Data Transfer

 Source Server         : mmsmysql
 Source Server Type    : MySQL
 Source Server Version : 50725
 Source Host           : localhost:3306
 Source Schema         : mms

 Target Server Type    : MySQL
 Target Server Version : 50725
 File Encoding         : 65001

 Date: 08/03/2019 11:04:25
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for account
-- ----------------------------
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '账户ID，自增长',
  `login_name` varchar(32) NOT NULL DEFAULT '' COMMENT '登录名',
  `password` varchar(32) NOT NULL DEFAULT '' COMMENT '密码',
  `phone` varchar(32) NOT NULL DEFAULT '' COMMENT '手机号',
  `email` varchar(32) DEFAULT '' COMMENT '邮箱',
  `status` int(2) unsigned NOT NULL DEFAULT '1' COMMENT '1：正常；2：不可用',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID，自增长',
  `account_id` int(11) NOT NULL,
  `user_name` varchar(128) NOT NULL DEFAULT '' COMMENT '用户姓名',
  `nick_name` varchar(128) DEFAULT '' COMMENT '用户昵称',
  `icon_path` varchar(128) DEFAULT '' COMMENT '头像路径',
  `sex` int(2) unsigned  DEFAULT '0' COMMENT '0：未知； 1：男；2：女',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;

ALTER TABLE `mms`.`user` ADD UNIQUE INDEX `idx_user_acount_id`(`account_id`);


-- ----------------------------
-- Table structure for session
-- ----------------------------
DROP TABLE IF EXISTS `session`;
CREATE TABLE `session` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID，自增长',
  `account_id` int(11) NOT NULL COMMENT '账号ID',
  `sign` varchar(32) NOT NULL COMMENT '签名',
  `expired_time` timestamp NOT NULL COMMENT '过期时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;

ALTER TABLE `mms`.`session` ADD UNIQUE INDEX `idx_session_acount_id`(`account_id`);
ALTER TABLE `mms`.`session` ADD UNIQUE INDEX `idx_session_sign`(`sign`);