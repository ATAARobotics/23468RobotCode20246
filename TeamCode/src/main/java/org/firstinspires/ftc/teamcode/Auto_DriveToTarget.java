package org.firstinspires.ftc.teamcode;

import static java.lang.Math.abs;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.hardware.DcMotor;


public class Auto_DriveToTarget extends State {
    public MonkeyMotor br;
    public MonkeyMotor bl;
    public MonkeyMotor fr;
    public MonkeyMotor fl;

    public double targetx;
    public double targety;

    public double curx;
    public double cury;
    public double curh;

    public double tolerance = 20;
    double coefficient = 1.1;
    double rotScale = 1;
    double speed = 0.5;

    public double dx=0;
    public double dy=0;
    public double totaldist = 0;

    public double rx = 0;
    public double ry = 0;

    public Auto_DriveToTarget(double targetx, double targety, double power, MonkeyMotor br, MonkeyMotor bl, MonkeyMotor fr, MonkeyMotor fl){
        this.br = br;
        this.bl = bl;
        this.fr = fr;
        this.fl = fl;

        this.speed = power;

        this.targetx = targetx;
        this.targety = targety;
    }


    @Override
    public void setCurrentLocationAndRotation (double x_mm, double y_mm, double heading_rad){
        this.curx = x_mm;
        this.cury = y_mm;
        this.curh = heading_rad;
    }


    @Override
    public boolean truefalse(){
        if ( abs(curx - targetx) < tolerance && abs(cury - targety) < tolerance ){
            bl.set(0);
            br.set(0);
            fl.set(0);
            fl.set(0);
            return true;
        }
        return false;
    }

    @Override
    public void action(){

        dx = targetx - curx;
        dy = targety - cury;

        totaldist = abs(dx) + abs(dy); //sqrt( pow(dx, 2) + pow(dy, 2) );

        ry = (Math.cos(curh)*dx + Math.sin(curh)*dy) / (totaldist);
        rx = (Math.sin(curh)*dx + -1*Math.cos(curh)*dy) / (totaldist);


        double adjustment = coefficient*(curh - targetH);
        double denominator = Math.max(Math.abs(ry) + Math.abs(rx), 1);

        fr.set_pd((ry - rx ) / denominator * speed,-adjustment * rotScale, totaldist);
        fl.set_pd((ry + rx ) / denominator * speed, adjustment * rotScale, totaldist);
        br.set_pd((-ry - rx ) / denominator * speed, adjustment * rotScale, totaldist);
        bl.set_pd((ry - rx ) / denominator * speed,adjustment * rotScale, totaldist);

    }

    @Override
    public String readStateData() {
        return String.format("dx, %f.1 | dy, %f.1 |  dist, %f.1 | rx, %f.1 | ry, %f.1", dx, dy, totaldist, rx, ry);
    }
}
