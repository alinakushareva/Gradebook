/**
 * Project Name: Gradebook
 * File Name: Subject.java
 * Course: CSC 335 Spring 2025
 * 
 * Purpose: This interface defines the contract for any object that wants to act as 
 * a "Subject" in the Observer Design Pattern. It allows observers to be added, 
 * removed, and notified of changes. This supports dynamic UI updates in response 
 * to model changes.
 */
package model;

public interface Subject {
    void addObserver(Observer o);
    void removeObserver(Observer o);
    void notifyObservers();
}
