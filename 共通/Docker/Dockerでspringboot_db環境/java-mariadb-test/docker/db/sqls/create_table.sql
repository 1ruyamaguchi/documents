CREATE DATABASE docker_java_db_test;

USE docker_java_db_test;

CREATE TABLE user(
    id int primary key auto_increment,
    user_name varchar(15) not null,
    detail text
);