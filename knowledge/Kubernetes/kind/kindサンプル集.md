# サンプル
## nginx起動

sample-cluster-1.yml
```yaml:sample-cluster-1.yaml
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

sample-service-deployment-1.yml
```yaml:sample-service-deployment-1.yaml
# サービスおよびデプロイメント構築
---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: nginx-deployment
spec:
  replicas: 3
  selector:
    matchLabels:
      app: sample-app
  template:
    metadata:
      labels:
        app: sample-app
    spec:
      containers:
      - name: nginx-containers
        image: nginx:1.16
---
apiVersion: v1
kind: Service
metadata:
  name: nginx-service
spec:
  type: LoadBalancer
  ports:
  - name: "http-port"
    protocol: "TCP"
    port: 8080
    targetPort: 80
    nodePort: 30080
  selector:
    app: sample-app
```
`http://${ホスト側のIPアドレス}:30070`にアクセスしてnginxが起動していることを確認できる。

## ボリュームのマウント
あらかじめ各ワーカーノードにディレクトリ・ファイルを仕込んでおく。
```docker-compose.yml
spec:
  containers:
  - name: java-containers
    image: nob-openjdk17:latest
    imagePullPolicy: IfNotPresent
    volumeMounts: 
    - mountPath: /nob
      name: java-volume
  volumes:
  - name: java-volume
    hostPath: 
      path: /nob/server
```
上の場合だと、ノード上の`/nob/server`がPodの`/nob`にマウントされる。どのノードのボリュームがマウントされるかはランダムに決まるらしい。

## ホストマシンのdocker imageをワーカーノードにロード
openjdkのPodを立ち上げようとして、`CrashLoopBackOff`でハマった際の対応。  
cf. https://qiita.com/yokawasa/items/bba45ad775bbf8ac25c3  

ローカルにopenjdkのdocker imageを作成、立ち上がった瞬間落ちないようにするおまじないを追加
```Dockerfile
FROM openjdk:17

CMD tail -f /dev/null
```

各ワーカーノードにdocker imageをロード
```
kind load docker-image ${docker image name} --name ${cluster name}
```
