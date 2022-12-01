# KubernetesでJavaアプリケーションをデプロイ
curlによる疎通確認をするだけのアプリケーションをKubernetesクラスター上でデプロイします。

## 事前準備
- Kubernetesクラスター
- docker hubの（パブリック）リポジトリ

## デプロイ手順

### ディレクトリ構成
```
first-k8s-restapi
    ├─docker
    │   ├─Dockerfile
    │   └─jar
    │       └─app-0.0.1-SNAPSHOT.jar
    └─kube
        ├─java-deployment.yml
        └─java-service.yml  
```

### dockerイメージ作成
あらかじめdockerイメージを作成して、docker hubに登録しておく必要があるため、以下の内容でDockerfileを作成する。
```Dockerfile
FROM openjdk:17

# イメージビルド用の環境変数
ARG jarFile="app-0.0.1-SNAPSHOT.jar"
ARG jarFilePath="/nob/server/jar"
ARG logFilePath="/nob/server/log"
# コンテナ内で使う環境変数
ENV JAR_FILE=${jarFile}
ENV JAR_FILE_PATH=${jarFilePath}

RUN mkdir -p ${jarFilePath}
RUN mkdir ${logFilePath}

COPY ./jar/${jarFile} ${jarFilePath}

CMD java -jar ${JAR_FILE_PATH}/${JAR_FILE}
```

イメージ名は`${userName}/${repositoryName}`にする必要がある。以下のシェルを叩けばイメージが作成できる。
```docker-build.sh
USER_NAME="1ruyamaguchi"
REPOSITORY_NAME="kube-restapi"

# イメージをビルド
docker build -t ${USER_NAME}/${REPOSITORY_NAME} .
```

docker hubにイメージをpushする。
```
docker image push 1ruyamaguchi/kube-restapi:latest
```

### デプロイメント起動
`java-deployment.yml`でPodに関する設定を記載する。`containers`配下にて、`1ruyamaguchi/kube-restapi`をpullして使うことを宣言している。
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
        image: 1ruyamaguchi/kube-restapi:latest
        imagePullPolicy: IfNotPresent
        ports: 
        - containerPort: 8080
```

デプロイメントを起動する
```
kubectl apply -f java-deployment.yml
```

### サービス起動
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

### 動作確認
`curl http://${コントロールプレーンのIPアドレス}:30080/k8s/date`で本日日時が返って来れば正常動作している。`k8s/date`はjava側の設定になるので、変える場合はソースファイルの変更、jarの再作成、imageの再作成が必要になる。
