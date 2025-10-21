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
    public double starth;

    public double tolerance = 0.05;
    double diff = 1;
    double speed = 0.5;

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
    public void initializeState() {
        this.starth = this.curh;
    }


    @Override
    public boolean truefalse(){
        //double diff = ((curh - targeth + Math.PI) % (2 * Math.PI)) - Math.PI;
        // This keeps diff in range [-180, 180]



        if (Math.abs(((curh - targeth + Math.PI) % (2 * Math.PI)) - Math.PI) < tolerance
                && (this.curh - this.starth) > 0.1 ) {
            bl.set(0);
            br.set(0);
            fl.set(0);
            fr.set(0);
            return true;
        }
        return false;
    }

    @Override
    public void action(){
        diff = ((curh - targeth + Math.PI) % (2 * Math.PI)) - Math.PI;

        double adjustment = speed;

        fr.set_pd_rotate(-adjustment, diff);
        fl.set_pd_rotate(adjustment, diff);
        br.set_pd_rotate(adjustment, diff);
        bl.set_pd_rotate(adjustment, diff);

    }

    @Override
    public String readStateData() {
        return String.format(new Locale("en-us"),"CH: %f.1 | TH: %f.1 | diff: %f.1", curh, targeth, diff);
    }
}
