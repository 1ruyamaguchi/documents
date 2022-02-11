# Spring boot環境構築
プロジェクト構成、依存関係などのSpring bootでの環境構築方法をメモしておく

## 使用環境
- Java: jdk-17.0.2
- エディター: VSCode  
下記の拡張機能をインストール
  - Debugger for Java
  - Extension Pack for Java
  - GitLens
  - Japanese Language Pack for Visual Studio Code
  - Java extension pack
  - Java Language Support
  - Language Support for Java
  - Lombok Annotations Support for VS Code
  - Maven for Java
  - Project Manager for Java
  - Spring Boot Dashboard
  - Spring Boot Extension Pack
  - Spring Boot Tools
  - Spring Initializr Java Support
  - Start git-bash
  - Test Runner for Java
  - Visual Studio IntelliCodeß
## プロジェクト構成
###  依存関係
  - Spring Web
  - Validation
  - Thymeleaf
  - Lombokå
### パッケージ構成（自動生成される部分などは省略）
```
projectName
　┣src
　┃　┣main
　┃　┃　┣java/com/example/projectName
　┃　┃　┃　┣controller
　┃　┃　┃　┃　┗XXXController.java
　┃　┃　┃　┣dao
　┃　┃　┃　┃　┗XXXDao.java #各テーブルに対応したdao
　┃　┃　┃　┣dto
　┃　┃　┃　┃　┗XXXDto.java
　┃　┃　┃　┣entity
　┃　┃　┃　┃　┗XXXEntity.java #各テーブルのカラムに対応したentity
　┃　┃　┃　┣logic
　┃　┃　┃　┃　┣impl
　┃　┃　┃　┃　┃　┗XXXLogicImpl.java
　┃　┃　┃　┃　┗XXXLogic.java
　┃　┃　┃　┣mapper
　┃　┃　┃　┃　┣XXXMapper.java
　┃　┃　┃　┃　┗XXXMapper.xml
　┃　┃　┃　┣service
　┃　┃　┃　┃　┣impl
　┃　┃　┃　┃　┃　┗XXXImpl.java
　┃　┃　┃　┃　┗XXXService.java
　┃　┃　┃　┗ProjectNameApplication.java
　┃　┃　┗resources
　┃　┃　　　┗XXX #ページクラス.htmlを格納
　┃　┗test
　┃　　　┗java/com/example/projectName
　┗sqls
　　　┣create_table.sql #テーブル作成用のSQL文
　　　┣initial_data.sql #イニシャルデータ用のSQL文
　　　┗sample_data.sql #サンプルデータ用のSQL文
```
test/java/com/example/projectName配下はsrc/java/com/example/projectName配下と同一の構成にする。

### DB関連の設定
#### ProjectNameApplication.javaの追加設定
#### pom.xmlの追加設定
#### mybatisの追加設定