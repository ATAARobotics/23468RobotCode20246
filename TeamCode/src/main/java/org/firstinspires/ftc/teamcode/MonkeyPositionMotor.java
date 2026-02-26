package org.firstinspires.ftc.teamcode;

import static java.lang.Math.max;
import static java.lang.Math.min;

import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class MonkeyPositionMotor extends Motor {

    public static int POWER = 0;
    public static int POSITION = 1;

    public static int FORWARD = 1;
    public static int REVERSE = -1;

    public static int TOLERANCE = 10;


    //public double targetPosition = 321;


    MonkeyPositionMotor(HardwareMap hardwareMap, String Name) {//, int direction, int mode) {
        super(hardwareMap, Name);
        //this.direction = direction;
        //this.mode = mode;
        super.resetEncoder();
        super.setRunMode(RunMode.PositionControl);
        super.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);

        super.setPositionTolerance(TOLERANCE);

        super.setPositionCoefficient( 0.02 );
    }


    public void run(){
        if(super.atTargetPosition()){
            super.set(0.01);
        }
        else{
            super.set(0.5);
        }


    }
        /*
        if(super.getCurrentPosition() < targetPosition-TOLERANCE){
            super.set(0.25);
        }
        else if (super.getCurrentPosition() > targetPosition + TOLERANCE) {
            super.set(-0.25);

        }
        else {
            super.set(0);
        }

    }

     */
}

