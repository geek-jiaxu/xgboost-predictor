package com.jcnlp.xgboost.predictor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

/**
 * 保存模型文件
 * 
 * @author jc
 */
public class Model {
  public List<Tree> trees = Lists.newArrayList();
  public double base_score = 0.5f; // 模型基本分, xgboost默认0.5，若要保持与python二进制模型文件打分一致，需要保持与训练模型时的base_score一致

  public Model(String fileName) {
    BufferedReader br = null;
    try{
      br = new BufferedReader(new FileReader(fileName));
      String line = null;
      List<String> treeText = Lists.newArrayList();

      while((line = br.readLine()) != null){
        if(line.trim().startsWith("basescore")){
          base_score = Double.parseDouble(line.trim().split("=")[1].trim());
          continue;
        }
        if(line.trim().startsWith("booster") && treeText.size() > 0){
          trees.add(new Tree(treeText));
          treeText.clear();
          continue;
        }
        if(!line.startsWith("booster"))
          treeText.add(line);
      }
      if(treeText.size() > 0){
        trees.add(new Tree(treeText));
        treeText.clear();
      }
    } catch (Exception e){
      e.printStackTrace();
    }
  }

  public double score(Map<Integer, Double> vec) {
    double result = base_score;
    for (Tree tree : trees) {
      result += tree.predict(vec);
    }
    return result;
  }
  
}
