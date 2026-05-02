package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;

import java.util.Locale;

public class Auto_Launch extends State {

    Wheel wheel;
    Intake intake;

    int stallCount = 0;
    boolean canShoot = true;
    boolean wheelShootPrev;


    public Auto_Launch(Wheel wheel) {
        this.wheel = wheel;
    }

    public boolean truefalse( ){
        if ( System.currentTimeMillis() - wheel.lastSortTime > 500 && !wheel.isShooting && !wheel.amISorting && Math.abs(wheel.gen_motor.getCurrentPosition() - wheel.encoderTargetPos) < MonkeyPositionMotorv2.TOLERANCE && wheel.CanTurn ) {
            return true;
        }
        return false;
    }

    @Override
    public void initializeState(double targetH) {
        this.targetH = targetH;
        wheel.shootWithCooldown();


    }


    public String readStateData() {
        return "Wheel State";
    }


}
