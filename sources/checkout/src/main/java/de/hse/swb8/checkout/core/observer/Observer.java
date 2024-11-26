package de.hse.swb8.checkout.core.observer;

public interface Observer<T> {
    void update(Observable<T> observable, T newValue);
}