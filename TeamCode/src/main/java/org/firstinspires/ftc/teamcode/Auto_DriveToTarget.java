package org.firstinspires.ftc.teamcode;

import static java.lang.Math.abs;
import static java.lang.Math.pow;
import static java.lang.Math.round;
import static java.lang.Math.sqrt;

import com.arcrobotics.ftclib.hardware.motors.Motor;


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

    //public int stallcount = 0;
    public int prevToGo = 9999;

    public double coefficient_p_rot = 1.25;
    public double coefficient_d_rot = 0.93;
    public double prevError = 0.0;

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
    public void initializeState(double targetH) {
        this.targetH = targetH;
        this.stallcount = 0 ;
    }


    @Override
    public void setCurrentLocationAndRotation (double x_mm, double y_mm, double heading_rad){
        this.curx = x_mm;
        this.cury = y_mm;
        this.curh = heading_rad;
    }


    @Override
    public boolean truefalse(){
        if (  (abs(curx - targetx) < tolerance && abs(cury - targety) < tolerance) || this.stallcount > 10 ){
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

        this.dx = targetx - curx;
        this.dy = targety - cury;

        totaldist = sqrt( pow(dx, 2) + pow(dy, 2) );

        ry = (Math.cos(curh)*dx + Math.sin(curh)*dy) * -1 / (totaldist);
        rx = (Math.sin(curh)*dx + -1 * Math.cos(curh)*dy) / (totaldist);

        if ( (int) totaldist == prevToGo) {
            this.stallcount++;
        } else {
            this.stallcount = 0;
        }
        prevToGo = (int) totaldist ;

        double error = Math.atan2(Math.sin(curh - targetH), Math.cos(curh - targetH));
        double test_auto = Math.min(
                (coefficient_p_rot * 1 * error) + (coefficient_d_rot * 1.2 * (error - prevError))
                , 1);
        prevError = error;
        double adjustment = test_auto * 0.4 ;


        double denominator = Math.max(Math.abs(ry) + Math.abs(rx), 1);

        fr.set_pd((ry + rx ) / denominator * speed,adjustment * rotScale, totaldist);
        fl.set_pd((ry - rx ) / denominator * speed, -adjustment * rotScale, totaldist);
        br.set_pd((ry - rx ) / denominator * speed, adjustment * rotScale, totaldist);
        bl.set_pd((-ry - rx ) / denominator * speed,adjustment * rotScale, totaldist);

    }

    @Override
    public String readStateData() {
        return String.format("drive forward; dx, %f.1 | dy, %f.1 |  dist, %d | rx, %f.1 | ry, %f.1 | prevtogo, %d", dx, dy, (int) totaldist, rx, ry, prevToGo);
    }
}
