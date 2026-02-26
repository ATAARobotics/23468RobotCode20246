package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class MonkeyVelocityMotorBkp extends Motor {


    double targetSpeed = 0.0;


    MonkeyVelocityMotorBkp(HardwareMap hardwareMap, String Name) {//, int direction, int mode) {
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