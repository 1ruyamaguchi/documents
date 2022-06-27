# Kubernetes Tips
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
