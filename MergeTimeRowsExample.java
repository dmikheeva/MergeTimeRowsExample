package com.example;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Daria on 18.03.2018.
 */
public class MergeTimeRowsExample {
    private static TreeMap<LocalDateTime, Integer> mergeRows(TreeMap<LocalDateTime, Integer> row1,
                                                             TreeMap<LocalDateTime, Integer> row2) {
        if (row2 == null || row2.isEmpty()) {
            throw new IllegalArgumentException("Row2 cannot be empty!");
        }
        if (row1 == null || row1.isEmpty()) {
            return new TreeMap(row2);
        }
        LocalDateTime start = row2.firstKey();
        Iterator<Map.Entry<LocalDateTime, Integer>> row1Iterator = row1.entrySet().iterator();
        Iterator<Map.Entry<LocalDateTime, Integer>> row2Iterator = row2.entrySet().iterator();
        TreeMap<LocalDateTime, Integer> result = new TreeMap();
        Map.Entry<LocalDateTime, Integer> row1Elem = null, row2Elem;
        //сдвигаемся по первому ряду, пока не найдем элемент, больший стартового значения второго ряда
        while (row1Iterator.hasNext()) {
            row1Elem = row1Iterator.next();
            if (row1Elem.getKey().isAfter(start)) {
                break;
            }
        }
        row2Elem = row2Iterator.next();
        while (row2Elem != null) {
            if (row1Elem == null) {
                //если в первом ряду больше нет элементов, просто копируем хвост второго ряда в результат
                result.putAll(row2.tailMap(row2Elem.getKey()));
                row2Elem = null;
            } else {
                if (row1Elem.getKey().isBefore(row2Elem.getKey())) {
                    result.put(row1Elem.getKey(), row1Elem.getValue());
                    row1Elem = row1Iterator.hasNext() ? row1Iterator.next() : null;
                } else {
                    if (row1Elem.getKey().isEqual(row2Elem.getKey())) {
                        row1Elem = row1Iterator.hasNext() ? row1Iterator.next() : null;
                    }
                    result.put(row2Elem.getKey(), row2Elem.getValue());
                    row2Elem = row2Iterator.hasNext() ? row2Iterator.next() : null;
                }
            }
        }
        return result;
    }

    private static void printResult(Map<?, ?> result) {
        for (Map.Entry e : result.entrySet()) {
            System.out.println("timestamp: " + e.getKey() + ", value: " + e.getValue());
        }
    }

    public static void main(String[] args) {
        TreeMap<LocalDateTime, Integer> row1 = new TreeMap<>();
        row1.put(LocalDateTime.of(2018, Month.MARCH, 18, 22, 0, 10), 10);
        row1.put(LocalDateTime.of(2018, Month.MARCH, 18, 21, 31, 10), 20);
        row1.put(LocalDateTime.of(2018, Month.MARCH, 18, 21, 21, 10), 30);
        row1.put(LocalDateTime.of(2018, Month.MARCH, 18, 21, 21, 0), 40);
        row1.put(LocalDateTime.of(2018, Month.MARCH, 17, 21, 0, 15), 50);
        row1.put(LocalDateTime.of(2018, Month.MARCH, 18, 23, 56, 10), 30);

        TreeMap<LocalDateTime, Integer> row2 = new TreeMap<>();
        row2.put(LocalDateTime.of(2018, Month.MARCH, 18, 20, 0, 10), 10);
        row2.put(LocalDateTime.of(2018, Month.MARCH, 18, 21, 31, 10), 20);
        row2.put(LocalDateTime.of(2018, Month.MARCH, 18, 21, 21, 0), 30);
        row2.put(LocalDateTime.of(2018, Month.MARCH, 19, 21, 21, 5), 40);
        row2.put(LocalDateTime.of(2018, Month.MARCH, 18, 21, 17, 15), 50);
        row2.put(LocalDateTime.of(2018, Month.MARCH, 18, 21, 56, 10), 60);

        printResult(mergeRows(row1, row2));
    }
}
