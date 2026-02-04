package org.firstinspires.ftc.teamcode;

import java.util.ArrayList;
import java.util.Arrays;

public class MonkeyTinyIterator {

    public static int NONE = 0;
    public static int GREEN = 1;
    public static int PURPLE = 2;

    public static int PPG = 1;
    public static int GPP = 2;
    public static int PGP = 3;

    public int currM = 0;

    public ArrayList<Integer> states = new ArrayList<>(Arrays.asList(NONE,NONE,NONE));
    public int cursor = 0;

    public MonkeyTinyIterator(){

    }

    public void setMotief(int motief) {
        currM = motief;
    }

    public void next() {
        cursor = (cursor + 2) % 3;
    }

    public void prev(){
        cursor = (cursor + 1) % 3;
    }

    public void set(int val) {
        states.set(cursor, val);
    }

    public void erase() {
        states = new ArrayList<>(Arrays.asList(NONE,NONE,NONE));
        cursor = 0;
    }

    public ArrayList<Integer> getCurrentContents() {
        return new ArrayList<>(Arrays.asList( states.get(cursor),states.get((cursor+1) % 3),states.get((cursor+2) % 3)));
    }

    public int getCount() {
        int ctr = 0;
        for (int i=0; i<3; i++) {
            if (states.get(i) != NONE) {
                ctr++;
            }
        }
        return ctr;
    }

    public int getNumberOfRotatesToOrder() {
        //TODO: given the balls we have what order do we need?

        ArrayList<Integer> translated = new ArrayList<>(Arrays.asList( states.get(cursor),states.get((cursor+1) % 3),states.get((cursor+2) % 3)));

        if ( translated.get(0) == NONE ) {
            if ( translated.get(1) == NONE || translated.get(2) != NONE ) {
                return 2;
            } else {
                return 0;
            }
        } else if (currM == PPG) {
            if (translated.get(0) == GREEN && translated.get(1) == PURPLE && translated.get(2) == PURPLE) {  return 1; }
            else if (translated.get(0) == GREEN && translated.get(1) == GREEN && translated.get(2) == PURPLE) {  return 1; }
            else if (translated.get(0) == GREEN && translated.get(1) == GREEN && translated.get(2) == GREEN) {  return 0; }
            else if (translated.get(0) == PURPLE && translated.get(1) == GREEN && translated.get(2) == GREEN) {  return 0; }
            else if (translated.get(0) == PURPLE && translated.get(1) == PURPLE && translated.get(2) == GREEN) {  return 0; }
            else if (translated.get(0) == PURPLE && translated.get(1) == PURPLE && translated.get(2) == PURPLE) {  return 0; }
            else if (translated.get(0) == PURPLE && translated.get(1) == GREEN && translated.get(2) == PURPLE) {  return 2; }
            else if (translated.get(0) == GREEN && translated.get(1) == PURPLE && translated.get(2) == GREEN) {  return 2; }
            else { return 1; }
        } else if (currM == GPP) {
            if (translated.get(0) == GREEN && translated.get(1) == PURPLE && translated.get(2) == PURPLE) {  return 0; }
            else if (translated.get(0) == GREEN && translated.get(1) == GREEN && translated.get(2) == PURPLE) {  return 0; }
            else if (translated.get(0) == GREEN && translated.get(1) == GREEN && translated.get(2) == GREEN) {  return 0; }
            else if (translated.get(0) == PURPLE && translated.get(1) == GREEN && translated.get(2) == GREEN) {  return 1; }
            else if (translated.get(0) == PURPLE && translated.get(1) == PURPLE && translated.get(2) == GREEN) {  return 2; }
            else if (translated.get(0) == PURPLE && translated.get(1) == PURPLE && translated.get(2) == PURPLE) {  return 0; }
            else if (translated.get(0) == PURPLE && translated.get(1) == GREEN && translated.get(2) == PURPLE) {  return 1; }
            else if (translated.get(0) == GREEN && translated.get(1) == PURPLE && translated.get(2) == GREEN) {  return 0; }
            else { return 1; }
        } else if (currM == PGP) {
            if (translated.get(0) == GREEN && translated.get(1) == PURPLE && translated.get(2) == PURPLE) {  return 2; }
            else if (translated.get(0) == GREEN && translated.get(1) == GREEN && translated.get(2) == PURPLE) {  return 0; }
            else if (translated.get(0) == GREEN && translated.get(1) == GREEN && translated.get(2) == GREEN) {  return 0; }
            else if (translated.get(0) == PURPLE && translated.get(1) == GREEN && translated.get(2) == GREEN) {  return 0; }
            else if (translated.get(0) == PURPLE && translated.get(1) == PURPLE && translated.get(2) == GREEN) {  return 1; }
            else if (translated.get(0) == PURPLE && translated.get(1) == PURPLE && translated.get(2) == PURPLE) {  return 0; }
            else if (translated.get(0) == PURPLE && translated.get(1) == GREEN && translated.get(2) == PURPLE) {  return 0; }
            else if (translated.get(0) == GREEN && translated.get(1) == PURPLE && translated.get(2) == GREEN) {  return 1; }
            else { return 1; }
        }

        return 1;
    }


}
