SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for app_info
-- ----------------------------
DROP TABLE IF EXISTS `app`;
CREATE TABLE `app` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `app_name` varchar(255) DEFAULT NULL,
  `secret` varchar(255) DEFAULT NULL,
  `current_server` varchar(255) DEFAULT NULL,
  `create_time` datetime(6) DEFAULT NULL,
  `modified_time` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `appNameUK` (`app_name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

INSERT INTO `app` (`app_name`, `create_time`, `modified_time`, `secret`) VALUES ("wjg@test", now(), now(), "123");
