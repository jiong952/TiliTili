/*
SQLyog Community v13.1.6 (64 bit)
MySQL - 8.0.23 
*********************************************************************
*/
/*!40101 SET NAMES utf8 */;

create table `event` (
	`event_id` int (11),
	`eventGroup_id` int (11),
	`publisher_id` int (11),
	`event_name` varchar (765),
	`event_content` text ,
	`comment_num` int (11),
	`likes_num` int (11),
	`collection_num` int (11),
	`event_status` tinyint (4),
	`create_time` timestamp ,
	`update_time` timestamp ,
	`accuse_status` tinyint (4)
); 
insert into `event` (`event_id`, `eventGroup_id`, `publisher_id`, `event_name`, `event_content`, `comment_num`, `likes_num`, `collection_num`, `event_status`, `create_time`, `update_time`, `accuse_status`) values('11','4','10','瓜瓜01','01','1','1','1','1','2021-04-11 14:58:46','2021-04-11 15:14:15','0');
insert into `event` (`event_id`, `eventGroup_id`, `publisher_id`, `event_name`, `event_content`, `comment_num`, `likes_num`, `collection_num`, `event_status`, `create_time`, `update_time`, `accuse_status`) values('13','5','10','运动01','搜索','0','0','0','1','2021-04-11 14:59:35','2021-04-11 15:03:07','0');
insert into `event` (`event_id`, `eventGroup_id`, `publisher_id`, `event_name`, `event_content`, `comment_num`, `likes_num`, `collection_num`, `event_status`, `create_time`, `update_time`, `accuse_status`) values('14','5','10','sss','sss','0','0','0','1','2021-04-11 14:59:42','2021-04-11 15:09:28','0');
