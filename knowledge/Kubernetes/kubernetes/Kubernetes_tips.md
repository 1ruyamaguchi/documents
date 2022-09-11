# Kubernetes tips
Kubernetesを触ってて得られたナレッジを記録していく。使用環境については、[Kubernetesクラスター構築手順](Kubernetesクラスター構築手順.md)を参照。



## トラブルシュート編

### `kubectl`コマンドを叩くと`connection refused`エラーになる
`kubectl get nodes`した際に以下のエラーメッセージが出てきた。
>The connection to the server 192.168.144.200:6443 was refused - did you specify the right host or port?

マシンを再起動したとかで`kubelet`が落ちている可能性がある。`systemctl status kubelet`を確認して、起動してなさそうなら以下の手順で復帰可能。  
<br>
swapを無効化
```
sudo swapoff -a
```
`kubelet`リスタート
```
systemctl restart kubelet
```

### metrics-server インストール方法
`kubectl top nodes`でリソースの使用状況を確認するために、metrics-serverをインストールする必要がある。  
cf: https://kubernetes.io/docs/tasks/debug/debug-cluster/resource-metrics-pipeline/#metrics-server  
<br>
インストール
```
kubectl apply -f https://github.com/kubernetes-sigs/metrics-server/releases/latest/download/components.yaml
```

ログを確認。多分動いていない。
```
kubectl logs -n kube-system -l k8s-app=metrics-server --container metrics-server
```

deploymentを編集
```
kubectl edit deploy metrics-server -n kube-system
```
```deployment.yaml
spec:
  containers:
  - args:
    - --cert-dir=/tmp
    - --secure-port=4443
-    - --kubelet-preferred-address-types=InternalIP,ExternalIP,Hostname
+    - --kubelet-preferred-address-types=InternalIP
+    - --kubelet-insecure-tls
    - --kubelet-use-node-status-port
    - --metric-resolution=15s
```

しばらくしてからpodを確認すると、動いているはず。
```
kubectl get pods -n kube-system
```
```
nob@kind:~/kind$ kubectl get pods -n kube-system
NAME                                                  READY   STATUS    RESTARTS   AGE
省略
metrics-server-98c4f9f68-9fnlj                        1/1     Running   0          13m
```
