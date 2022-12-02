# minikubeインストール手順
Ubuntu上にminikubeをインストールしてKubernetesの簡易的な環境を構築する。

## スペック
- Ubuntu20.04.1 LTS
  - CPU 2コア以上
  - メモリ 2GB以上
  - 空き容量20GB以上

## Dockerのインストール
[こちら](../../../docker/Docker%E3%82%A4%E3%83%B3%E3%82%B9%E3%83%88%E3%83%BC%E3%83%AB/README.md)を参考にdockerをインストールする

## kubectlのインストール
[こちら](../../kind/kind%E3%82%A4%E3%83%B3%E3%82%B9%E3%83%88%E3%83%BC%E3%83%AB%E6%89%8B%E9%A0%86/README.md)を参考にkubectlコマンドをインストールする

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
