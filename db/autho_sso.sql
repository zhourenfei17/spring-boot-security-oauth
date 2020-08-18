/*
Navicat MySQL Data Transfer

Source Server         : away
Source Server Version : 50729
Source Host           : 47.98.33.15:3306
Source Database       : autho_sso

Target Server Type    : MYSQL
Target Server Version : 50729
File Encoding         : 65001

Date: 2020-08-18 17:51:07
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for oauth_client_details
-- ----------------------------
DROP TABLE IF EXISTS `oauth_client_details`;
CREATE TABLE `oauth_client_details` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `app_name` varchar(30) DEFAULT NULL COMMENT '应用名称',
  `app_key` varchar(60) DEFAULT NULL COMMENT '应用key',
  `app_secret` varchar(60) DEFAULT NULL COMMENT '应用密钥',
  `resource_ids` varchar(255) DEFAULT NULL COMMENT '资源id集合',
  `scope` varchar(255) DEFAULT NULL COMMENT 'oauth权限范围',
  `authorized_grant_types` varchar(255) DEFAULT NULL COMMENT '支持的授权类型',
  `redirect_uri` varchar(255) DEFAULT NULL COMMENT '重定向uri',
  `authorities` varchar(255) DEFAULT NULL COMMENT 'security权限值',
  `access_token_validity` int(10) DEFAULT NULL COMMENT 'access_token有效时间',
  `refresh_token_validity` int(10) DEFAULT NULL COMMENT 'refresh_token有效时间',
  `additional_information` text COMMENT '客户端的其他信息，必须为json格式',
  `archived` int(3) DEFAULT NULL COMMENT '是否已存档',
  `trusted` int(3) DEFAULT NULL COMMENT '是否是信任的,0:不受信任，1：信任的',
  `autoapprove` varchar(20) DEFAULT NULL COMMENT '是否自动跳过approve操作，默认为false，取值范围：true,false,read,write',
  `create_by` varchar(60) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(60) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of oauth_client_details
-- ----------------------------
INSERT INTO `oauth_client_details` VALUES ('1293380307897106433', '微信', '1293380307393789953', '$2a$10$IYtMMj4Rc0TIX6Ty4wy/POYIydxZt.fH/p8.rWWv.pPKyIw7tFt9i', 'USER-RESOURCE', 'read,write', 'authorization_code,password,refresh_token', 'http://localhost:8090/index', 'admin', null, null, null, null, '0', null, null, '2020-08-12 10:54:30', null, '2020-08-12 10:54:30');

-- ----------------------------
-- Table structure for oauth_role
-- ----------------------------
DROP TABLE IF EXISTS `oauth_role`;
CREATE TABLE `oauth_role` (
  `id` bigint(20) NOT NULL,
  `role_code` varchar(20) DEFAULT NULL COMMENT '角色编码',
  `role_name` varchar(20) DEFAULT NULL COMMENT '角色名称',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of oauth_role
-- ----------------------------
INSERT INTO `oauth_role` VALUES ('1293177755318456322', 'admin', '管理员', null, '2020-08-11 21:29:38', null, '2020-08-11 21:29:38');
INSERT INTO `oauth_role` VALUES ('1293356939856674817', 'visitor', '访客', null, '2020-08-12 09:21:39', null, '2020-08-12 09:21:39');

-- ----------------------------
-- Table structure for oauth_user
-- ----------------------------
DROP TABLE IF EXISTS `oauth_user`;
CREATE TABLE `oauth_user` (
  `id` bigint(20) NOT NULL,
  `account` varchar(30) DEFAULT NULL COMMENT '登录帐号',
  `user_name` varchar(20) DEFAULT NULL COMMENT '用户姓名',
  `password` varchar(100) DEFAULT NULL COMMENT '用户密码',
  `phone` varchar(11) DEFAULT NULL COMMENT '手机号',
  `gender` tinyint(1) DEFAULT NULL COMMENT '性别，1：男，2：女',
  `status` tinyint(1) DEFAULT NULL COMMENT '状态，0：正常，1：冻结',
  `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除标识，0：正常，1：已删除',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of oauth_user
-- ----------------------------
INSERT INTO `oauth_user` VALUES ('1293177353705459713', 'admin', '管理员', '$2a$10$zctpRXk8AMnvnQuUMbbQeOvz3NHYU7Fg3s4f1m30DmYxSvkwy2WBW', '13866666666', '1', '0', '0', null, '2020-08-13 17:26:28', null, '2020-08-13 17:26:28');
INSERT INTO `oauth_user` VALUES ('1293356708595335170', 'test', '普通用户', '$2a$10$rTPd80G2ffBJekzJ5SylRecqBNtG9RvByMgDn5wUIkgMmlAKsCugC', '13866666666', '1', '0', '0', null, '2020-08-12 10:43:15', null, '2020-08-12 10:43:15');

-- ----------------------------
-- Table structure for oauth_user_role
-- ----------------------------
DROP TABLE IF EXISTS `oauth_user_role`;
CREATE TABLE `oauth_user_role` (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of oauth_user_role
-- ----------------------------
INSERT INTO `oauth_user_role` VALUES ('1293178327694151682', '1293177353705459713', '1293177755318456322');
INSERT INTO `oauth_user_role` VALUES ('1293357301908996097', '1293356708595335170', '1293356939856674817');
