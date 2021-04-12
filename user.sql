/*
SQLyog Community v13.1.6 (64 bit)
MySQL - 8.0.23 
*********************************************************************
*/
/*!40101 SET NAMES utf8 */;

create table `user` (
	`user_id` int (11),
	`login_name` varchar (765),
	`login_password` varchar (765),
	`password_remember` tinyint (4),
	`user_e-mail` varchar (765),
	`icon_address` varchar (765),
	`user_nickname` varchar (765),
	`user_gender` tinyint (4),
	`user_birthday` date ,
	`user_description` varchar (765),
	`create_time` timestamp ,
	`update_time` timestamp 
); 
insert into `user` (`user_id`, `login_name`, `login_password`, `password_remember`, `user_e-mail`, `icon_address`, `user_nickname`, `user_gender`, `user_birthday`, `user_description`, `create_time`, `update_time`) values('1','sss','0ab44bd43d6b18fcd5cd928d6faab1b8','0',NULL,NULL,NULL,NULL,NULL,NULL,'2021-03-28 18:22:10','2021-04-12 22:26:37');
insert into `user` (`user_id`, `login_name`, `login_password`, `password_remember`, `user_e-mail`, `icon_address`, `user_nickname`, `user_gender`, `user_birthday`, `user_description`, `create_time`, `update_time`) values('2','zjh','0ab44bd43d6b18fcd5cd928d6faab1b8','0','1017328759@qq.com',NULL,NULL,'0','2015-06-14','sadasd\ndasdas\n\ndsa\n','2021-03-29 16:01:33','2021-04-13 00:00:16');
insert into `user` (`user_id`, `login_name`, `login_password`, `password_remember`, `user_e-mail`, `icon_address`, `user_nickname`, `user_gender`, `user_birthday`, `user_description`, `create_time`, `update_time`) values('4','zjhl','0ab44bd43d6b18fcd5cd928d6faab1b8','0',NULL,NULL,NULL,NULL,NULL,NULL,'2021-03-29 16:12:22','2021-04-12 22:24:35');
insert into `user` (`user_id`, `login_name`, `login_password`, `password_remember`, `user_e-mail`, `icon_address`, `user_nickname`, `user_gender`, `user_birthday`, `user_description`, `create_time`, `update_time`) values('5','ppp','0ab44bd43d6b18fcd5cd928d6faab1b8','0',NULL,NULL,'ppp',NULL,NULL,NULL,'2021-03-30 00:17:03','2021-04-12 22:24:33');
insert into `user` (`user_id`, `login_name`, `login_password`, `password_remember`, `user_e-mail`, `icon_address`, `user_nickname`, `user_gender`, `user_birthday`, `user_description`, `create_time`, `update_time`) values('8','ooo','0ab44bd43d6b18fcd5cd928d6faab1b8','0',NULL,NULL,'ooo','2',NULL,NULL,'2021-03-30 22:48:55','2021-04-12 22:24:31');
insert into `user` (`user_id`, `login_name`, `login_password`, `password_remember`, `user_e-mail`, `icon_address`, `user_nickname`, `user_gender`, `user_birthday`, `user_description`, `create_time`, `update_time`) values('10','admin','0ab44bd43d6b18fcd5cd928d6faab1b8','0',NULL,NULL,NULL,'2',NULL,NULL,'2021-03-30 23:34:07','2021-04-12 22:24:30');
insert into `user` (`user_id`, `login_name`, `login_password`, `password_remember`, `user_e-mail`, `icon_address`, `user_nickname`, `user_gender`, `user_birthday`, `user_description`, `create_time`, `update_time`) values('18','一样一样','0ab44bd43d6b18fcd5cd928d6faab1b8','0',NULL,NULL,'一样一样','2',NULL,NULL,'2021-04-12 18:04:20','2021-04-12 22:23:39');
