# GitLabサーバ構築手順書（パッケージ版）

だいたい公式ドキュメント通り。https://about.gitlab.com/install/

## 使用環境
VirtualBoxを使って以下の仮想マシンを動かす。
- Ubuntu 20.04

## GitLabサーバ手順

構成、必要な依存関係をインストール
```
sudo apt-get update
sudo apt-get install -y curl openssh-server ca-certificates tzdata perl
```

postfixをインストール（メール関係が不要ならスキップしてもいいかも）
```
sudo apt-get install -y postfix
```

GitLabのパッケージリポジトリを追加、インストール
```
curl https://packages.gitlab.com/install/repositories/gitlab/gitlab-ee/script.deb.sh | sudo bash
```

DNSの設定をしないのであれば`http://localhost:80`でGitLabにアクセスできる。ただしログイン画面が表示されるまでに時間がかかる。Error: 502が出るようであればしばらく待ってみるとよい。  
root用のログインパスワードは`/etc/gitlab/initial_root_password`に格納されている。24時間経つとファイルが消えるので早めにパスワードを変更する必要がある。

## ネットワーク設定手順

VirtualBoxを使ってGitLabサーバを立てた場合、ホスト側からGitLabにアクセスできるようにする設定をする。

- VirtualBoxの「設定」を開く
- ネットワーク -> 高度 -> ポートフォワーディング　を選択
- 「ホストポート」および「ゲストポート」を設定。ゲストポートは80、ホストポートは（例えば）8888。

`http://localhost:8888`にアクセスすればホスト側からGitLabを見れる。