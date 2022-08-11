# kindでJavaアプリケーションをデプロイ
DBとの繋ぎこみすら行わない、curlによるレスポンスを受け取るだけのアプリケーションをkind上でデプロイします。

## 事前準備
- kindのインストールされたマシン

## デプロイ手順

### ディレクトリ構成
```
first-k8s-restapi
    ├─docker
    │   ├─Dockerfile
    │   └─jar
    │       └─app-0.0.1-SNAPSHOT.jar
    └─kube
        ├─java-cluster.yml
        ├─java-deployment.yml
        └─java-service.yml  
```

### デプロイ手順

#### クラスタ構築

`java-cluster.yml`では、ワーカーノードを2台にすること、service向けのポートを30080にする旨が記載されている。
```java-cluster.yml
# クラスタ構築
kind: Cluster
apiVersion: kind.x-k8s.io/v1alpha4
nodes:
# コントロールプレーン1台
- role: control-plane
  extraPortMappings:
    # ServiceのnodePortとして指定するポート
  - containerPort: 30080
    # ホスト側のポートを指定
    hostPort: 30070
# ワーカーノード2台
- role: worker
- role: worker
```

以下のコマンドで`java-cluster`クラスターを構築する。
```
kind create cluster --config java-cluster.yml --name java-cluster
```

#### dockerイメージ作成

`Dockerfile`では、openjdk17をベースとして、
- ログ格納用のディレクトリを作成すること
- jarファイルをコピーしておくこと
- 起動した瞬間に死なないこと

が記載されている。
```Dockerfile
FROM openjdk:17

RUN mkdir -p /nob/server/jar
RUN mkdir /nob/server/log

COPY ./jar/app-0.0.1-SNAPSHOT.jar /nob/server/jar

CMD tail -f /dev/null
```

`first-k8s-restapi/docker`にてdockerイメージを作成する。`-t`オプションで名前をつけておく。
```
docker build ./ -t nob-openjdk17
```

各ワーカーノードにdockerイメージをロードする。これを忘れるとデプロイメント起動時にPodがimageを取得できずに`CrashLoopBackOff`し続ける。
```
kind load docker-image nob-openjdk17 --name java-cluster
```

#### デプロイメント起動
`java-deployment.yml`でPodに関する設定を記載する。`containers`配下にて、ローカルの`nob-openjdk17`を使うことを宣言している。
```java-deployment.yml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: java-deployment
spec:
  replicas: 2
  selector:
    matchLabels:
      app: java-app
  template:
    metadata:
      labels:
        app: java-app
    spec:
      containers:
      - name: java-containers
        image: nob-openjdk17:latest
        imagePullPolicy: IfNotPresent
        ports: 
        - containerPort: 8080
```

デプロイメントをapplyする。
```
kubectl apply -f java-deployment.yml
```

#### サービス起動
`java-service.yml`によって外部から通信できるようにする。
```java-service.yml
apiVersion: v1
kind: Service
metadata:
  name: java-service
spec:
  type: NodePort
  ports:
  - name: "java-port"
    protocol: "TCP"
    port: 8080
    nodePort: 30080
  selector:
    app: java-app
```

サービスをapplyする。
```
kubectl apply -f java-service.yml
```

#### 動作確認
各Podでjavaアプリケーションを起動して（**要改善！！！**）、`curl http://${kindサーバのIPアドレス}:30070/k8s/date`で本日日時が返って来れば正常動作している。`k8s/date`はjava側の設定になるので、変える場合はソースファイルの変更、jarの再作成、imageの再作成が必要になる。
