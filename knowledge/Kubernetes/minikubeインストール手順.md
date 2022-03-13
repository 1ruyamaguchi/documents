# minikubeインストール手順
Ubuntu上にminikubeをインストールしてKubernetesの簡易的な環境を構築する。

## スペック
- Ubuntu20.04.1 LTS
  - CPU 2コア以上
  - メモリ 2GB以上
  - 空き容量20GB以上

## kubectlのインストール
curlのインストール
```
sudo apt install curl
```

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

## dockerのインストール
パッケージデータベースの更新
```
sudo apt update
```

dockerに必要なパッケージのインストール
```
sudo apt install apt-transport-https ca-certificates software-properties-common
```

dockerリポジトリを追加
```
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
```

dockerリポジトリをaptに追加
```
sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu focal stable"
```

パッケージデータベースの更新
```
sudo apt update
```

dockerリポジトリが適用されたことの確認
```
apt-cache policy docker-ce
```

dockerのインストール
```
sudo apt install docker-ce
```

dockerが起動していることの確認
```
systemctl status docker
```
Active: active (running)になっていればOK。

ユーザをdockerグループに追加
```
sudo usermod -aG docker ${userName}
```
グループが存在しない場合は`sudo groupadd docker`で作成する。

マシンの再起動後、`sudo systemctl restart docker`でdockerを再起度王する。

## minikubeのインストール

minikubeバイナリのダウンロード
```
curl -Lo minikube https://storage.googleapis.com/minikube/releases/latest/minikube-linux-amd64 \
chmod +x minikube
```

minikubeバイナリを実行可能にする
```
sudo mkdir -p /usr/local/bin/
sudo install minikube /usr/local/bin/
```

minikubeを起動
```
minikube start --driver=docker
```

minikubeが起動したことを確認
```
minikube status
```
