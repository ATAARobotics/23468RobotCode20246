package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class MonkeyPositionMotorv2 extends Motor {

    public static int TOLERANCE = 8;


    public int targetPosition = 0;
    public boolean isSorting = false;
    public boolean isShooting = false;

    MonkeyPositionMotorv2(HardwareMap hardwareMap, String Name) {//, int direction, int mode) {
        super(hardwareMap, Name);
        //this.direction = direction;
        //this.mode = mode;
        super.resetEncoder();
        super.setRunMode(RunMode.RawPower);
        super.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);

        //super.setPositionTolerance(TOLERANCE);

        //super.setPositionCoefficient( 0.02 );
    }

    public void setTargetPosition(int targetPosition) {
        this.targetPosition = targetPosition;
    }

    @Override
    public boolean atTargetPosition() {
        return Math.abs(super.getCurrentPosition() - targetPosition) < TOLERANCE;
    }

    public void setIsSorting(boolean isSorting) {
        this.isSorting = isSorting;
    }

    public void setIsShooting(boolean isShooting) {
        this.isShooting = isShooting;
    }

    public void run(){

        int error = targetPosition - super.getCurrentPosition();
        int error_abs = Math.abs(error);

        double pow = 0.7;
        if (isSorting) {
            pow = 0.5;
        } else if (isShooting) {
            pow = 0.9;
        }

        if(error_abs > TOLERANCE * 4 ){
            super.set(pow  * error / error_abs);
        }
        else if (error_abs > TOLERANCE) {
            super.set(0.2 * error / error_abs);

        }
        else {
            super.set(0);
        }

    }

}

