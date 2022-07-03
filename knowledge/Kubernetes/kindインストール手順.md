# kindインストール手順
ローカルで手軽にk8s環境を立ち上げるツールであるkindをインストールする。  
WIP

## スペック
- Ubuntu20.04.1 LTS
  - メモリ 2GB以上
  - 空き容量20GB以上

## インストール手順
`brew`コマンドを使ってインストールできる。

### Homebrewのインストール

curlをインストール
```
sudo apt-get install curl
```

Homebrewをインストール
```
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
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
kind create cluster first-kind.yml
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

