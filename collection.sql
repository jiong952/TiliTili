/*
SQLyog Community v13.1.6 (64 bit)
MySQL - 8.0.23 
*********************************************************************
*/
/*!40101 SET NAMES utf8 */;

create table `collection` (
	`id` int (11),
	`user_id` int (11),
	`event_id` int (11),
	`create_time` timestamp ,
	`update_time` timestamp 
); 
insert into `collection` (`id`, `user_id`, `event_id`, `create_time`, `update_time`) values('2','8','8','2021-04-01 23:47:47','2021-04-01 23:47:47');
insert into `collection` (`id`, `user_id`, `event_id`, `create_time`, `update_time`) values('3','8','7','2021-04-01 23:48:37','2021-04-01 23:48:37');
insert into `collection` (`id`, `user_id`, `event_id`, `create_time`, `update_time`) values('10','2','7','2021-04-10 13:00:25','2021-04-10 13:00:25');
insert into `collection` (`id`, `user_id`, `event_id`, `create_time`, `update_time`) values('11','10','12','2021-04-11 14:59:57','2021-04-11 14:59:57');
insert into `collection` (`id`, `user_id`, `event_id`, `create_time`, `update_time`) values('12','10','11','2021-04-11 15:00:06','2021-04-11 15:00:06');
