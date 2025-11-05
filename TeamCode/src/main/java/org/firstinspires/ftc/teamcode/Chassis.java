package org.firstinspires.ftc.teamcode;


public class Chassis {
    //PARAMETERS:
    double sped = 1.0;
    double rxSped = 0.75;
    double keepHeadingCoefficient = 0.8;

    public MonkeyMotor br;
    public MonkeyMotor bl;
    public MonkeyMotor fr;
    public MonkeyMotor fl;

    public double targetHeading = 0.0;
    public double currentHeading = 0.0;



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

    public void DRIVE(double forwardStick, double strafeStick, double rotateStick) {

        double lx = rotateStick;

        if (rotateStick == 0) {
            //keep heading as 0
            lx = keepHeadingCoefficient*(currentHeading - targetHeading);
        } else {
            targetHeading = currentHeading;
        }

        double denominator = Math.max(Math.abs(forwardStick) + Math.abs(strafeStick) + Math.abs(lx), 1);

        fr.set((forwardStick - strafeStick - lx * rxSped) / denominator * sped);
        fl.set((forwardStick + strafeStick + lx * rxSped) / denominator * sped);
        br.set((-forwardStick - strafeStick + lx * rxSped) / denominator * sped);
        bl.set((forwardStick - strafeStick + lx * rxSped) / denominator * sped);
    }


}
