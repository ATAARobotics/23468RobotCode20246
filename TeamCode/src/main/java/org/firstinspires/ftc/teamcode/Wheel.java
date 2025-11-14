package org.firstinspires.ftc.teamcode;


import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.hardware.CRServo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Wheel {
    public boolean flag;
    //vars: motors and var for current position
    Rev2mDistanceSensor distanceSensor;
    MonkeyCRServo motor;
    Intake intake;

    int count = 0;

    public long milis;


    public Wheel(MonkeyCRServo motor, Rev2mDistanceSensor distanceSensor, Intake intake) {
        // save motors, initialize to desired starting position
        this.motor = motor;
        this.distanceSensor = distanceSensor;
        this.intake = intake;
        //milis = System.currentTimeMillis();
        motor.setTargetPosition(31);
    }

    public void toggleWheelForward() {
        // move the wheel one notch forward
        if (count < 2) {
            motor.setTargetPosition( motor.targetPos - 2746 );
            count += 1;
        }


    }

    public void toggleWheelForwardForce() {
        // move the wheel one notch forward
        motor.setTargetPosition( motor.targetPos - 2730 );
        count += 1;

    }

    public void toggleWheelFireAll() {
        // move the wheel one notch forward
        motor.setTargetPosition( motor.targetPos - 8240 );
        count = 0;

    }


    public boolean isBottomIndexFull() {
        return true;
    }

    public void run(){
        if (distanceSensor.getDistance(DistanceUnit.MM)<40){
            flag = true;
            //milis = System.currentTimeMillis();
        }
        if(flag && distanceSensor.getDistance(DistanceUnit.MM)>80){
            toggleWheelForward();
            flag = false;

        }
        /*
        if (motor.stallMode) {
            intake.runForIncrement();
        }
        */

    }

}
