# kindインストール手順
ローカルでマルチノードk8s環境を立ち上げるツールであるkindをインストールする。  

## スペック
- Ubuntu20.04.1 LTS
  - メモリ 2GB以上
  - 空き容量20GB以上

## インストール手順
`brew`コマンドを使ってインストールできる。`Docker`および`kubectl`が必要なので併せてインストールする。
### Dockerのインストール

必要なパッケージをインストール
```
sudo apt-get update
```
```
 sudo apt-get install \
    ca-certificates \
    curl \
    gnupg \
    lsb-release
```
GPG鍵の入手  
GPG鍵はメールやファイルの暗号化、ファイルの署名に使うらしい。
```
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg
```
リポジトリの登録
```
echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/ubuntu \
  $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
```
Docker Engine インストール
```
sudo apt-get update
```
```
sudo apt-get install docker-ce docker-ce-cli containerd.io docker-compose-plugin
```

インストールが成功していることを確認
```
sudo docker --version
```
dockerが起動していることを確認
```
systemctl status docker
```
`active(running)`になっていればOK

ユーザをdockerグループに追加
```
sudo usermod -aG docker $USER
```
グループが存在しない場合は`sudo groupadd docker`で作成する。

マシンの再起動後、必要であれば`sudo systemctl restart docker`でdockerを再起動する。

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
nginx起動
```yaml:sample-cluster-1.yaml
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
```yaml:sample-service-deployment-1.yaml
---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: order1-deployment
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
  name: order2-service
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
