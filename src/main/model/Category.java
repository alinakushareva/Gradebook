package main.model;

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
  public void addAssignment(Assignment a) {
      assignments.add(a);
  }

  public List<Assignment> getAssignments() {
      return assignments;
  }

  public double getWeight() {
      return weight;
  }

  public String getName() {
      return name;
  }

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
