package model;

import java.util.List;

public class Category {
  private String name;
  private double weight; //(0.0 to 1.0)
  private List<Assignment> assignments;
  private int dropLowestCount;//Number of lowest grades to drop

  public Category(){
    this.weight = weight;
    this.name = name;
  }
  public void addAssignment(Assignment a){}
  public List<Assignment> getAssignments(){
    return assignments;
  }
  public double getWeight(){
    return weight;
  }
//  public double calculateCategoryAverage(Student s){
//	  
//  }

}