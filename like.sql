/*
SQLyog Community v13.1.6 (64 bit)
MySQL - 8.0.23 
*********************************************************************
*/
/*!40101 SET NAMES utf8 */;

create table `like` (
	`id` int (11),
	`event_id` int (11),
	`user_id` int (11),
	`create_time` timestamp ,
	`update_time` timestamp 
); 
insert into `like` (`id`, `event_id`, `user_id`, `create_time`, `update_time`) values('1','7','1','2021-04-01 22:31:53','2021-04-01 22:31:53');
insert into `like` (`id`, `event_id`, `user_id`, `create_time`, `update_time`) values('3','8','1','2021-04-01 23:23:14','2021-04-01 23:23:14');
insert into `like` (`id`, `event_id`, `user_id`, `create_time`, `update_time`) values('8','7','2','2021-04-10 13:01:22','2021-04-10 13:01:22');
insert into `like` (`id`, `event_id`, `user_id`, `create_time`, `update_time`) values('9','12','10','2021-04-11 14:59:56','2021-04-11 14:59:56');
insert into `like` (`id`, `event_id`, `user_id`, `create_time`, `update_time`) values('10','11','10','2021-04-11 15:00:04','2021-04-11 15:00:04');
