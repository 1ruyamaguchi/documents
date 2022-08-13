# WIP: Igniteを触ってみる
触ってみます。Javaアプリケーションで、Igniteをキャッシュサーバとして使ってみます。  
cf: https://qiita.com/mkyz08/items/81a526827a573ea17805

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

環境変数`JAVA_HOME`を設定する。
```
# java_home.shを作成する
sudo vi /etc/profile.d/java_home.sh
```

`java_home.sh`に以下の内容を記載する。
```
export JAVA_HOME=`echo $(dirname $(readlink $(readlink $(which java)))) | sed -e 's/\/bin$//g' | sed -e 's/\/jre$//g'`
```

```
# JAVA_HOME反映
source /etc/profile.d/java_home.sh

# 確認
echo $JAVA_HOME
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
https://ignite.apache.org/docs/latest/quick-start/java#running-ignite-with-java-11  
これ見ながらソースを作成した。javaのバージョン11でも17でもダメだった。

### jarを配置して起動しようとしたらなんか例外

```
# ダメだった
java -jar ${jar-file-name}
```
cf. https://blog1.mammb.com/entry/2020/12/28/090000  


```
# これもダメだった
java \
--add-opens java.base/java.nio=ALL-UNNAMED \
-jar firstignite-0.0.1-SNAPSHOT.jar
```