package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.hardware.Servo;

public class Auto_SetServo extends State {

    Servo servo;
    double target;

    public Auto_SetServo(Servo servo, double target) {
        this.servo = servo;
        this.target = target;
    }

    public boolean truefalse(){
        if ( Math.abs(servo.getPosition() - target) < 1 ) {
            return true;
        }
        return false;
    }

    public void action(){
        servo.setPosition(target);
    }

    public String readStateData() {
        return "servo state";
    }


}
