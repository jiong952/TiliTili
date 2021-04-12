/*
SQLyog Community v13.1.6 (64 bit)
MySQL - 8.0.23 
*********************************************************************
*/
/*!40101 SET NAMES utf8 */;

create table `eventgroup` (
	`eventGroup_id` int (11),
	`eventGroup_name` varchar (765),
	`eventGroup_description` varchar (765),
	`eventGroup_status` tinyint (4),
	`create_time` timestamp ,
	`update_time` timestamp 
); 
insert into `eventgroup` (`eventGroup_id`, `eventGroup_name`, `eventGroup_description`, `eventGroup_status`, `create_time`, `update_time`) values('4','唱歌','啊啊啊啊','1','2021-04-10 22:30:05','2021-04-10 22:30:05');
insert into `eventgroup` (`eventGroup_id`, `eventGroup_name`, `eventGroup_description`, `eventGroup_status`, `create_time`, `update_time`) values('5','运动','四大行扣回去的开会撒娇客户端健康','1','2021-04-10 23:08:42','2021-04-10 23:08:42');
