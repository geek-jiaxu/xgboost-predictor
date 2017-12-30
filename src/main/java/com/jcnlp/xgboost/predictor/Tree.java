package com.jcnlp.xgboost.predictor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class Tree {
  public List<Node> nodes = Lists.newArrayList();

  public Tree(List<String> dumpText) {

    HashMap<Integer, Node> id2node = Maps.newHashMap();

    for (String line : dumpText) {
      
      String[] idAndContent = line.trim().split(":");
      int id = Integer.parseInt(idAndContent[0]);
      Node node = new Node(id);
      nodes.add(node);
      id2node.put(id, node);
      
      if (idAndContent[1].trim().startsWith("leaf")) {
        node.isLeaf = true;
        // 部分版本叶子节点中多cover参数，such as 811:leaf=-0.0210526,cover=75
        // idAndContent[1].split(",")[0]，兼容此种情况
        double value = Double.parseDouble(idAndContent[1].split(",")[0].split("=")[1].trim());
        node.predict = value;
      } else {
        idAndContent[1] = idAndContent[1].replaceAll("\\s", "");
        String[] items = idAndContent[1].split("[,=<\\[\\]]");
        for (int i = 0; i < items.length;) {
          if (items[i].equals("[")) {
            i++;
            continue;
          }
          if (items[i].startsWith("f")) {
            node.splitFeature = Integer.parseInt(items[i].substring(1));
            node.splitValue = Double.parseDouble(items[i + 1]);
            i += 2;
          } else if (items[i].toLowerCase().equals("yes")) {
            node.yesChildId = Integer.parseInt(items[i + 1]);
            i += 2;
          } else if (items[i].toLowerCase().equals("no")) {
            node.noChildId = Integer.parseInt(items[i + 1]);
            i += 2;
          } else if (items[i].toLowerCase().equals("missing")) {
            node.missingChildId = Integer.parseInt(items[i + 1]);
            i += 2;
          } else {
            i++;
          }
        }
      }

    }
    
    for (Node node : nodes) {
      if (node.isLeaf)
        continue;
      node.yesChild = id2node.get(node.yesChildId);
      node.noChild = id2node.get(node.noChildId);
      node.missingChild = id2node.get(node.missingChildId);
    }
  }
  
  public double predict(Map<Integer, Double> vec) {
    Node root = nodes.get(0);
    Node tmp = root;
    while (true) {
      if (tmp.isLeaf) {
        return tmp.predict;
      }
      int currentFeature = tmp.splitFeature;
      if (vec.containsKey(currentFeature)) {
        double value = vec.get(currentFeature);
        if (value < tmp.splitValue) {
          tmp = tmp.yesChild;
        } else {
          tmp = tmp.noChild;
        }
      } else {
        tmp = tmp.missingChild;
      }
    }
  }
  
}
