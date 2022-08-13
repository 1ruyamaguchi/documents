# WIP: Igniteを触ってみる
触ってみます。Javaアプリケーションで、Igniteをキャッシュサーバとして使ってみます。

## openjdkのインストール

パッケージインデックスを更新する。
```
sudo apt update
```

Javaが既にインストールされているか確認する。
```
java -version
```

インストールされていない場合は推奨コマンド群が出てくるので、必要なバージョンのコマンドを叩けばOK。
```
sudo apt install openjdk-17-jre-headless
```

## Igniteのインストール

[公式サイト](https://ignite.apache.org/download.cgi)からzipファイルをダウンロードする。Binary Releasesを選択する。その後、zipファイルを`/opt`配下に展開する。
```
cd /opt
sudo unzip ${zipファイルのパス}
sudo ln -s ${binファイルのパス} /opt/apache-ignite
```

Igniteを起動する。
```
cd /opt/apache-ignite/bin
sudo ./ignite.sh
```

## Javaアプリケーションの準備