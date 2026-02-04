package org.firstinspires.ftc.teamcode;


import com.arcrobotics.ftclib.geometry.Transform2d;
import com.arcrobotics.ftclib.geometry.Vector2d;

import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

public class Chassis {
    //PARAMETERS:
    double sped = 1.0;
    double rxSped = 0.75;
    double keepHeadingCoefficient = 1.4;

    public MonkeyMotor br;
    public MonkeyMotor bl;
    public MonkeyMotor fr;
    public MonkeyMotor fl;

    public GoBildaPinpointDriver odo;

    public double targetHeading = 0.0;
    public double currentHeading = 0.0;

    public double x_prev = 0;
    public double y_prev = 0;

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

    public void averageDirectionOverSecond() {



    }

    public void DRIVE(double forwardStick, double strafeStick, double rotateStick, boolean turbo) {
        long t = System.currentTimeMillis();


        if (!turbo) {
            forwardStick = forwardStick / 1.3;
            strafeStick = strafeStick * 1.1 / 1.3;
            rotateStick = rotateStick / 1.6;
        }

        if (rotateStick == 0 ) {
            //keep heading as 0
            rotateStick = keepHeadingCoefficient*(currentHeading - targetHeading);
            if ( Math.abs(rotateStick) < 0.02 ) {
                rotateStick = 0;
            }
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
