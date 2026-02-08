package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;

import java.util.Locale;

public class Auto_Launch extends State {

    Wheel wheel;
    Intake intake;

    int prevError = 0;
    int stallCount = 0;

    public Auto_Launch(Wheel wheel) {
        this.wheel = wheel;
    }

    public boolean truefalse(){
        int error = Math.abs(wheel.encoderTargetPos - wheel.wheelEncoder.getPosition());
        if (error == prevError) {
            stallCount++;
        } else {
            stallCount = 0;
        }
        prevError = error;
;
        if ( error < Wheel.ERROR || stallCount > 5 ) {
            return true;
        }
        return false;
    }

    public void initializeState(double targetH) {
        this.targetH = targetH;
        wheel.shoot();//run once

    }


    public String readStateData() {
        return "Wheel State";
    }


}
