/*
SQLyog Community v13.1.6 (64 bit)
MySQL - 8.0.23 
*********************************************************************
*/
/*!40101 SET NAMES utf8 */;

create table `accusation` (
	`id` int (11),
	`accused_event_id` int (11),
	`accuse_user_id` int (11),
	`accused_content` varchar (765),
	`create_time` timestamp ,
	`update_time` timestamp 
); 
insert into `accusation` (`id`, `accused_event_id`, `accuse_user_id`, `accused_content`, `create_time`, `update_time`) values('7','11','2','不行','2021-04-12 01:30:54','2021-04-12 01:30:54');
