# spring-boot-helloworld


```bash
docker build -t helloworld-java .
```

```bash
docker images
```

```bash
docker network create helloworld
```

```bash
docker run --name=mysql --rm --network=helloworld --hostname mysql -e MYSQL_DATABASE=helloworlddb -e MYSQL_ROOT_PASSWORD={MYSQL_ROOT_PASSWORD} mysql
```

```bash
docker run --name=helloworld-java --rm --network=helloworld -p 8080:8080 -e MYSQL_URL=mysql://mysql:3306/helloworlddb helloworld-java
```

### Uploading the container image to a container registry

#### 

```bash
docker login
```

#### 

```bash
docker tag knote-java <username>/helloworld-java:1.0.0
```

#### 

```bash
docker push username/knote-java:1.0.0
```

### 



#### With Minikube installed, you can create a cluster as follows:

```bash
minikube start
```

#### When the command completes, you can verify that the cluster is created with:

```bash
kubectl cluster-info
```

#### Make sure that your Minikube cluster is running:

```bash
minikube status
```

#### submit your resource definitions to Kubernetes with the following command:

```bash
kubectl apply -f k8s
```

#### You can watch your Pods coming alive with:

```bash
kubectl get pods --watch
```

#### In Minikube, a Service can be accessed with the following command:

```bash
minikube service helloworld --url
```

### Scaling your app

#### Kubernetes makes it very easy to increase the number of replicas to 2 or more:

```bash
kubectl scale --replicas=2 deployment/helloworld
```

#### You can watch how a new Pod is created with:

```bash
kubectl get pods -l app=helloworld --watch
```

##### Tags: `Spring Boot` `MySQL` `Kubernetes`