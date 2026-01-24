package org.firstinspires.ftc.teamcode;


import com.arcrobotics.ftclib.hardware.motors.CRServo;

public class MonkeyCRServo_PowerMode {

    public CRServo servo;

    public MonkeyCRServo_PowerMode(CRServo servo) {
        this.servo = servo;

    }

    public void set(double output) {
        servo.set(output);
    }



}
