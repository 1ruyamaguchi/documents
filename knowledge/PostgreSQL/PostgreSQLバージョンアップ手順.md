# PostgreSQLバージョンアップ手順
`pg_upgrade`コマンドを使ったPostgreSQLのバージョンアップ手順。例として12 -> 13の手順を記載する。

## 事前準備

### バックアップを取る
WIP

## バージョンアップ手順

リポジトリの追加
```
sudo sh -c 'echo "deb http://apt.postgresql.org/pub/repos/apt $(lsb_release -cs)-pgdg main" > /etc/apt/sources.list.d/pgdg.list'
```

GPG鍵の追加
```
wget --quiet -O - https://www.postgresql.org/media/keys/ACCC4CF8.asc | sudo apt-key add -
```

パッケージの更新
```
sudo apt-get update
```

新しいバージョンのインストール
```
sudo apt-get install postgresql-13
```

新しいバージョンのクラスタサービスが起動していることを確認
```
systemctl status postgresql@13-main.service 
```

サービスの状態を確認（`enabled`であればOK）
```
systemctl is-enabled postgresql
```

サービスの停止
```
sudo systemctl stop postgresql.service
```

postgresユーザになる
```
sudo su - postgres
```

アップグレード実施
```
 /usr/lib/postgresql/13/bin/pg_upgrade \
     --old-datadir=/var/lib/postgresql/12/main \
     --new-datadir=/var/lib/postgresql/13/main \
     --old-bindir=/usr/lib/postgresql/12/bin \
     --new-bindir=/usr/lib/postgresql/13/bin \
     --old-options '-c config_file=/etc/postgresql/12/main/postgresql.conf' \
     --new-options '-c config_file=/etc/postgresql/13/main/postgresql.conf'
```

一般ユーザに戻る
```
exit
```

新しいバージョンの`postgresql.conf`についてポート番号などを変更
```
sudo vim /etc/postgresql/13/main/postgresql.conf
```
`port = 5433`になっているため、`port = 5432`とすればOK。また、`listen_addresses`についてもコメントアウトを外し、`localhost` -> `*`に変更する

古いバージョンについても同様にポート番号などを変更
```
sudo vim /etc/postgresql/12/main/postgresql.conf
```
`port = 5432` -> `port = 5433`

また、必要であれば`pg_hba.conf`についても新しいファイルに設定を追記する
```
sudo vim /etc/postgresql/13/main/pg_hba.conf
```

サービスを起動
```
sudo systemctl start postgresql.service
```

postgresユーザに戻り、`psql`などで接続先が新しいバージョンになっていることを確認する

オプティマイザの統計情報を収集する
```
./analyze_new_cluster.sh
```

古いバージョンの削除
```
sudo apt-get remove postgresql-12
sudo rm -rf /etc/postgresql/12/
```

postgresユーザにて`delete_old_cluster.sh`を実行
```
sudo su - postgres
./delete_old_cluster.sh
```
