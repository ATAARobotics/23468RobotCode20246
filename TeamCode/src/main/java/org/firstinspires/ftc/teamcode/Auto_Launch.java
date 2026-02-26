package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;

import java.util.Locale;

public class Auto_Launch extends State {

    Wheel wheel;
    Intake intake;

    int stallCount = 0;
    boolean canShoot = true;


    public Auto_Launch(Wheel wheel) {
        this.wheel = wheel;
    }

    public boolean truefalse( ){
        if ( (!wheel.isShooting && !canShoot) || stallCount > 5 ) {
            return true;
        }
        return false;
    }

    @Override
    public void action() {
        if (canShoot && !wheel.amISorting) {
            wheel.shootWithCooldown();//run once
            canShoot = false;
        }
    }


    public String readStateData() {
        return "Wheel State";
    }


}
