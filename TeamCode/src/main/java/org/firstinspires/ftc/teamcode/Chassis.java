package org.firstinspires.ftc.teamcode;


import static java.lang.Math.floor;

import com.arcrobotics.ftclib.geometry.Transform2d;
import com.arcrobotics.ftclib.geometry.Vector2d;

import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

public class Chassis {
    //PARAMETERS:
    double sped = 1.0;
    double rxSped = 0.8;
    double keepHeadingCoefficient = 1.4;

    public MonkeyMotor br;
    public MonkeyMotor bl;
    public MonkeyMotor fr;
    public MonkeyMotor fl;

    public GoBildaPinpointDriver odo;

    public double targetHeading = 0.0;
    public double currentHeading = 0.0;

    public double coefficient_p_rot = 1.25;
    public double coefficient_d_rot = 0.93;
    public double prevError = 0.0;

    public Chassis(MonkeyMotor fr, MonkeyMotor fl, MonkeyMotor br, MonkeyMotor bl) {
        // save motors
        this.fr = fr;
        this.fl = fl;
        this.br = br;
        this.bl = bl;

        //this.odo = odo;


    }

    public void setHeading(double heading) {
        this.currentHeading = heading;
    }

    public void intakeDrive(double rotateStick) {
        this.DRIVE(-0.4, 0, rotateStick, true); //up is negitive on the controller
    }
    public void AutoAlign(double heading, double yaw) {
        this.currentHeading = heading;
        double idk_twin = 67676767;
        double tolerance = -0.001;
        //fr.set_accelerate((forwardStick + strafeStick + rotateStick * rxSped) / denominator * sped);
        //fl.set_accelerate((forwardStick - strafeStick - rotateStick * rxSped) / denominator * sped);
        //br.set_accelerate((forwardStick - strafeStick + rotateStick * rxSped) / denominator * sped);
        //
        // bl.set_accelerate ((-forwardStick - strafeStick + rotateStick * rxSped) / denominator * sped);
    }

    public void stop (){
        fl.set(0);
        fr.set(0);
        bl.set(0);
        br.set(0);
    }

    public void turnTowards(double yaw){

        //double error = ((currentHeading - targetHeading + Math.PI) % (2 * Math.PI)) - Math.PI;
        //double error = Math.atan2(Math.sin(currentHeading - targetHeading), Math.cos(currentHeading - targetHeading));


        double error = Math.atan2(Math.sin(currentHeading - yaw), Math.cos(currentHeading - yaw));

        if (Math.abs(error) > 0.02) {
            fr.set_pd_rotate(-0.75, error);
            fl.set_pd_rotate(0.75, error);
            br.set_pd_rotate(-0.75, error);
            bl.set_pd_rotate(-0.75, error);
        } else {
            this.stop();
        }


    }

    public void DRIVE(double forwardStick, double strafeStick, double rotateStick, boolean turbo) {
        //long t = System.currentTimeMillis();

        if (!turbo) {
            forwardStick = forwardStick / 1.2;
            strafeStick = strafeStick * 1.2 / 1.2;
            rotateStick = rotateStick / 1.4;
        }

        if (Math.abs(rotateStick) < 0.01 ) {
            double error = Math.atan2(Math.sin(currentHeading - targetHeading), Math.cos(currentHeading - targetHeading ));
            //double error = ((currentHeading - targetHeading + Math.PI) % (2 * Math.PI)) - Math.PI;

            double test_auto = Math.min(
                    (coefficient_p_rot * 1 * error) + (coefficient_d_rot * 1.1 * (error - prevError))
                    , 1);
            prevError = error;

            rotateStick = test_auto * 0.4;
        } else {
            targetHeading = currentHeading;
        }

        double denominator = Math.max(Math.abs(forwardStick) + Math.abs(strafeStick) + Math.abs(rotateStick), 1);

        fr.set_accelerate((forwardStick + strafeStick + rotateStick * rxSped) / denominator * sped);
        fl.set_accelerate((forwardStick - strafeStick - rotateStick * rxSped) / denominator * sped);
        br.set_accelerate((forwardStick - strafeStick + rotateStick * rxSped) / denominator * sped);
        bl.set_accelerate ((-forwardStick - strafeStick + rotateStick * rxSped) / denominator * sped);
    }


}
