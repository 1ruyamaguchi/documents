# DockerでDBサーバーを立ててJavaアプリと連携させる
## 目標
DockerでDBサーバを構築し、VSCodeからJavaアプリを動かしてデータのinsert, selectができることを確認する。

## 使用環境
- Java: openjdk-17
- DB: MariaDB

## プロジェクト構成
```
mariadb_test
  ┣server
  ┃  ┣javadbtest
  ┃  ┃  ┗アプリケーションのソースファイル（省略）
  ┃  ┗sqls
  ┃     ┗create_table.sql 
  ┗docker-compose.yml
```
### docker-compose.yml
CREATE DATABASEおよびCREATE TABLEするためのSQLファイルをコンテナ側の`/higuchi/sqls`に配置する。  
`MYSQL_ROOT_PASSWORD=password`に合わせてソースファイル側のパスワードも記載することを忘れずに。
```
version: "3.6"
services: 
        
  db:
    image: mariadb
    restart: always
    ports: 
      - 3306:3306
    volumes: 
      - type: bind
        source: "./server/sqls"
        target: "/higuchi/sqls"
    environment: 
      - MYSQL_ROOT_PASSWORD=password
      
  adminer:
    image: adminer
    restart: always
    ports:
      - 8081:8080
```
`adminer`はブラウザ上で、GUIにてDBを操作できるものらしい。トラシューに使えたりするのでセットにしておく。

### アプリケーションのソースファイル
具体的なコードは省略するが、以下のAPIを用意した：
- userテーブルに１行insertするAPI
- userテーブルの情報を全件取得するAPI

### create_table.sql
後にサーバ内で操作する際、このSQLファイルを実行するだけで下準備が整うようにしてある。イニシャルデータを仕込むことももちろん可能。
```
CREATE DATABASE docker_java_db_test;

USE docker_java_db_test;

CREATE TABLE user(
    id int primary key auto_increment,
    user_name varchar(15) not null,
    detail text
);
```

## 実際にやってみる
以下、起動確認までのコマンドを記載する。Docker Desktopを起動していないと冒頭からコケるので注意（２敗）。

### コンテナを作成
今回作成したdocker-compose.ymlを配置してあるディレクトリにて以下を実行：
```
docker-compose up -d
```
imageが無ければpullしてくれて、コンテナをせっせとこしらえてくれる。

### テーブルを仕込む
以下を叩いてDBのコンテナに入る：
```
docker exec -it ${コンテナのID} /bin/bash
```
SQLファイルを実行してテーブル作成、イニシャルデータの挿入などを行う：
```
source ${path to sql}
```

### アプリケーションを起動
VSCodeなどからいつも通りアプリを起動する。