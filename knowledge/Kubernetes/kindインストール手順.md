# kindインストール手順
ローカルでマルチノードk8s環境を立ち上げるツールであるkindをインストールする。  

## スペック
- Ubuntu20.04.1 LTS
  - メモリ 2GB以上
  - 空き容量20GB以上

## インストール手順
`brew`コマンドを使ってインストールできる。`Docker`および`kubectl`が必要なので併せてインストールする。
### Dockerのインストール

[Dockerインストール](../Docker/Docker%E3%82%A4%E3%83%B3%E3%82%B9%E3%83%88%E3%83%BC%E3%83%AB/Docker%E3%82%A4%E3%83%B3%E3%82%B9%E3%83%88%E3%83%BC%E3%83%AB%E6%89%8B%E9%A0%86.md)を参考にインストールする。

### kubectlのインストール

kubectlのダウンロード
```
curl -LO "https://storage.googleapis.com/kubernetes-release/release/$(curl -s https://storage.googleapis.com/kubernetes-release/release/stable.txt)/bin/linux/amd64/kubectl"
```

kubectlバイナリを実行可能にする
```
chmod +x ./kubectl
```

kubectlバイナリをPATHに通す
```
sudo mv ./kubectl /usr/local/bin/kubectl
```

kubectlがインストールされていること、およびバージョンを確認する
```
kubectl version --client  
```

### Homebrewのインストール

必要なパッケージをインストール
```
sudo apt-get install curl git build-essential
```

Homebrewをインストール
```
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
```

パスを通すために、インストール時に出た以下のコマンドを実行
```
echo 'eval "$(/home/linuxbrew/.linuxbrew/bin/brew shellenv"' >> /home/$USER/.profile
eval "$(/home/linuxbrew/.linuxbrew/bin/brew shellenv)"
```

### kindのインストール

kindをインストール
```
brew install kind
```

kindのバージョンを確認
```
kind version
```

## 使い方

```
# クラスタ構築
kind create cluster
```
コントロールプレーンオンリーのクラスタが立ち上がる。  


```
# クラスタ削除
kind delete cluster
```
クラスタが消える

```
# マルチノードクラスタ構築
kind create cluster --config first-kind.yml
```
以下のyamlに従って、マルチノードのクラスタが立ち上がる。
```yaml:first-kind.yml
kind: Cluster
apiVersion: kind.x-k8s.io/v1alpha4
nodes:
# コントロールプレーン1台
- role: control-plane
# ワーカーノード2台
- role: worker
- role: worker
```

## サンプル
### nginx起動

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

### ボリュームのマウント
あらかじめ各ワーカーノードにディレクトリ・ファイルを仕込んでおく。
```
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
