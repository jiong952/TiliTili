/*
SQLyog Community v13.1.6 (64 bit)
MySQL - 8.0.23 
*********************************************************************
*/
/*!40101 SET NAMES utf8 */;

create table `comment` (
	`id` int (11),
	`event_id` int (11),
	`user_id` int (11),
	`comment_content` text ,
	`create_time` timestamp ,
	`update_time` timestamp 
); 
insert into `comment` (`id`, `event_id`, `user_id`, `comment_content`, `create_time`, `update_time`) values('30','7','2','新生代','2021-04-10 17:02:52','2021-04-10 17:02:52');
insert into `comment` (`id`, `event_id`, `user_id`, `comment_content`, `create_time`, `update_time`) values('32','7','2','我来测试','2021-04-11 14:12:17','2021-04-11 14:12:17');
insert into `comment` (`id`, `event_id`, `user_id`, `comment_content`, `create_time`, `update_time`) values('33','11','10','这是1111','2021-04-11 15:14:15','2021-04-11 15:14:15');
