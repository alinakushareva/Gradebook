package main.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import model.Assignment;
import model.Grade;

public class Category {
  private String name;
  private double weight; //(0.0 to 1.0)
  private List<Assignment> assignments;
  private int dropLowestCount;//Number of lowest grades to drop

  public Category(){
    this.weight = weight;
    this.name = name;
    this.dropLowestCount = dropLowestCount;
    this.assignments = new ArrayList<>();
  }
  
  /*
   * Adds an assignment to this category.
   * @param a The assignment to add
   */
  public void addAssignment(Assignment a) {
      assignments.add(a);
  }

  /*
   * Returns all assignments in this category.
   * @return List of assignments
   */
  public List<Assignment> getAssignments() {
      return assignments;
  }

  /*
   * Gets the weight of this category (used for weighted averages). 
   * @return Weight between 0.0 and 1.0
   */
  public double getWeight() {
      return weight;
  }

  /*
   * Gets the name of this category.
   * @return Name of the category
   */
  public String getName() {
      return name;
  }

  /*
   * Calculates the average percentage of grades in this category for a student.
   * Drops the lowest N grades if applicable.
   * @param s The student
   * @return Average percentage, or 0.0 if no grades
   */
  public double calculateCategoryAverage(Student s) {
      List<Double> percentages = new ArrayList<>();
      for (Assignment a : assignments) {
          Grade g = a.getGrade(s);
          if (g != null) {
              percentages.add(g.getPercentage());
          }
      }

      percentages.sort(Double::compare);
      for (int i = 0; i < Math.min(dropLowestCount, percentages.size()); i++) {
          percentages.remove(0);
      }

      double sum = 0;
      for (double p : percentages) {
          sum += p;
      }
      return percentages.isEmpty() ? 0 : sum / percentages.size();
  }

}
