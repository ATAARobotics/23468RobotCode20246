package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;

import java.util.Locale;

public class Auto_Launch extends State {

    Wheel wheel;
    Intake intake;

    int prevError = 0;
    int stallCount = 0;

    public Auto_Launch(Wheel wheel, Intake intake) {
        this.wheel = wheel;
        this.intake = intake;
    }

    public boolean truefalse(){
        int error = wheel.motor.targetPos - wheel.motor.encoder.getCurrentPosition();
        if (error == prevError) {
            stallCount++;
        } else {
            stallCount = 0;
        }
        prevError = error;
;
        if ( error > -50 || stallCount > 15 ) {
            return true;
        }
        return false;
    }

    public void initializeState(double targetH) {
        this.targetH = targetH;
        intake.runForIncrement();
        wheel.toggleWheelFireAll();//run once

    }


    public String readStateData() {
        return String.format(new Locale("en-us"),"target: %d | pos: %d | error: %d | stall: %d", wheel.motor.targetPos, wheel.motor.encoder.getCurrentPosition(), wheel.motor.targetPos - wheel.motor.encoder.getCurrentPosition(), stallCount );
    }


}
