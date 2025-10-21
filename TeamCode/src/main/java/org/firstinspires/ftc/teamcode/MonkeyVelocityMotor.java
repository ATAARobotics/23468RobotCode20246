package org.firstinspires.ftc.teamcode;

import static java.lang.Math.max;
import static java.lang.Math.min;

import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class MonkeyVelocityMotor extends Motor {

    public static int POWER = 0;
    public static int POSITION = 1;

    public static int FORWARD = 1;
    public static int REVERSE = -1;


    double targetSpeed = 0.0;


    MonkeyVelocityMotor(HardwareMap hardwareMap, String Name) {//, int direction, int mode) {
        super(hardwareMap, Name);
        //this.direction = direction;
        //this.mode = mode;

        super.setRunMode(RunMode.VelocityControl);
        super.setZeroPowerBehavior(ZeroPowerBehavior.FLOAT);

        super.setVeloCoefficients(0.05, 0.01, 0.31);
        super.setFeedforwardCoefficients(0.92, 0.47);
    }

    @Override
    public void set(double output){
        targetSpeed = output;
        super.set(output);
    }



}