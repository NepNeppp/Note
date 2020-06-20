package com.example.note.controller;

import com.example.note.model.pojo.Note;

import java.util.Comparator;

public class Compare_time implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        Note note1 = (Note) o1;
        Note note2 = (Note) o2;
        if(note1.getTime().compareTo(note2.getTime())<0)
            return 1;
        else if (note1.getTime().compareTo(note2.getTime())>0)
            return -1;
        return 0;
    }
}
