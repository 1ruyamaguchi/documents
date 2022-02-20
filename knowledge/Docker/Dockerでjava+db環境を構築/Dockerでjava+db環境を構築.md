# Dockerでjava+db環境を構築

## 目標
DockerでJavaアプリケーション実行環境およびDBサーバを立てて連携させる

## 使用環境
- Java: openjdk-17
- DB: MariaDB

## プロジェクト構成
```
java-mariadb-test
　├─docker
　│　├─db
　│　│　├─sqls
　│　│　│　└─create_table.sql
　│　│　└─Dockerfile
　│　└─java
　│　 　├─dockertest
　│　 　│　└─アプリケーションソースファイル（省略）
　│　 　└─Dockerfile
　└─docker-compose.yml
```
### docker-compose.yml
```
version: "3.6"
services: 

  java: 
    build: ./docker/java
    container_name: java-container
    ports: 
      - 8080:8080
    tty: true
        
  db:
    build: ./docker/db
    container_name: db-container
    restart: always
    ports: 
      - 3306:3306
    environment: 
      - MYSQL_ROOT_PASSWORD=password
      
  adminer:
    image: adminer
    container_name: adminer-container
    restart: always
    ports:
      - 8081:8080
```
JavaおよびDBに関しては、Dockerfileを読み込ませることで必要なファイルを準備する。
### sqls/Dockerfile
```
FROM mariadb

COPY sqls /higuchi/sqls
```
公式のイメージ`mariadb`をpullし、`sqls`配下のSQLファイルたちをコンテナ内にコピーする。
### java/Dockerfile
```
FROM openjdk:17

COPY dockertest /higuchi/server
```
公式のイメージ`openjdk:17`をpullし、`dockertest`配下のアプリケーションソースファイルたちをコンテナ内にコピーする。
### create_table.sql
```
CREATE DATABASE docker_java_db_test;

USE docker_java_db_test;

CREATE TABLE user(
    id int primary key auto_increment,
    user_name varchar(15) not null,
    detail text
);
```
`ユーザ`テーブルを作成する。
### アプリケーションのソースファイル
`java/dockertest`配下を参照。今回はuserテーブルにinsertするための入力画面、テーブルのデータを全件取得して出力する画面およびAPIを用意した。基本的にはいつも通りの作成手順で問題ないが、`application.properties`についてはDBの接続用URLを以下のように記述する：
```
spring.datasource.url=jdbc:mariadb://db-container:3306/docker_java_db_test
```
コンテナ間で連携する際は`//${コンテナ名}:${ポート番号}/${DB名}`と記述するらしい。ポート番号についてはデフォルトの3306で問題ない。

## 実際にやってみる
`docker-compose.yml`を配置しているディレクトリにて
```
docker-compose up -d
```
を実行。yamlファイルおよびDockerfileを読み込んでコンテナを作ってくれる。
### コンテナ内で下準備
#### MariaDB
以下のコマンドでDBコンテナに入る。
```
docker exec -it db-container /bin/bash
```
以下コマンドの後にパスワードを入力してMysqlを起動。
```
mysql -u root -p
```
あらかじめ仕込んであったSQLファイルを実行。今回はDBおよびテーブル作成のみ。
```
source /higuchi/sqls/create_table.sql
```

#### Java
以下のコマンドでJavaコンテナに入る。
```
docker exec -it java-container /bin/bash
```
アプリケーションをビルドする。
```
cd higuchi/server
./mvnw package
```
アプリケーションを実行
```
java -jar target/javadbtest-0.0.1-SNAPSHOT.jar
```
ブラウザで`http://localhost:8080/docker-welcome`にアクセスすればアプリケーションの動作確認ができる。