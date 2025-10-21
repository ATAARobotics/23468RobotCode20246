package org.firstinspires.ftc.teamcode;


public class State {

    public double targetH;

    public State() {
    }

    public boolean truefalse(){
        return true;
    }

    public void action(){

    }

    public void setCurrentLocationAndRotation (double x_mm, double y_mm, double heading_rad){
    }

    public void initializeState(double targetH) {
        this.targetH = targetH;
    }

    public double stop() {
        return targetH;
    }

    public String readStateData() {
        return "default state";
    }


}
