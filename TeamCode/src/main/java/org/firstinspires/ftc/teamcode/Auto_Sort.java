package org.firstinspires.ftc.teamcode;


public class Auto_Sort extends State {

    Wheel wheel;

    int prevError = 0;
    int stallCount = 0;

    public Auto_Sort(Wheel wheel) {
        this.wheel = wheel;
    }

    public boolean truefalse(){
        return true;
    }

    public void initializeState(double targetH) {
        this.targetH = targetH;
        wheel.order();//run once

    }


    public String readStateData() {
        return "Wheel Order State";
    }


}
