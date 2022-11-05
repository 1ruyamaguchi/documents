# kindでArgo CDを使う
参考: https://argo-cd.readthedocs.io/en/stable/getting_started/

## クラスタの準備
事前に[kindインストール手順](kind%E3%82%A4%E3%83%B3%E3%82%B9%E3%83%88%E3%83%BC%E3%83%AB%E6%89%8B%E9%A0%86.md)に従ってkindを使えるようにしておく。

## クラスタ起動
以下のyamlファイルを用いてクラスタを起動する。typeを`NodePort`としてクラスタ外と通信ができるようにしている。
```first-cluster.yaml
kind: Cluster
apiVersion: kind.x-k8s.io/v1alpha4
nodes:
- role: control-plane
  extraPortMappings:
  - containerPort: 30080
    hostPort: 30070
    protocol: TCP
```

```
kind create cluster --name first-cluster --config first-cluster.yml
```

## Argo CD起動
namespaceを作成する。
```
kubectl create namespace argocd
```

マニフェストを読み込んでArgo CDを起動する。
```
kubectl apply -n argocd -f https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml
```

`argocd-server`サービスを一部変更してNodePortに対応させる。
```
kubectl patch svc argocd-server -n argocd -p '{"spec": {"type": "NodePort", "ports": [{"name": "http", "port": 80, "protocol": "TCP", "targetPort": 8080, "nodePort": 30080}, {"name": "https", "port": 443, "protocol": "TCP", "targetPort": 8080, "nodePort": 30081}]}}'
```

Argo CD CLIをインストールする。
```
curl -sSL -o argocd-linux-amd64 https://github.com/argoproj/argo-cd/releases/latest/download/argocd-linux-amd64
sudo install -m 555 argocd-linux-amd64 /usr/local/bin/argocd
rm argocd-linux-amd64
```

初回ログイン用のパスワード入手
```
kubectl -n argocd get secret argocd-initial-admin-secret -o jsonpath="{.data.password}" | base64 -d; echo
```

CLIを使ってログイン
```
argocd login ${ホストサーバのIPアドレス}:30070
```

パスワードの更新
```
argocd account update-password
```

`http://${ホストサーバのIPアドレス}:30070`にアクセスすればGUIでログインができる。

## アプリケーションのデプロイ
GUIにて「+ New APP」を押下して、以下を入力する。
- Application Name: 任意のアプリ名
- Project: "default"
- Repository URL: リポジトリのURL（例: https://github.com/1ruyamaguchi/argocd-first-kube-deploy.git）
- Revision: "HEAD"
- Path: デプロイに使うファイルのパス
- Cluster: "https://kubernetes.default.svc"
- Namespace: "default"

アプリケーションが作成されていることを確認する。
```
argocd app get ${Application Name}
```

アプリケーションをデプロイする。
```
argocd app sync ${Application Name}
```