package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

import java.util.List;
import java.util.Locale;


public class Auto_FaceAHeadingWithCamera extends State {
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
    public double coefficient_p_rot = 1.25;
    public double coefficient_d_rot = 0.93;
    public double prevError = 0.0;

    public int stallcount = 0;
    public double prevToGo = 9999;

    public double error = 9999;

    public MonkeyAprilTagCamera cam;

    public Auto_FaceAHeadingWithCamera(double targeth, double power, MonkeyMotor br, MonkeyMotor bl, MonkeyMotor fr, MonkeyMotor fl, MonkeyAprilTagCamera cam){
        this.br = br;
        this.bl = bl;
        this.fr = fr;
        this.fl = fl;

        this.speed = power;

        this.cam = cam;

        this.targeth = targeth;
    }

    @Override
    public void setCurrentLocationAndRotation (double x_mm, double y_mm, double heading_rad){
        this.curh = heading_rad;
    }

    @Override
    public void initializeState(double targetH) {
        this.starth = this.curh;
    }


    @Override
    public boolean truefalse(){
        //double diff = ((curh - targeth + Math.PI) % (2 * Math.PI)) - Math.PI;
        // This keeps diff in range [-180, 180]

        if ( (Math.abs(error) < tolerance) || stallcount > 3){
                //&& (this.curh - this.starth) > 0.001 ) {
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
        error = 999;
        //double error = ((currentHeading - targetHeading + Math.PI) % (2 * Math.PI)) - Math.PI;
        List<AprilTagDetection> currentDetections = this.cam.getDetections();
        if (!currentDetections.isEmpty()) {
            for (AprilTagDetection detection : currentDetections) {
                if (detection.metadata != null) {
                    if (detection.id == 20 || detection.id == 24  ) {
                        error = Math.toRadians(detection.ftcPose.yaw);
                        break;

                    }
                }
            }
        }
        if (error == 999) {
            error = Math.atan2(Math.sin(curh - targeth), Math.cos(curh - targeth));
        }

        double test_auto = Math.min(
                (coefficient_p_rot * 1 * error) + (coefficient_d_rot * 1.2 * (error - prevError))
                , 1);
        prevError = error;

        double adjustment = test_auto;

        fr.set_pd_rotate(speed, error);
        fl.set_pd_rotate(-speed, error);
        br.set_pd_rotate(speed, error);
        bl.set_pd_rotate(speed, error);
        /*
        diff = ((curh - targeth + Math.PI) % (2 * Math.PI)) - Math.PI;

        double adjustment = speed;

        double roundedDelta = 0.01 * floor( (curh - targetH) * 100.0);
        if (roundedDelta == prevToGo) {
            stallcount++;
        } else {
            stallcount = 0;
        }
        prevToGo = roundedDelta;

        fr.set_pd_rotate(adjustment, diff);
        fl.set_pd_rotate(adjustment, diff);
        br.set_pd_rotate(-adjustment, diff);
        bl.set_pd_rotate(adjustment, diff);
        */
    }

    @Override
    public String readStateData() {
        return String.format(new Locale("en-us"),"CH: %f.1 | TH: %f.1 | diff: %f.1", curh, targeth, diff);
    }

    @Override
    public double stop() {
        return targeth;
    }
}
