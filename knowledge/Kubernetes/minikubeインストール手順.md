# minikubeインストール手順
Ubuntu上にminikubeをインストールしてKubernetesの簡易的な環境を構築する。

## スペック
- Ubuntu20.04.1 LTS
  - CPU 2コア以上
  - メモリ 2GB以上
  - 空き容量20GB以上

## Dockerのインストール
公式ドキュメントに準拠：https://docs.docker.com/engine/install/ubuntu/  

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
sudo usermod -aG docker ${userName}
```
グループが存在しない場合は`sudo groupadd docker`で作成する。

マシンの再起動後、必要であれば`sudo systemctl restart docker`でdockerを再起動する。

## kubectlのインストール
公式ドキュメントhttps://kubernetes.io/ja/docs/tasks/tools/install-kubectl/  

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

## minikubeのインストール
公式ドキュメントhttps://minikube.sigs.k8s.io/docs/start/  

minikubeバイナリのダウンロード
```
curl -LO https://storage.googleapis.com/minikube/releases/latest/minikube-linux-amd64
```

minikubeインストール
```
sudo install minikube-linux-amd64 /usr/local/bin/minikube
```

minikubeを起動
```
minikube start --driver=docker
```

minikubeが起動したことを確認
```
minikube status
```
