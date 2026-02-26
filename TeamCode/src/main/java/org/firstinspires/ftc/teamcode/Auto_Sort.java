package org.firstinspires.ftc.teamcode;


import java.util.ArrayList;

public class Auto_Sort extends State {

    Wheel wheel;
    ArrayList<Integer> translated;

    boolean canSort = true;

    public Auto_Sort(Wheel wheel, ArrayList<Integer> translated) {
        this.wheel = wheel;
        this.translated = translated;
    }

    public boolean truefalse(){
        return true;
    }

    public void initializeState(double targetH) {
        this.targetH = targetH;
        wheel.itr.cursor = 0;
        wheel.orderWithPredefinedSet(translated);//run once


    }


    public String readStateData() {
        return "Wheel Order State";
    }


}
