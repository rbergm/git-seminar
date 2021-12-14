package de.tudresden.geo.gitseminar.util;

import java.util.ArrayList;
import java.util.List;

public class SlidingWindow {

  public static <T> List<List<T>> over(List<T> elements, int windowSize) {
    if (windowSize > elements.size()) {
      throw new IllegalArgumentException("Window size larger than number of elements");
    }
    int nElems = elements.size();
    int nWindows = nElems - windowSize + 1;
    List<List<T>> result = new ArrayList<>(nWindows);

    for (int i = 0; i < nWindows; ++i) {
      var currentWindow = new ArrayList<>(elements.subList(i, i + windowSize));
      result.add(currentWindow);
    }

    return result;
  }

}
