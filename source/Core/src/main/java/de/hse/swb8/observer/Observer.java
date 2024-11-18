package de.hse.swb8.observer;

public interface Observer<T> {
    void update(Observable<T> observable, T newValue);
}