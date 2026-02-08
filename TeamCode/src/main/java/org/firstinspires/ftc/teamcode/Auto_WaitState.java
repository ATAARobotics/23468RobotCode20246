package org.firstinspires.ftc.teamcode;


public class Auto_WaitState extends State{
    double timems;
    double movingTimeInMillis = System.currentTimeMillis();

    public Auto_WaitState(double timems) {
        this.timems = timems;


    }

    public boolean truefalse(){
        double elapsedTimeInMillis = System.currentTimeMillis();
        double currentTimInMillis = elapsedTimeInMillis - movingTimeInMillis;
        if (currentTimInMillis >= timems){
            return true;
        } else {

            return false;
        }

    }

    public void initializeState(double targetH) {
        movingTimeInMillis = System.currentTimeMillis();
        super.initializeState(targetH);
    }


    public String readStateData() {
        return "wait state";
    }


}
