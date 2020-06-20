package com.example.note.controller;

import com.example.note.model.pojo.Note;

import java.util.Comparator;

public class Compare_tag implements Comparator <Object> {
    @Override
    public int compare(Object o1, Object o2) {
        Note note1 = (Note) o1;
        Note note2 = (Note) o2;
        if(note1.getTag()<note2.getTag())
            return -1;
        else if (note1.getTag()>note2.getTag())
            return 1;
        return 0;
    }
}
