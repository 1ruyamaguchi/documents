# たのしいKubernetes構築
公式ドキュメントに従ってKubernetesを構築した際の手順を残しておく。一部は公式の丸コピ。
<br>
<br>

## マスターノード編
VirtualBox上で仮想マシンを立てて、コントロールプレーンを構築する。  
用意した環境：
- OS: Ubuntu 20.04.3 LTS (Focal Fossa)
- プロセッサー　２　←超重要
- あと何を書けばいいんだろう

### コンテナランタイムインストール
参考にしたもの：https://kubernetes.io/ja/docs/setup/production-environment/container-runtimes/

コンテナランタイムとは…  
>Container image名やContainer実行に関する各種設定情報を受け取ってContainerを作成・実行・停止・削除するソフトウェア全体を指します。

(cf. https://qiita.com/cfg17771855/items/0c0f5df26ffcfe18a74d)  
-> 負荷分散や死活監視をする人？  
<br>
#### 必要な設定の追加
```
cat | sudo tee /etc/modules-load.d/containerd.conf <<EOF
overlay
br_netfilter
EOF
```
```
sudo modprobe overlay
sudo modprobe br_netfilter
```
```
# 必要なカーネルパラメータの設定
cat | sudo tee /etc/sysctl.d/99-kubernetes-cri.conf <<EOF
net.bridge.bridge-nf-call-iptables  = 1
net.ipv4.ip_forward                 = 1
net.bridge.bridge-nf-call-ip6tables = 1
EOF
```
```
# 設定ファイルの読み込み
sudo sysctl --system
```

#### containerdのインストール
```
# (containerdのインストール)
## リポジトリの設定
### HTTPS越しのリポジトリの使用をaptに許可するために、パッケージをインストール
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

### カーネルパラメータ投入 / kubeadm等インストール
参考にしたもの：https://kubernetes.io/ja/docs/setup/production-environment/tools/kubeadm/install-kubeadm/  

```
# br_netfilterモジュールがロードされていることを確認
lsmod | grep br_netfilter

# 明示的にロードするには
modprobe br_netfilter
を実行
```
```
# iptablesがブリッジを通過するトラフィックを処理できるようにする
cat <<EOF | sudo tee /etc/sysctl.d/k8s.conf
net.bridge.bridge-nf-call-ip6tables = 1
net.bridge.bridge-nf-call-iptables = 1
EOF
sudo sysctl --system
```
```
# iptablesがnftablesバックエンドを使用しないようにする
## レガシーバイナリがインストールされていることを確認
sudo apt-get install -y iptables arptables ebtables

## レガシーバージョンに切り替える
sudo update-alternatives --set iptables /usr/sbin/iptables-legacy
sudo update-alternatives --set ip6tables /usr/sbin/ip6tables-legacy
sudo update-alternatives --set arptables /usr/sbin/arptables-legacy
sudo update-alternatives --set ebtables /usr/sbin/ebtables-legacy
```
```
# kubelet, kubeadm, kubectlをインストール
sudo apt-get update && sudo apt-get install -y apt-transport-https curl
curl -s https://packages.cloud.google.com/apt/doc/apt-key.gpg | sudo apt-key add -
cat <<EOF | sudo tee /etc/apt/sources.list.d/kubernetes.list
deb https://apt.kubernetes.io/ kubernetes-xenial main
EOF
sudo apt-get update
sudo apt-get install -y kubelet kubeadm kubectl
sudo apt-mark hold kubelet kubeadm kubectl
```

### コントロールプレーン構築
`sudo kubeadm init`を叩く。  
`sudo kubectl get nodes`で起動確認：
```
NAME                  STATUS     ROLES                  AGE   VERSION
nobuhiro-virtualbox   NotReady   control-plane,master   43m   v1.22.4
```

### CNIプラグインインストール
上で確認できるように、ここまでの作業ではNodeのステータスが`NotReady`になっている。  
このままではKubernetesのNodeとして機能しないため、CNIプラグインをKubernetesにインストールする。  
ここではWeavenetを選択。ほかにも色々種類があるらしい。  
cf. https://www.weave.works/docs/net/latest/kubernetes/kube-addon/#-installation  
上を参考に、以下を叩けばインストールが進む：  
```
kubectl apply -f "https://cloud.weave.works/k8s/net?k8s-version=$(kubectl version | base64 | tr -d '\n')"
```

### コケた箇所
ここまでで、以下の2点でコケた。いずれも`kubeadm init`をたたいた時に怒られた。
- 仮想マシンのCoreが1つだった  
　-> 凡ミス
- swapがoffになっていない  
　-> `sudo swapoff -a`を叩けばOK  

また、`sudo kubectl get nodes`を叩いたときに以下のように怒られた：
>The connection to the server localhost:8080 was refused - did you specify the right host or port?

-> `kubeadm init`時のメッセージによると、admin.confのcopy忘れが原因っぽい。以下のコマンドを叩けばOK：
```
mkdir -p $HOME/.kube
sudo cp /etc/kubernetes/admin.conf $HOME/.kube/config
sudo chown $(id -u):$(id -g) $HOME/.kube/config
``` 
~~それでも直らない場合は、`sudo -E kubectl get nodes`を叩いてみよう。~~  
これで`kubectl get nodes` **（sudo不要）** を実行できる。  

**原因:** 上の3コマンドは一般ユーザでも`kubectl`が叩けるようにconfigを連れてくる的な意味をもつ。  
これらのコマンドを叩いた後に`sudo kubectl get nodes`を叩いてしまうとわざわざrootの方のconfigを見に行ってしまうため、エラーを吐いてしまう。

### 課題点
- カーネルパラメータ投入のあたりは、コマンド叩いてるだけ感が否めない。用語などをしっかり調べて何をしてるのか理解したい。
- `sudo kuectl get nodes`でエラーを吐かれる。`sudo -E kubectl get nodes`を叩いたらnodeが表示された。Eオプションについて、
>現在の環境変数をそのまま保持するのがユーザの意向だと、セキュリティポリシーに指示する。

(cf. https://a4dosanddos.hatenablog.com/entry/2016/09/30/001007)  
とのことであったが、じゃあ何故最初はEオプション無しで上手くいったのか、原因が分かっていない。

## ワーカーノード編
マシンスペック、OSバージョンなどはコントロールプレーンと同様。  

### 各種設定
コントロールプレーンと同じことをすればOK。マスターノード編のコンテナランタイムインストールからkubexxxインストールまでを参照。

### ワーカーノードをkubernetesに参加させる
マスターノードで以下を実行
```
kubeadm token create --print-join-command
```
以下のような、ノードがkubernetesに参加するためのコマンド  
```
kubeadm join 10.20.16.20:6443 --token XXX --discovery-token-ca-cert-hash sha256:XXX
```
が出力される。このコマンドをワーカーノードで実行することでkubernetesに参加させることができる。コントロールプレーンで`kubectl get nodes`（sudo不要）を叩いてチェックしよう。
