package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.hardware.DigitalChannel;

public class Wheel {

    //vars: motors and var for current position
    DigitalChannel touchSensor;


    public Wheel(MonkeyMotor motor, DigitalChannel touchSensor) {
        // save motors, initialize to desired starting position


        touchSensor.setMode(DigitalChannel.Mode.INPUT);
    }

    public void toggleWheelForward() {
        // move the wheel one notch forward
    }

    public void toggleWheelBackarad() {
        // move the wheel one notch backward
    }

    public void storageMode() {
        //index the wheel a 1/4 notch forward for storage
    }

    public boolean isBottomIndexFull() {
        return touchSensor.getState();
    }

}
