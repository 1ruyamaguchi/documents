# GitLabをAWS上のRDSに接続する
AWSのRDSを利用して立ち上げたPostgreSQLにGitLabを接続する。

## PostgreSQLの設定
事前準備として、AWSコンソールなどを利用してPostgreSQLを立ち上げる。  
https://docs.aws.amazon.com/ja_jp/AmazonRDS/latest/UserGuide/USER_ConnectToPostgreSQLInstance.html

GitLabサーバに`psql`をインストールする。
```
sudo apt-get install postgresql-client-common
sudo apt-get install postgresql-client
```

以下、PostgreSQLサーバに設定を入れていく。  
https://docs.gitlab.com/ee/administration/postgresql/external.html  
AWSコンソール上に表示されるDBのエンドポイントを控えておき、デフォルトで用意されている`postgres`ユーザを使ってPostgreSQLに接続する。
```
psql \
   --host=${PostgreSQLのエンドポイント} \
   --port=5432 \
   --username=postgres \
   --password \
```

以下のように、PostgreSQL上の操作でGitLab用のユーザおよびデータベースを用意する。
```
# ユーザを作成
CREATE USER gitlab WITH PASSWORD 'gitlab_secret' CREATEDB;

# extensionの作成
CREATE EXTENSION IF NOT EXISTS pg_trgm;
CREATE EXTENSION IF NOT EXISTS btree_gist;

# マスタユーザをgitlabのメンバにする
GRANT gitlab TO postgres

# データベースの作成
CREATE DATABASE gitlabhq_production OWNER gitlab;

# gitlabユーザにスーパーユーザの権限を付与
GRANT rds_superuser TO new_master;
```

## GitLabの設定
dockerで動かすため、`docker-compose.yml`を以下で作成する。
```docker-compose.yml
version: '3'
services:
  gitlab:
    image: gitlab/gitlab-ee:latest
    container_name: gitlab-test
    restart: always
    environment:
      GITLAB_OMNIBUS_CONFIG: |
        external_url "http://${IP address}:80"
        # Disable the built-in Postgres
        postgresql['enable'] = false
        # Fill in the connection details for database.yml
        gitlab_rails['db_adapter'] = 'postgresql'
        gitlab_rails['db_username'] = 'gitlab'
        gitlab_rails['db_password'] = 'gitlab_secret'
        gitlab_rails['db_encoding'] = 'utf8'
        gitlab_rails['db_host'] = '${DBのエンドポイント}'
    ports:
    - '80:80'
    - '2022:22'
    volumes:
    - '/srv/gitlab/config:/etc/gitlab'
    - '/srv/gitlab/logs:/var/log/gitlab'
    - '/srv/gitlab/data:/var/opt/gitlab'
```

## 起動
`docker-compose up`でOK。初期パスワードは以下で確認できる。
```
sudo docker exec -it gitlab-test grep 'Password:' /etc/gitlab/initial_root_password
```