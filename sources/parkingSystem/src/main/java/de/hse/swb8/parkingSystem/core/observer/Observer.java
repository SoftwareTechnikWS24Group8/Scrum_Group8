package de.hse.swb8.parkingSystem.core.observer;

public interface Observer<T> {
    void update(Observable<T> observable, T newValue);
}