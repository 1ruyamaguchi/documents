# WIP: IgniteをJavaアプリケーションのキャッシュサーバとして運用する

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

h2の依存関係追加してみたりした  
https://jar-download.com/artifacts/com.h2database/h2/1.3.158/source-code/org/h2/index/BaseIndex.java