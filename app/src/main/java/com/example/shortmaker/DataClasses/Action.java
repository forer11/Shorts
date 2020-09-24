package com.example.shortmaker.DataClasses;

import java.util.ArrayList;
import java.util.List;

public interface Action {

    void activate();

    void setData(List<String> params);
}
