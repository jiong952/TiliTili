/*
SQLyog Community v13.1.6 (64 bit)
MySQL - 8.0.23 
*********************************************************************
*/
/*!40101 SET NAMES utf8 */;

create table `role` (
	`role_id` int (11),
	`role_name` varchar (765),
	`create_time` timestamp ,
	`update_time` timestamp 
); 
insert into `role` (`role_id`, `role_name`, `create_time`, `update_time`) values('1','吃瓜群众','2021-03-29 13:14:40','2021-03-29 13:15:24');
insert into `role` (`role_id`, `role_name`, `create_time`, `update_time`) values('2','管理员','2021-03-29 13:15:32','2021-03-29 13:15:32');
insert into `role` (`role_id`, `role_name`, `create_time`, `update_time`) values('3','游客','2021-03-29 13:15:35','2021-03-29 13:15:35');
insert into `role` (`role_id`, `role_name`, `create_time`, `update_time`) values('4','超管','2021-03-29 13:15:44','2021-03-29 13:15:44');
