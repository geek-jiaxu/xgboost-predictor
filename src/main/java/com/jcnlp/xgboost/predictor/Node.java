package com.jcnlp.xgboost.predictor;

/**
 * Tree Node
 * @author jc
 */
public class Node {
  
  public int seq;
  public Node yesChild;
  public Node noChild;
  public Node missingChild;
  public double predict;
  public boolean isLeaf;
  public int splitFeature;
  public double splitValue;
  public int yesChildId;
  public int noChildId;
  public int missingChildId;
  
  public Node(int seq){
    this.seq = seq;
    yesChild = null;
    noChild = null;
    missingChild = null;
    isLeaf = false;
    splitFeature = -1;
    splitValue = -1;
    yesChildId = -1;
    noChildId = -1;
    missingChildId = -1;
  }

}
