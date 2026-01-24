package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.hardware.motors.CRServo;
import com.arcrobotics.ftclib.hardware.motors.Motor.Encoder;

public class Wheel {

    public MonkeyEncodedCRServo_PositionMode gen_servo1;
    public MonkeyEncodedCRServo_PositionMode gen_servo2;

    public Encoder wheelEncoder;

    public boolean CanTurn = true;
    public int encoderTargetPos = 0;

    public static int NOTCH = 2710;
    public static int ERROR = 200;

    public Wheel(CRServo gen_servo1, CRServo gen_servo2, Encoder wheelEncoder) {
        // save motors, initialize to desired starting position
        this.gen_servo1 = new MonkeyEncodedCRServo_PositionMode(gen_servo1, wheelEncoder);
        this.gen_servo2 = new MonkeyEncodedCRServo_PositionMode(gen_servo2, wheelEncoder);
        this.wheelEncoder = wheelEncoder;
    }

    public void toggleWheelForwardForce() {
        if (CanTurn) {
            CanTurn = false;
            encoderTargetPos += NOTCH;
            // move the wheel one notch forward
            gen_servo1.setTargetPosition(encoderTargetPos);
            gen_servo2.setTargetPosition(encoderTargetPos);
        }
    }


    public void run() {

        if( Math.abs(encoderTargetPos - wheelEncoder.getPosition()) < ERROR ){
            CanTurn = true;
            gen_servo1.setTargetPosition(wheelEncoder.getPosition());
            gen_servo2.setTargetPosition(wheelEncoder.getPosition());
            //milis = System.currentTimeMillis();
        }


        gen_servo1.run();
        gen_servo2.run();
    }

}
