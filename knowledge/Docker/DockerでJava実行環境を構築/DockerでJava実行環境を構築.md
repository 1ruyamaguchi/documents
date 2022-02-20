# DockerでJava実行環境を構築
## 目標
公式のopenjdkイメージを使ってJavaの実行環境をDockerで用意する。

## 使用環境
- Java: openjdk-17

## プロジェクト構成
```
java-test
  ┣server
  ┃  ┗アプリケーションのソースファイル（省略）
  ┗docker-compose.yml
```

### docker-compose.yml
```
version: "3.6"
services: 
  java: 
    image: openjdk:17
    ports: 
      - 8080:8080
    tty: true
    volumes: 
      - type: bind
        source: "./server"
        target: "/higuchi/server"
```
`./server`配下のファイル（今回はソースファイルのみだが、仕込みたいシェルスクリプトなど）を`/higuchi/server`にバインドする。

### アプリケーションのソースファイル
Hello World程度のものなので省略。


## 実際にやってみる
起動までの流れ
- `docker-compose up -d`にて、yamlファイルを読み込ませる。トイレに行っている間にコンテナが完成している。
- `docker exec -it ${container ID} /bin/bash`にてコンテナに入る。  
- 適切なディレクトリにて`./mvnw package`を叩く。コンパイルが始まる。
- `java -jar ${path to jar}`を叩けばアプリケーションが起動する。jarファイルはtarget配下に格納されている。
