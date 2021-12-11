## Ubuntu設定あれこれ

### vimのインストールおよびバックスペース、方向キーの有効化
```
# vimのインストール
sudo apt-get install vim
```
```
# .vimrcファイルの作成
vi ~/.vimrc
```
ファイル内に`set nocompatible`と記入。

### 内部ネットワーク設定
対象のVirtualBoxの設定 -> ネットワーク -> アダプターを選んで「内部ネットワーク」を選択、ポートの名称を確認する。  
以下を実行：
```
# ネットワークの設定ファイルを作成
sudo vi /etc/netplan/${設定ファイル名}.yaml
``` 
設定内容は以下：
```
network
  version: 2
  renderer: networkd
  ethernets:
    ${ポート名}: 
      addresses: [192.168.1.1/24]
      gateway4: 192.168.0.1
      nameservers:
        addresses: [8.8.8.8]
```
`sudo netplay try`で設定内容を適用できる。`ip addr`で確認しよう。