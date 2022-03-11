## Java、Spring Boot環境構築
VSCodeを使ってJava開発を行うための環境構築

### OpenJDKインストール
参考: https://help.rview.com/hc/ja/articles/360018669493--MacOS-OpenJDKインストール方法
- 以下から必要なものをダウンロード
https://jdk.java.net
- .tarファイルを展開、jdk-***ファイルを  
/ライブラリ/Java/JavaVirtualMachines  
にコピー  
  - settings.jsonにパスを通す。  
  `/usr/libexec/java_home`で出てきたパスを使って`"java.home": "path/to/jdk"`を記載する（/Contents/Homeは不要？）

### VSCodeの拡張機能インストール
インストールしたもの一覧：  
- Java Extension Pack
- Spring Boot Extension Pack
- Lombok Annotations Support for VS Code
