package com.amr.prodconsumer.observing;

import java.util.ArrayList;

public interface IObserver {
    ArrayList<Object> react1();
    Object react2(String color);
}
