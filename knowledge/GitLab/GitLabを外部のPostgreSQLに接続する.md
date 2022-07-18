# GitLabを外部のPostgreSQLに接続する
PostgreSQLサーバを外部に立ててGitLabのDBとして運用する。

## 事前準備
- GitLabサーバを立てる
  - [DockerでGitLabサーバを立てる](../Docker/Docker%E3%81%A7GitLab%E3%82%B5%E3%83%BC%E3%83%90%E3%82%92%E6%A7%8B%E7%AF%89/docker%E3%82%B3%E3%83%B3%E3%83%86%E3%83%8A%E3%81%A7GitLab%E3%82%B5%E3%83%BC%E3%83%90%E3%82%92%E7%AB%8B%E3%81%A6%E3%82%8B.md)
  - [GitLabサーバ構築手順書（パッケージ版）](./GitLab%E3%82%B5%E3%83%BC%E3%83%90%E6%A7%8B%E7%AF%89%E6%89%8B%E9%A0%86%E6%9B%B8%EF%BC%88%E3%83%91%E3%83%83%E3%82%B1%E3%83%BC%E3%82%B8%E7%89%88%EF%BC%89.md)
- PostgreSQLサーバを立てる
  - [PostgreSQLサーバ構築手順](../PostgreSQL/PostgreSQL%E3%82%B5%E3%83%BC%E3%83%90%E6%A7%8B%E7%AF%89%E6%89%8B%E9%A0%86.md)

## PostgreSQLサーバ側の設定

postgresユーザになる
```
sudo su - postgres
```

postgreSQLに接続
```
psql
```

gitlabユーザの作成
```
CREATE USER gitlab with PASSWORD 'gitlab_secret';
```

ユーザが作成されていることを確認
```
SELECT * FROM pg_user;
```

gitlabhq_production データベース作成
```
CREATE DATABASE gitlabhq_production OWNER gitlab;
```

スーパーユーザ権限付与
```
ALTER ROLE gitlab WITH SUPERUSER;
```
`SELECT * FROM pg_user;`で`usersuper`が`t`になっていることを確認。

pg_hba.confをvimなどでを編集する。
```
find / -name pg_hba.conf 2> /dev/null
```
ファイルの末尾に以下を追記する。
```
host    all             all             ${Gitlabのaddress}            trust
```
サービスを再起動して変更を適用
```
sudo service postgresql restart
```

## GitLabサーバ側の設定
以下の設定を追記する。パッケージ版であれば`gitlab.rb`ファイルを編集する。コンテナ版であれば`docker-compose.yml`の`services.gitlab.environment.GITLAB_OMNIBUS_CONFIG`に以下を追記する。
```
# Disable the built-in Postgres
postgresql['enable'] = false
# Fill in the connection details for database.yml
gitlab_rails['db_adapter'] = 'postgresql'
gitlab_rails['db_encoding'] = 'utf8'
gitlab_rails['db_username'] = 'gitlab'
gitlab_rails['db_password'] = 'gitlab_secret'
gitlab_rails['db_host'] = '${PostgreSQLのIPアドレス}'
gitlab_rails['db_port'] = 5432
```

あとはいつも通り`docker-compose up -d`で起動すればOK。


  
