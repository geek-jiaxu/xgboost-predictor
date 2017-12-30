# xgboost-predictor
xgboost-predictor ，java使用python训练的xgboost模型文件，模型文件须是dump出的文本文件

### eg
```java
public static void main(String[] args) {
  XgBooster xgb = new XgBooster("./data/dump.model");
  System.out.println(xgb.score("1:1 2:1 3:0.223 33:22 7:3"));
}
```
