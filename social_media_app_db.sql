Drop database if exists social_media_app_db;
create database social_media_app_db;
use social_media_app_db;

drop table if exists app_user;
create table app_user(
id INT auto_increment primary key ,
age INT,
name varchar(30),
email varchar(50) unique,
password varchar(25)
);

drop table if exists profile;

create table profile(
id INT auto_increment,
user_id INT,

bio Text,
image_path varchar(255),
foreign key(user_id) references app_user(id),
primary key(id,user_id)
);

drop table if exists friend_request;
create table friend_request(
id INT auto_increment primary key,
sender_id INT , 
receiver_id INT,
request_date Date,
foreign key(sender_id) references app_user(id),
foreign key(receiver_id) references app_user(id)
); 

drop table if exists friends;
create table friends (
user1_id INT,
user2_id INT,
friendship_date date,
foreign key(user1_id) references app_user(id) ,
foreign key(user2_id) references app_user(id),
primary key(user1_id,user2_id)
);

drop table if exists posts;
create table posts(
id INT auto_increment primary key,
user_id INT,
post_text Text,
post_image_path varchar(50),
post_date date,
privacy varchar(20),
foreign key(user_id) references app_user(id) 


);

drop table if exists likes;
create table likes(
post_id INT,
user_id INT,
foreign key(post_id) references posts(id),
foreign key(user_id) references app_user(id),
primary key(post_id,user_id)

);

drop table if exists comments;
create table comments(
id INT auto_increment primary key,
post_id INT,
user_id INT,
comment_text Text,
comment_date date,
foreign key(post_id) references posts(id),
foreign key(user_id) references app_user(id)
);

drop table if exists post_notifications;
create table post_notifications(
id INT auto_increment primary key,
post_id INT,
sender_id INT,
notification_text varchar(255),
notification_type varchar(10),
notification_date date,
foreign key(sender_id) references app_user(id) ,
foreign key(post_id) references posts(id)
);

drop table if exists app_user_post_notifications;
create table app_user_post_notifications(
receiver_id INT,
notification_id INT,
foreign key(receiver_id) references app_user(id) ,
foreign key(notification_id) references post_notifications(id),
primary key(receiver_id,notification_id)
);
