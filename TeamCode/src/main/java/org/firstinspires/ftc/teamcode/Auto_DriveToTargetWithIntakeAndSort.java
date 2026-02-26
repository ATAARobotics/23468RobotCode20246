package org.firstinspires.ftc.teamcode;

import static java.lang.Math.abs;
import static java.lang.Math.pow;
import static java.lang.Math.round;
import static java.lang.Math.sqrt;

import java.util.ArrayList;


public class Auto_DriveToTargetWithIntakeAndSort extends State {
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

    public boolean canSort = true;
    public Wheel wheel;

    public double rx = 0;
    public double ry = 0;

    public int stallcount = 0;
    public double prevToGo = 9999;

    public double coefficient_p_rot = 1.25;
    public double coefficient_d_rot = 0.93;
    public double prevError = 0.0;

    public ArrayList<Integer> translated;
    public Intake intake;

    public Auto_DriveToTargetWithIntakeAndSort(double targetx, double targety, double power, MonkeyMotor br, MonkeyMotor bl, MonkeyMotor fr, MonkeyMotor fl, Intake intake, Wheel wheel, ArrayList<Integer> translated){
        this.br = br;
        this.bl = bl;
        this.fr = fr;
        this.fl = fl;

        this.wheel = wheel;
        this.intake = intake;
        this.translated = translated;

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
    public void initializeState(double targetH) {
        this.targetH = targetH;
        this.intake.startHold();
    }


    @Override
    public boolean truefalse(){
        if ( ((abs(curx - targetx) < tolerance && abs(cury - targety) < tolerance) || stallcount > 4) && (!wheel.amISorting && canSort) ){
            bl.set(0);
            br.set(0);
            fl.set(0);
            fl.set(0);
            this.intake.stopHold();
            return true;
        }
        return false;
    }

    @Override
    public void action(){

        if (canSort && wheel.count == 3) {
            canSort = false;
            wheel.orderWithPredefinedSet(translated);
        }

        dx = targetx - curx;
        dy = targety - cury;

        totaldist = sqrt( pow(dx, 2) + pow(dy, 2) );

        ry = (Math.cos(curh)*dx + Math.sin(curh)*dy) * -1 / (totaldist);
        rx = (Math.sin(curh)*dx + -1 * Math.cos(curh)*dy) / (totaldist);

        if ( round( totaldist ) == prevToGo) {
            stallcount++;
        } else {
            stallcount = 0;
        }
        prevToGo = round( totaldist );

        double error = ((curh - targetH + Math.PI) % (2 * Math.PI)) - Math.PI;
        double test_auto = Math.min(
                (coefficient_p_rot * 1 * error) + (coefficient_d_rot * 1.1 * (error - prevError))
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
        return String.format("dx, %f.1 | dy, %f.1 |  dist, %f.1 | rx, %f.1 | ry, %f.1", dx, dy, totaldist, rx, ry);
    }
}
