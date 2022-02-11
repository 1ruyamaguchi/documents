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
  - Visual Studio IntelliCode

## プロジェクト構成
- 依存関係
  - Spring Web
  - Validation
  - Thymeleaf
  - Lombok
- パッケージ構成（自動生成される部分などは省略）
```
${projectName}
┗src
　┣main
　　┣java/com/example/${projectName}
　　┗resources
　┗test

sqls
┗create_table.sql
```