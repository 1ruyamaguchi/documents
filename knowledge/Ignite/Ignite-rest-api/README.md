# IgniteのRest API
Igniteに実装されているRest APIを叩く方法を記載する。  
cf: https://ignite.apache.org/docs/latest/quick-start/restapi

## 事前準備

### モジュールの準備

[公式サイト](https://ignite.apache.org/docs/latest/setup#enabling-modules)を参考に、`libs/optional`配下にある`ignite-rest-http`を`libs`に移動する
```
cp ignite-rest-http ..
```

Ignite起動
```
./ignite.sh ../examples/config/example-ignite.xml
```