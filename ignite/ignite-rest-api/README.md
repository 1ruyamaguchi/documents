# IgniteのRest API
Igniteに実装されているRest APIを叩く方法を記載する。  
cf: https://ignite.apache.org/docs/latest/quick-start/restapi

## 事前準備

### モジュールの準備

[公式サイト](https://ignite.apache.org/docs/latest/setup#enabling-modules)を参考に、`libs/optional`配下にある`ignite-rest-http`を`libs`に移動する
```
sudo cp -r ignite-rest-http/ ..
```

Ignite起動
```
sudo ./ignite.sh ../examples/config/example-ignite.xml
```

### APIの呼び出し

キャッシュの作成
```
curl "http://localhost:8080/ignite?cmd=getorcreate&cacheName=myCache"
```

データをキャッシュに登録
```
curl "http://localhost:8080/ignite?cmd=put&key=1&val="Hello_World"&cacheName=myCache"
```

キーを指定してデータをキャッシュから取得
```
curl "http://localhost:8080/ignite?cmd=get&key=1&cacheName=myCache"
```
