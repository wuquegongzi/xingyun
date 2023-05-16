# 创建镜像仓库的秘钥
kubectl create secret docker-registry harbor190 \
-n hs-dev \
--docker-server=http://192.168.216.190:80 \
--docker-username=admin \
--docker-password=Harbor12345

# 秘钥删除命令
kubectl delete secret harbor190  -n hs-dev

# 将脚本复制到 k8s node节点
scp docs/k8s/k8s-deploy* root@k6:/data/docker
sh  k8s-deploy.sh

# 查看deployment
kubectl get deployment -n hs-dev

# 删除deployment
kubectl delete deployment xingyun-app -n hs-dev

# 查看pods列表
kubectl get pods -n hs-dev

# 查看 pods 详情
kubectl describe pod {pod name} -n hs-dev
# 查看 pods日志
kubectl logs {pod name}  -n hs-dev







