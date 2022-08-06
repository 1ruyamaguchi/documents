# Kubernetesクラスター構築手順
公式ドキュメントに従ってKubernetesクラスターを構築する。

## 事前準備
cf. https://kubernetes.io/ja/docs/setup/production-environment/tools/kubeadm/_print/

### マシン
VirtualBox上で仮想マシンを立てて構築。ドキュメントに記載されている最低条件ギリギリ  
- メモリ：2GB
- CPU：2コア
- HDD: 32GB
- OS: Ubuntu 20.04.3

### エラー回避のための設定

swapの無効化
```
sudo swapoff -a
```

ただし、上記の方法だとノードを再起動するとswapが再度有効化されてしまう。永続的に無効化したい場合は`/etc/fstab`ファイルのswapに関する行をコメントアウトしてリブートする。

## コントロールプレーン構築

### ランタイムのインストール
cf. https://kubernetes.io/ja/docs/setup/production-environment/container-runtimes/  

必要な設定の追加
```
cat | sudo tee /etc/modules-load.d/containerd.conf <<EOF
overlay
br_netfilter
EOF

sudo modprobe overlay
sudo modprobe br_netfilter

# 必要なカーネルパラメータの設定をします。これらの設定値は再起動後も永続化されます。
cat | sudo tee /etc/sysctl.d/99-kubernetes-cri.conf <<EOF
net.bridge.bridge-nf-call-iptables  = 1
net.ipv4.ip_forward                 = 1
net.bridge.bridge-nf-call-ip6tables = 1
EOF

sudo sysctl --system
```

containerdのインストール
```
# HTTPS越しのリポジトリの使用をaptに許可するために、パッケージをインストール
sudo apt-get update 
sudo apt-get install -y apt-transport-https ca-certificates curl software-properties-common
```
```
# Docker公式のGPG鍵を追加
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
```
```
# Dockerのaptリポジトリの追加
sudo add-apt-repository \
    "deb [arch=amd64] https://download.docker.com/linux/ubuntu \
    $(lsb_release -cs) \
    stable"
```
```
# containerdのインストール
sudo apt-get update 
sudo apt-get install -y containerd.io
```
```
# containerdの設定
sudo mkdir -p /etc/containerd
containerd config default | sudo tee /etc/containerd/config.toml
```
```
# containerdの再起動
sudo systemctl restart containerd
```

### kubeadm, kubelet, kubectlのインストール
cf. https://kubernetes.io/ja/docs/setup/production-environment/tools/kubeadm/_print/#kubeadm-kubelet-kubectlのインストール  

```
sudo apt-get update && sudo apt-get install -y apt-transport-https curl
curl -s https://packages.cloud.google.com/apt/doc/apt-key.gpg | sudo apt-key add -
cat <<EOF | sudo tee /etc/apt/sources.list.d/kubernetes.list
deb https://apt.kubernetes.io/ kubernetes-xenial main
EOF
sudo apt-get update
sudo apt-get install -y kubelet kubeadm kubectl
sudo apt-mark hold kubelet kubeadm kubectl
```

### コントロールプレーンの起動
cf. https://kubernetes.io/ja/docs/setup/production-environment/tools/kubeadm/_print/#pg-134ed1f6142a98e6ac681a1ba4920e53  

コントロールプレーンノードの初期化。`kubeadm join`コマンドを控えておく。
```
sudo kubeadm init
```

一般ユーザでも`kubectl`コマンドを叩けるようにする。
```
mkdir -p $HOME/.kube
sudo cp /etc/kubernetes/admin.conf $HOME/.kube/config
sudo chown $(id -u):$(id -g) $HOME/.kube/config
```

CNIプラグインを適用する。これが無いと`kubectl get node`で確認した際のノードのStatusが`NotReady`のまま動かない。
```
kubectl apply -f "https://cloud.weave.works/k8s/net?k8s-version=$(kubectl version | base64 | tr -d '\n')"
```

## ワーカーノード構築
cf. https://kubernetes.io/ja/docs/setup/production-environment/tools/kubeadm/_print/#join-nodes

### kubeXXXインストール
コントロールプレーン構築の[ランタイムのインストール](#ランタイムのインストール)および[kubeadm, kubelet, kubectlのインストール](#kubeadm-kubelet-kubectlのインストール)と同様の手順を踏む。

### ノードをクラスターに参加させる
先に控えた`kubeadm join`コマンドを叩く。しばらく経ってから`kubectl get nodes`するとノードのStatusが`Ready`になる。

