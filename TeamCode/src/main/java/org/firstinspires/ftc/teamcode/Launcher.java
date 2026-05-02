package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.hardware.Servo;

public class Launcher {

    public boolean fallback = false;

    //fallback mode
    public static double IDLE_SPEED = 0.0;
    /*
    public static int SLOW_60 = 3250;//60 degree ramp
    public static int MID_60 = 3800;//60 degree ramp
    public static int FAST_60 = 4530;//60 degree ramp

    public static int SLOW_45 = 4530;//45 degree ramp
    public static int MID_45 = 4530;//45 degree ramp
    public static int FAST_45 = 4530;//45 degree ramp

    public int SLOW_SPEED = SLOW_60;
    public int MID_SPEED = MID_60;
    public int FAST_SPEED = FAST_60;
    */
    public int SLOW_SPEED =  3130;
    public int MID_SPEED = 3650;
    public int FAST_SPEED = 4150;

    public static int MODE_STOP = 0;
    //public static int MODE_IDLE = 1;
    public static int MODE_SLOW = 2;
    public static int MODE_MID = 3;
    public static int MODE_FAST = 4;

    //vars: adjustment factor, motors and parameters for slow/medium/fast velocities and var for current mode
    public MonkeyVelocityMotor motor1;
    public MonkeyVelocityMotor motor2;

    public Servo rampServo;

    public int target = 0;
    public int adjustment = 10;
    private int mode = MODE_STOP;
    public boolean idle = false;

    public int override = 0;


    public Launcher(MonkeyVelocityMotor motor1, MonkeyVelocityMotor motor2, Servo rampServo) {
        // save motors
        this.motor1 = motor1;
        this.motor2 = motor2;
        this.rampServo = rampServo;
    }

    public void fallbackLauncher() {
        this.fallback = true;
    }

    public void adjustLauncherFaster() {
        // set adjustment to be faster by 0.01
        adjustment += 100;
    }

    public void adjustLauncherSlower() {
        //set adjustment to be slower by 0.01
        adjustment -= 100;
    }

    public void setLauncherSlow() {
        // using the parameter and the adjustment factor set to slow mode
        mode = MODE_SLOW;
        Servo60();
    }

    public void setLauncherMedium() {
        //using the parameter and the adjustment factor set to medium mode
        mode = MODE_MID;
        Servo60();
    }

    public void setLauncherFast() {
        //using the parameter and the adjustment factor set to fast (Sniper Mode)
        mode = MODE_FAST;
        Servo45();
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
        override = (int)distance/77;
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
                Servo60();
            } else if (mode == MODE_MID) {
                target = MID_SPEED;
                Servo60();
            } else if (mode == MODE_FAST) {
                target = FAST_SPEED;
                Servo45();
            } else {
                target = 0;
            }

            motor1.set((target + adjustment));
            motor2.set((target + adjustment));

        }

        motor1.run();
        motor2.run();

    }
    public void Servo60() {
        rampServo.setPosition(0.3);
        //SLOW_SPEED = SLOW_60;
        //MID_SPEED = MID_60;
        //FAST_SPEED = FAST_60;
    }
    public void Servo45() {
        rampServo.setPosition(0.7);
        //SLOW_SPEED = SLOW_45;
        //MID_SPEED = MID_45;
        //FAST_SPEED = FAST_45;
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
