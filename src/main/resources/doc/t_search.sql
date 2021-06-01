/*
 Navicat Premium Data Transfer

 Source Server         : 阿里云MySQL
 Source Server Type    : MySQL
 Source Server Version : 50733
 Source Schema         : douban

 Target Server Type    : MySQL
 Target Server Version : 50733
 File Encoding         : 65001

 Date: 01/06/2021 14:51:01
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_search
-- ----------------------------
DROP TABLE IF EXISTS `t_search`;
CREATE TABLE `t_search` (
  `id` int(11) NOT NULL,
  `scope` varchar(255) NOT NULL DEFAULT '1' COMMENT '最近几小时',
  `keywordsList` varchar(255) NOT NULL DEFAULT '',
  `blackWordsList` varchar(255) NOT NULL DEFAULT '',
  `receiver` varchar(255) NOT NULL DEFAULT '',
  `status` tinyint(255) NOT NULL DEFAULT '1' COMMENT '是否启用',
  `updateTime` datetime(3) NOT NULL ON UPDATE CURRENT_TIMESTAMP(3),
  `createTime` datetime(3) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_search
-- ----------------------------
BEGIN;
INSERT INTO `t_search` VALUES (1, '1', '海湾明珠,圣淘沙骏园,香缇湾花园,泰华阳光海,金港华庭,高树围,西乡,轻铁西', '固戍', 'xxx@qq.com', 1, '2021-06-01 14:49:25.973', '2019-12-04 15:32:00.000');
INSERT INTO `t_search` VALUES (2, '1', '海湾明珠,圣淘沙骏园,香缇湾花园,泰华阳光海,金港华庭,高树围,西乡', '固戍', 'xxx@qq.com', 1, '2021-06-01 14:49:38.133', '2021-06-01 12:21:00.000');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
