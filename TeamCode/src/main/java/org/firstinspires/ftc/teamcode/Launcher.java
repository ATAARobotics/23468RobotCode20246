package org.firstinspires.ftc.teamcode;


public class Launcher {

    public boolean fallback = false;

    //fallback mode
    public static double IDLE_SPEED = 0.0;
    public static double SLOW_SPEED = 0.69;
    public static double MID_SPEED = 0.83;
    public static double FAST_SPEED = 0.92;

    public static int MODE_STOP = 0;
    //public static int MODE_IDLE = 1;
    public static int MODE_SLOW = 2;
    public static int MODE_MID = 3;
    public static int MODE_FAST = 4;

    //vars: adjustment factor, motors and parameters for slow/medium/fast velocities and var for current mode
    public MonkeyVelocityMotor motor1;
    public MonkeyVelocityMotor motor2;

    public double target = 0.0;
    public double adjustment = 0.0;
    private int mode = MODE_STOP;
    public boolean idle = false;

    public double override = 0;


    public Launcher(MonkeyVelocityMotor motor1, MonkeyVelocityMotor motor2) {
        // save motors
        this.motor1 = motor1;
        this.motor2 = motor2;
    }

    public void fallbackLauncher() {
        this.fallback = true;
    }

    public void adjustLauncherFaster() {
        // set adjustment to be faster by 0.01
        adjustment += 0.01;
    }

    public void adjustLauncherSlower() {
        //set adjustment to be slower by 0.01
        adjustment -= 0.01;
    }

    public void setLauncherSlow() {
        // using the parameter and the adjustment factor set to slow mode
        mode = MODE_SLOW;
    }

    public void setLauncherMedium() {
        //using the parameter and the adjustment factor set to medium mode
        mode = MODE_MID;
    }

    public void setLauncherFast() {
        //using the parameter and the adjustment factor set to fast (Sniper Mode)
        mode = MODE_FAST;
    }

    public void setLauncherIdle() {
        //using the parameter and the adjustment factor set to fast (Sniper Mode)
        idle = true;
    }

    public void setLauncherNotIdle() {
        //using the parameter and the adjustment factor set to fast (Sniper Mode)
        idle = false;
    }

    public void setLauncherStop() {
        mode = MODE_STOP;
    }

    public void incrementModeFaster() {
        if (mode == MODE_STOP) {
            setLauncherSlow();
        } else if (mode == MODE_SLOW) {
            setLauncherMedium();
        } else {
            setLauncherFast();
        }
    }

    public void incrementModeSlower() {
        if (mode == MODE_FAST) {
            setLauncherMedium();
        } else {
            setLauncherSlow();
        }
    }

    public void setToDistance (double distance) {
        override = distance/77;
    }

    public void manualControl() {
        override = 0;
    }

    public void run() {
        if ( mode == MODE_STOP) {
            motor1.set(0);
            motor2.set(0);
        } else if (override > 0) {
            motor1.set(override);
            motor2.set(override);
        } else {

            if (mode == MODE_SLOW) {
                target = SLOW_SPEED;
            } else if (mode == MODE_MID) {
                target = MID_SPEED;
            } else if (mode == MODE_FAST) {
                target = FAST_SPEED;
            } else {
                target = 0.0;
            }

            motor1.set(target + adjustment);
            motor2.set(target + adjustment);

        }

    }

    public String getMode() {
        if (mode == MODE_SLOW) {
            return "Slow";
        } else if (mode == MODE_MID) {
            return "Medium";
        } else if (mode == MODE_FAST) {
            return "Fast";
        }
        return "Stop";

    }
}
