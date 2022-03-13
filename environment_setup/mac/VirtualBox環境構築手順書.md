# VirtualBox環境構築
参考：https://www.rem-system.com/mac-virtualbox-install/

## VirtualBoxおよびExtension packを公式ページからインストールする。

## 仮想マシンの作成、起動まで

### 仮想マシンの作成
- VirtualBoxマネージャーの「新規」から流れに沿って行えばOK

### 仮想マシンへのisoファイル挿入
- 仮想マシンを指定、「設定」を選択
- 「ストレージ」を選択
- 「コントローラー：IDE」直下の「空」を選択
- 「光学ドライブ」右のCDアイコンをクリック
- 「仮想光学ディスクの選択/作成」を選択
- 「追加」を選択、予めダウンロードしておいたisoファイルを挿入
- 「OK」を押下

## 日本語入力ができるようにしたい場合
- 以下のコマンドでepelリポジトリをインストール  
　`sudo yum install epel-release`
- 以下のコマンドでmozcおよびibus-mozcをインストール
　`sudo yum install mozc ibus-mozc`
- OSを再起動する
- 設定->地域と言語->+ボタン押下、日本語->日本語（Mozc）を選択
- 設定->デバイス->キーボード　から「前の入力ソース...」「次の入力ソース...」のショートカットキーを確認  
　->デフォルトだと「英数」キー、「かな」キーで切り替え可能？

## バックスペース効かない問題
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

## ウィンドウが拡大できない問題
仮想マシンのウィンドウを拡大しても表示領域が大きくならないので以下の手順を踏む必要あり。
- 上部「Devices」から「install Guest Additions CD image...」を選択
- インストール完了後再起動

## その他
- ホスト側の操作をしたい場合は「command」キーを押下すればOK