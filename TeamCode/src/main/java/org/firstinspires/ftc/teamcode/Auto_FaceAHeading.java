package org.firstinspires.ftc.teamcode;

import static java.lang.Math.abs;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

import java.util.Locale;


public class Auto_FaceAHeading extends State {
    public MonkeyMotor br;
    public MonkeyMotor bl;
    public MonkeyMotor fr;
    public MonkeyMotor fl;

    public double targeth;


    public double curh;

    public double tolerance = 0.05;
    double coefficient = 1;
    double rotScale = 1;
    double speed = 0.5;

    public double dx=0;
    public double dy=0;
    public double totaldist = 0;

    public double rx = 0;
    public double ry = 0;

    public Auto_FaceAHeading(double targeth, double power, MonkeyMotor br, MonkeyMotor bl, MonkeyMotor fr, MonkeyMotor fl){
        this.br = br;
        this.bl = bl;
        this.fr = fr;
        this.fl = fl;

        this.speed = power;

        this.targeth = targeth;
    }

    @Override
    public void setCurrentLocationAndRotation (double x_mm, double y_mm, double heading_rad){
        this.curh = heading_rad;
    }


    @Override
    public boolean truefalse(){
        //double diff = ((curh - targeth + Math.PI) % (2 * Math.PI)) - Math.PI;
        // This keeps diff in range [-180, 180]



        if (Math.abs((curh - targeth) % 2 * Math.PI) < tolerance) {
            bl.set(0);
            br.set(0);
            fl.set(0);
            fr.set(0); // fixed duplicate 'fl'
            return true;
        }
        return false;
    }

    @Override
    public void action(){
        double diff = ((curh - targeth + Math.PI) % (2 * Math.PI)) - Math.PI;

        double adjustment = speed*(diff);
        adjustment = 0.33 ;

        fr.set(-adjustment);
        fl.set(adjustment);
        br.set(-adjustment);
        bl.set(-adjustment);

    }

    @Override
    public String readStateData() {
        return String.format(new Locale("en-us"),"Current Heading, %f.1 | Target Heading, %f.1", curh, targeth);
    }
}
