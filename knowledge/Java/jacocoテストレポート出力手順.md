# Jacocoテストレポート出力手順
参考：https://tosi-tech.net/2020/06/coverage-report-of-jacoco/  
MavenおよびGradleプロジェクトについて記載。

## Maven
### 手順
`pom.xml`の`<plugins>`内に以下を追記する：
```
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.5</version>
    <executions>
        <execution>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
        </execution>
        <execution>
            <id>report</id>
            <phase>test</phase>
            <goals>
                <goal>report</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```
また、`<project>`の中に以下を追加する：
```
<reporting>
<plugins>
    <plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <reportSets>
        <reportSet>
        <reports>
            <report>report</report>
        </reports>
        </reportSet>
    </reportSets>
    </plugin>
</plugins>
</reporting>
```
追記後、`./mvnw test jacoco:report`コマンドを叩く。うまくいけば`target/site/jacoco`内にhtml形式でレポートが作成される。

### ハマったところ
- 以下のようなエラーが出てBUILD FAILUREする場合：
```
Error while creating report: Error while analyzing /Users/nobuhiro/higuchi/GitLab/monitoring/Monitoring/monitoring/target/classes/com/example/monitoring/dto/InputDto.class. Unsupported class file major version
```
`jacoco-maven-pluginjacoco-maven-plugin`内のversionを最新にすると直る（はず）。
## Gradle
WIP...