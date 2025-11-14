package org.firstinspires.ftc.teamcode;


public class Chassis {
    //PARAMETERS:
    double sped = 1.0;
    double rxSped = 0.75;
    double keepHeadingCoefficient = 1.4;

    public MonkeyMotor br;
    public MonkeyMotor bl;
    public MonkeyMotor fr;
    public MonkeyMotor fl;

    public double targetHeading = 0.0;
    public double currentHeading = 0.0;

    public long duration = 99999;
    public long lastTime = 0;



    public Chassis(MonkeyMotor fr, MonkeyMotor fl, MonkeyMotor br, MonkeyMotor bl) {
        // save motors
        this.fr = fr;
        this.fl = fl;
        this.br = br;
        this.bl = bl;


    }

    public void setHeading(double heading) {
        this.currentHeading = heading;
    }

    public void intakeDrive(double rotateStick) {
        this.DRIVE(-0.4, 0, rotateStick, true); //up is negitive on the controller
    }

    public void DRIVE(double forwardStick, double strafeStick, double rotateStick, boolean turbo) {
        long t = System.currentTimeMillis();


        if (!turbo) {
            forwardStick = forwardStick / 1.3;
            strafeStick = strafeStick * 1.1 / 1.3;
            rotateStick = rotateStick / 1.3;
        }
        double lx = rotateStick;

        if (rotateStick == 0 ) {
            //keep heading as 0
            if (duration > 300) {
                lx = keepHeadingCoefficient*(currentHeading - targetHeading);
                if ( Math.abs(lx) < 0.02 ) {
                    lx = 0;
                }
            } else {

                duration = t - lastTime;
                lastTime = t;
                targetHeading = currentHeading;
            }

        } else {
            duration = 0;
            targetHeading = currentHeading;
        }


        double denominator = Math.max(Math.abs(forwardStick) + Math.abs(strafeStick) + Math.abs(lx), 1);

        fr.set_accelerate((forwardStick + strafeStick + lx * rxSped) / denominator * sped);
        fl.set_accelerate((-forwardStick + strafeStick + lx * rxSped) / denominator * sped);
        br.set_accelerate((-forwardStick + strafeStick - lx * rxSped) / denominator * sped);
        bl.set_accelerate ((-forwardStick - strafeStick + lx * rxSped) / denominator * sped);
    }


}
