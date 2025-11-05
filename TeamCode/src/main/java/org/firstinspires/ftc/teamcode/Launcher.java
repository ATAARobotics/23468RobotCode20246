package org.firstinspires.ftc.teamcode;


public class Launcher {

    //vars: adjustment factor, motors and parameters for slow/medium/fast velocities and var for current mode


    public Launcher(MonkeyVelocityMotor motor1, MonkeyVelocityMotor motor2) {
        // save motors
    }

    public void adjustLauncherFaster() {
        // set adjustment to be faster by 0.01
    }

    public void adjustLauncherSlower() {
        //set adjustment to be slower by 0.01
    }

    public void setLauncherIdle() {
        //set the launcher to idle
    }

    public void setLauncherSlow() {
        // using the parameter and the adjustment factor set to slow mode
    }

    public void setLauncherMedium() {
        //using the parameter and the adjustment factor set to medium mode
    }

    public void setLauncherFast() {
        //using the parameter and the adjustment factor set to fast (Sniper Mode)
    }

    public void preventSlowdown() {
        //based on the mode idle unless motor goes below a certain percentage of speed to prevent the wheel from taking too long to spin up while saving battery
    }
}
