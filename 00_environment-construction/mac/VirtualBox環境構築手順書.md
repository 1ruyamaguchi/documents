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

## その他
- ホスト側の操作をしたい場合は「command」キーを押下すればOK
- ssh接続できるようにすると便利
```
sudo apt-get install openssh-server
```
