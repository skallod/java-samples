package ru.galuzin.tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Жадный алгоритм
 * Задание из курса степик алгоритмы:
 * Даны отрезки на прямой. Найти такие точки, которые лежат на всех заданных отрезках. Найденное множество должно быть минимальным по размеру.
 * Формат входных данных:
 * Первая строка - количество отрезков
 * Последующие строки - координаты начала и конца отрезка, разделенные пробелом
 * Формат выходных данных:
 * Первая строка - количество найденных точек
 * Вторая строка - найденные точки, разделенные пробелом
 */
public class StepikSnippetTask {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        int segmentQuantity = Integer.parseInt(scanner.next());
        final int[][] segments = new int[segmentQuantity][2];
        for (int i = 0; i < segmentQuantity; i++) {
            final int begin = scanner.nextInt();
            final int end = scanner.nextInt();
            segments[i][0] = begin;
            segments[i][1] = end;
        }
        //сортирую отрезки по правому краю
        final List<int[]> collect = Stream.of(segments)
            .sorted((s1, s2) -> s1[1] - s2[1])
            .collect(Collectors.toList());
        final ArrayList<Integer> points = new ArrayList<>();
        int lastPoint = -1;
        for (int i = 0; i < collect.size(); i++) {
            int[] ints = collect.get(i);
            if (i == 0) {
                //правый край 1ого отрезка - надежный шаг
                points.add(ints[1]);
                lastPoint = ints[1];
                continue;
            }
            if (ints[0] <= lastPoint) {
                //точка, принадлежащая отрезку, уже есть в множесвет
                continue;
            }
            points.add(ints[1]);
            lastPoint = ints[1];
        }
        System.out.println(points.size());
        final String collect1 = points.stream()
            .map(String::valueOf)
            .collect(Collectors.joining(" "));
        System.out.println(collect1);
    }
}
//Arrays.sort(segments, Comparator.comparingInt(a -> a[1]));
