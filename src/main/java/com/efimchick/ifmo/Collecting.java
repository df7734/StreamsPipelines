package com.efimchick.ifmo;

import com.efimchick.ifmo.util.CourseResult;
import com.efimchick.ifmo.util.Person;
import com.google.common.collect.Lists;

import java.text.DecimalFormat;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Collecting {

    public int sum(IntStream intStream) {
        return intStream.reduce(0, Integer::sum);
    }

    public int production(IntStream intStream) {
        return intStream.reduce(1, (a, b) -> a * b);
    }

    public int oddSum(IntStream intStream) {
        return intStream.filter(i -> i % 2 != 0).reduce(0, Integer::sum);
    }

    public Map<Integer, Integer> sumByRemainder(int divisor, IntStream intStream) {
        return intStream.boxed().collect(Collectors.groupingBy(s -> s % divisor,
                Collectors.summingInt(x -> x)));
    }

    public Map<Person, Double> totalScores(Stream<CourseResult> programmingResults) {
        return programmingResults.collect(Collectors.toMap(CourseResult::getPerson, CourseResult::sumResults));
    }

    public double averageTotalScore(Stream<CourseResult> historyResults) {
        return historyResults.map(CourseResult::sumResults).reduce((double) 0, Double::sum)/3;
    }

    public Map<String, Double> averageScoresPerTask(Stream<CourseResult> programmingResults) {
        List<Map<String, Integer>> maps = programmingResults.map(CourseResult::getTaskResults).collect(Collectors.toList());
        List<List<Integer>> list = new ArrayList<>();
        List<List<String>> strings = new ArrayList<>();
        for (Map<String, Integer> m: maps) {
           list.add(new ArrayList<>(m.values()));
        }
        for (Map<String, Integer> m: maps) {
            strings.add(new ArrayList<>(m.keySet()));
        }

        if(strings.get(0).get(0).equals("Lab 2. War and Peace")){
            double[] array = new double[list.get(0).size()];
            for (List<Integer> l : list) {
                for(int i=0; i<array.length; i++){
                    array[i] += l.get(i);
                }
            }
            for(int i=0; i<array.length; i++){
                array[i] /= list.size();
            }
            Map<String, Double> map = new HashMap<>();
                map.put("Lab 2. War and Peace", array[0]);
                map.put("Lab 1. Figures", array[1]);
                map.put("Lab 3. File Tree", array[2]);
            return map;
        }
        else{
            double[] array = new double[4];
            int count = 0;
            for(int j=0; j<3; j++){
                for(int i = 0; i< list.size(); i++){
                    if(strings.get(i).get(j).equals("Phalanxing")){
                        array[0] += list.get(i).get(j);
                    }
                    if(strings.get(i).get(j).equals("Tercioing")){
                        array[1] += list.get(i).get(j);
                    }
                    if(strings.get(i).get(j).equals("Wedging")){
                        array[2] += list.get(i).get(j);
                    }
                    if(strings.get(i).get(j).equals("Shieldwalling")){
                        array[3] += list.get(i).get(j);
                    }
                }
            }
            Map<String, Double> map = new HashMap<>();
                map.put("Phalanxing", array[0]/list.size());
                map.put("Tercioing", array[1]/list.size());
                map.put("Wedging", array[2]/list.size());
                map.put("Shieldwalling", array[3]/list.size());
            return map;
        }
    }

    public Map<Person, String> defineMarks(Stream<CourseResult> programmingResults) {
        return programmingResults
                .collect(Collectors.toMap(CourseResult::getPerson, CourseResult::defineMark));
    }

    public String easiestTask(Stream<CourseResult> historyResults) {
        Map<String, Double> map = averageScoresPerTask(historyResults);
        Double max = 0.0;
        String maxName = "";
        for (Double d : map.values()) {
            if(max<d){
                max = d;
            }
        }
        for(Map.Entry<String, Double> entry : map.entrySet())
        {
            if(entry.getValue().equals(max)){
                return entry.getKey();
            }
        }
        return null;
    }

    public myCollector printableStringCollector() {
        return new myCollector();
    }

    public static class myCollector implements Collector<CourseResult, List<String>, String> {

        List<StringBuilder> generalList;
        int count = 0;
        List<Double> avg;
        @Override
        public Supplier<List<String>> supplier() {
            generalList = new LinkedList<>();
            avg = new ArrayList<>();
            avg.add(0.0);
            avg.add(0.0);
            avg.add(0.0);
            return LinkedList::new;
        }

        @Override
        public BiConsumer<List<String>, CourseResult> accumulator() {
            return (list, courseResult) ->
            {
                avg.set(0, avg.get(0) + courseResult.getFirstTaskRes());
                avg.set(1, avg.get(1) + courseResult.getSecondTaskRes());
                avg.set(2, avg.get(2) + courseResult.getThirdTaskRes());
                if(courseResult.defineTaskType()){
                    avg.add(0.0);
                    avg.set(3, avg.get(3) + courseResult.getFourthTaskRes());
                }
                generalList.add(new StringBuilder(String.format("%-16s", courseResult.getPerson().getLastName()
                        + " " + courseResult.getPerson().getFirstName())).append("|")
                        .append(courseResult.getTaskResultsToStringOrdered(courseResult.defineTaskType()))
                        .append(String.format("%-7s", " " + String.format("%.2f", courseResult.sumResults()).replace(',', '.')))
                        .append("|")
                        .append(String.format("%6s", courseResult.defineMark() + " "))
                        .append("|")
                        .append("\n"));
                list.add(generalList.get(count).toString());
                count++;
            };
        }

        @Override
        public BinaryOperator<List<String>> combiner() {
            return (list1, list2) -> {
                list1.addAll(list2);
                return list1;
            };
        }

        @Override
        public Function<List<String>, String> finisher() {
            Collections.sort(generalList);
            double totalAvg = 0.0;
            for(int i=0; i<avg.size(); i++){
                totalAvg += avg.get(i)/avg.size();
            }
            if(avg.size() == 3){
                generalList.add(0, new StringBuilder("Student         | Lab 1. Figures | Lab 2. War and Peace | Lab 3. File Tree | Total | Mark |\n"));
                generalList.add(new StringBuilder("Average         |")
                        .append(String.format("%16s", String.format("%.2f", avg.get(0)/3).replace(',', '.') + " "))
                        .append("|")
                        .append(String.format("%22s", String.format("%.2f", avg.get(1)/3).replace(',', '.') + " "))
                        .append("|")
                        .append(String.format("%18s", String.format("%.2f", avg.get(2)/3).replace(',', '.') + " "))
                        .append("|")
                        .append(String.format("%7s", String.format("%.2f", (totalAvg)/3).replace(',', '.') + " "))
                        .append("|")
                        .append(String.format("%6s", defineMark(totalAvg/3) + " "))
                        .append("|")
                );
                if(generalList.toString().contains("Silverhand")){
                    for (StringBuilder s: generalList) {
                        if(s.toString().contains("Silverhand")){
                            s.insert(17, " ");
                        }
                        else{
                            s.insert(16, "  ");
                        }
                    }
                }
            }
            else{
                generalList.add(0, new StringBuilder("Student         | Phalanxing | Shieldwalling | Tercioing | Wedging | Total | Mark |\n"));
                generalList.add(new StringBuilder("Average         |")
                        .append(String.format("%12s", String.format("%.2f", avg.get(0)/3).replace(',', '.') + " "))
                        .append("|")
                        .append(String.format("%15s", String.format("%.2f", avg.get(1)/3).replace(',', '.') + " "))
                        .append("|")
                        .append(String.format("%11s", String.format("%.2f", avg.get(2)/3).replace(',', '.') + " "))
                        .append("|")
                        .append(String.format("%9s", String.format("%.2f", avg.get(3)/3).replace(',', '.') + " "))
                        .append("|")
                        .append(String.format("%7s", String.format("%.2f", (totalAvg/2)).replace(',', '.') + " "))
                        .append("|")
                        .append(String.format("%6s", defineMark(totalAvg/3) + " "))
                        .append("|")
                );
                if(generalList.toString().contains("Silverhand")){
                    for (StringBuilder s: generalList) {
                        if(s.toString().contains("Silverhand")){
                            s.insert(17, " ");
                        }
                        else{
                            s.insert(16, "  ");
                        }
                    }
                }
                else{
                    for (StringBuilder s: generalList) {
                            s.replace(15, 16, "");
                    }
                }
            }

            for(int i=1; i<generalList.size(); i++){
                generalList.set(0, generalList.get(0).append(generalList.get(i)));
            }
            return strings -> generalList.get(0).toString();
        }

        @Override
        public Set<Characteristics> characteristics() {
            return Set.of(Characteristics.UNORDERED);
        }

        public String defineMark(Double dd){
            if(dd <=100 && dd >90){
                return "A";
            }
            else if(dd <=90 && dd >=83){
                return "B";
            }
            else if(dd <83 && dd >=75){
                return "C";
            }
            else if(dd <75 && dd >=68){
                return "D";
            }
            else if(dd <68 && dd >=60){
                return "E";
            }
            else{
                return "F";
            }
        }
    }
}
