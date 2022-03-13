# DockerコンテナでGitLabサーバを立てる

## 事前準備
仮装マシン、数々のエラーと戦う時間を用意する。

### スペック
- ubuntu: 20.04

### ubuntu側の準備

#### バックスペース効かない問題
docker-compse.ymlを編集する際にバックスペースが利かずに困ることがあるので、以下の設定が必要。  
vimのインストール
```
apt-get install vim
```
.vimrcファイルの作成
```
vi ~/.vimrc
```
ファイル内に`set nocompatible`と記入。

#### ウィンドウが拡大できない問題
仮想マシンのウィンドウを拡大しても表示領域が大きくならないので以下の手順を踏む必要あり。
- 上部「Devices」から「insert Guest Additions CD image...」を選択
- インストール完了後再起動


## Dockerのインストール
必要なパッケージをインストール
```
apt install \
apt-transport-https \
ca-certificates \
curl \
gnupg-agent \
software-properties-common
```
GPG鍵の入手  
GPG鍵はメールやファイルの暗号化、ファイルの署名に使うらしい。
```
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key  add -
```
リポジトリの登録
```
add-apt-repository \
"deb [arch=amd64] https://download.docker.com/linux/ubuntu \
$(lsb_release -cs) \
stable"
```
設定ファイルが作成されたことの確認
```
cat /etc/apt/sources.list | grep docker
```
使用可能なdockerのバージョンを調べる
```
apt-cache madison docker-ce
```
dockerをインストール
```
apt install docker-ce=${インストールするバージョン名}
```
インストールが成功していることを確認
```
docker --version
```
dockerが起動していることを確認
```
systemctl status docker
```
`active(running)`になっていればOK

### docker-compose インストール
公式サイトを参考にすればなんとかなる　はず。  

docker-composeの最新版ダウンロード
```
sudo curl -L https://github.com/docker/compose/releases/download/1.16.1/docker-compose-`uname -s`-`uname -m` -o /usr/local/bin/docker-compose
```
バイナリに対して実行権限を付与
```
sudo chmod +x /usr/local/bin/docker-compose
```

### gitlabコンテナを構築
ディレクトリ構成は以下
```
gitlab
　├─config
　├─logs
　├─data
　├─registry
　└─docker-compose.yml
```
docker-compose.ymlを作成
```
version: '3'
services:
  gitlab:
    image: gitlab/gitlab-ce:latest
    restart: always
    hostname: gitlab-private
    container_name: gitlab-private
    environment:
      GITLAB_OMNIBUS_CONFIG: |
        external_url "http://${SERVER_NAME}"
        registry_external_url "http://${SERVER_NAME}:4567"
    ports:
      - '80:80'
      - '8022:22'
      - '4567:4567'
    volumes:
      - './gitlab/config:/etc/gitlab'
      - './gitlab/logs:/var/log/gitlab'
      - './gitlab/data:/var/opt/gitlab'
      - './gitlab/registry:/var/opt/gitlab/gitlab-rails/shared/registry'
```
`docker-compose up -d`でコンテナを起動する。アクセスできるようになるまでに数分ラグがあるらしい。しばらく待って`http://localhost:80`にアクセスするとgitlabの画面が表示される。
