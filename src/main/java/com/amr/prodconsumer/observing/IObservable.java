package com.amr.prodconsumer.observing;

public interface IObservable {
    void addObserver(IObserver o);
    void removeObserver(IObserver o);
    Object notifyObservers();
}
