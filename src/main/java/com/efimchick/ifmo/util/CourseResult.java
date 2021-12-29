package com.efimchick.ifmo.util;

import java.util.*;

public class CourseResult {
    private final Person person;
    private final Map<String, Integer> taskResults;

    public CourseResult(final Person person, final Map<String, Integer> taskResults) {
        this.person = person;
        this.taskResults = taskResults;
    }

    public Person getPerson() {
        return person;
    }

    public Map<String, Integer> getTaskResults() {
        return taskResults;
    }


    public boolean defineTaskType(){
        for(Map.Entry<String, Integer> entry : taskResults.entrySet()){
            if(entry.getKey().contains("Phalanxing") || entry.getKey().contains("Shieldwalling") || entry.getKey().contains("Tercioing")){
                return true;
            }
        }
        return false;
    }

    public Double getFirstTaskRes(){
        for(Map.Entry<String, Integer> entry : taskResults.entrySet()){
            if(entry.getKey().equals("Lab 1. Figures") || entry.getKey().equals("Phalanxing")){
                return Double.parseDouble(entry.getValue().toString());
            }
        }
        return 0.0;
    }

    public Double getSecondTaskRes(){
        for(Map.Entry<String, Integer> entry : taskResults.entrySet()){
            if(entry.getKey().equals("Lab 2. War and Peace") || entry.getKey().equals("Shieldwalling")){
                return Double.parseDouble(entry.getValue().toString());
            }
        }
        return 0.0;
    }

    public Double getThirdTaskRes(){
        for(Map.Entry<String, Integer> entry : taskResults.entrySet()){
            if(entry.getKey().equals("Lab 3. File Tree") || entry.getKey().equals("Tercioing")){
                return Double.parseDouble(entry.getValue().toString());
            }
        }
        return 0.0;
    }

    public Double getFourthTaskRes(){
        for(Map.Entry<String, Integer> entry : taskResults.entrySet()){
            if(entry.getKey().equals("Wedging")){
                return Double.parseDouble(entry.getValue().toString());
            }
        }
        return 0.0;
    }

    public String getTaskResultsToStringOrdered(boolean phalan) {
        List<String> list = new ArrayList<>();
        list.add("");
        list.add("");
        list.add("");
        if(phalan){
            list.add("");
        }
        for(Map.Entry<String, Integer> entry : taskResults.entrySet()){
            if(entry.getKey().equals("Lab 1. Figures")){
                list.set(0, String.format("%16s", entry.getValue() + " "));
            }
            if(entry.getKey().equals("Phalanxing")){
                list.set(0, String.format("%12s", entry.getValue() + " "));
            }
            if(entry.getKey().equals("Lab 2. War and Peace")){
                list.set(1, String.format("%22s", entry.getValue() + " "));
            }
            if(entry.getKey().equals("Shieldwalling")){
                list.set(1, String.format("%15s", entry.getValue() + " "));
            }
            if(entry.getKey().equals("Lab 3. File Tree")){
                list.set(2, String.format("%18s", entry.getValue() + " "));
            }
            if(entry.getKey().equals("Tercioing")){
                list.set(2, String.format("%11s", entry.getValue() + " "));
            }
            if(phalan && entry.getKey().equals("Wedging")){
                list.set(3, String.format("%9s", entry.getValue() + " "));
            }

            if(phalan && !taskResults.toString().contains("Phalanxing")){
                list.set(0, String.format("%12s", 0 + " "));
            }
            if(phalan && !taskResults.toString().contains("Shieldwalling")){
                list.set(1, String.format("%15s", 0 + " "));
            }
            if(phalan && !taskResults.toString().contains("Tercioing")){
                list.set(2, String.format("%11s", 0 + " "));
            }
            if(phalan && !taskResults.toString().contains("Wedging")){
                list.set(3, String.format("%9s", 0 + " "));
            }

        }

        StringBuilder sb = new StringBuilder();
        for (String s: list) {
            sb.append(s).append("|");
        }
        return sb.toString();
    }

    public Double sumResults(){
        int size = taskResults.size();
        if(taskResults.containsKey("Shieldwalling") || taskResults.containsKey("Phalanxing")){
         size = 4;
        }
        return (double) taskResults.values().stream().reduce(0, Integer::sum)/size;
    }

    public String defineMark(){
        Double d = sumResults();
            if(d<=100 && d>90){
                return "A";
            }
            else if(d<=90 && d>=83){
                return "B";
            }
            else if(d<83 && d>=75){
                return "C";
            }
            else if(d<75 && d>=68){
                return "D";
            }
            else if(d<68 && d>=60){
                return "E";
            }
            else{
                return "F";
            }
        }
    }
