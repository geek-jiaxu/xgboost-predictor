package com.jcnlp.xgboost.predictor;

import java.util.Map;
import com.google.common.collect.Maps;
/**
 * Java 调用Python训练的xgboot模型
 * input : dump text file model & features such as 1:11 2:0.22 3:44 4:0.89
 * @author jc
 */
public class XgBooster {
  private Model model;

  public XgBooster(String modelPath){
    loadModel(modelPath);
  }
  
	private void loadModel(String modelPath) {
	  model = new Model(modelPath);
	}

	public double score(Map<Integer, Double> vec){
		return model.score(vec);
	}
	
	public double score(String vecString) {
	  String[] features = vecString.split(" ");
	  Map<Integer, Double> vec = Maps.newHashMap();
	  for (String feature : features) {
	    String[] args = feature.split(":");
	    vec.put(Integer.parseInt(args[0]), Double.parseDouble(args[1]));
	  }
	  return score(vec);
	}

	public static void main(String[] args) {
	  XgBooster xgb = new XgBooster("/Users/jc/eclipse-workspace/xgboost-predictor/data/dump.model");
	  System.out.println(xgb.score("1:30 2:1 33:22 7:3"));
	}
	
}
