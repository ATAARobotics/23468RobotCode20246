package org.firstinspires.ftc.teamcode;

import static java.lang.Math.max;
import static java.lang.Math.min;

import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class MonkeyVelocityMotor extends Motor {


    double targetSpeed = 0.0;


    MonkeyVelocityMotor(HardwareMap hardwareMap, String Name) {//, int direction, int mode) {
        super(hardwareMap, Name);
        super.setRunMode(RunMode.VelocityControl);
        super.setZeroPowerBehavior(ZeroPowerBehavior.FLOAT);

    }

    @Override
    public void set(double output){
        targetSpeed = output;
        super.set(output);
    }

}