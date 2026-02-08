package org.firstinspires.ftc.teamcode;


import com.arcrobotics.ftclib.hardware.motors.CRServo;
import com.arcrobotics.ftclib.hardware.motors.Motor.Encoder;

public class MonkeyEncodedCRServo_PositionMode {

    public CRServo servo;
    public Encoder encoder;

    public int targetPos = 0;
    public double pid_p = 2.3;
    public double pid_d = 0.7;

    private double prevError = 0;

    public MonkeyEncodedCRServo_PositionMode(CRServo servo, Encoder encoder) {
        this.servo = servo;
        this.encoder = encoder;

        this.pid_p = pid_p;
        this.pid_d = pid_d;
    }

    public void setTargetPosition(int target) {
        this.targetPos = target;
    }

    public void run(int encoderPos) {

        double error = this.targetPos - encoderPos;

        if ( Math.abs(error) > 200 ) {
            servo.set(error / Math.abs(error));
        }
        if ( Math.abs(error) > 100 ) {
            servo.set(error * 0.3 / Math.abs(error));
        }
        else {
            servo.set(0);

        }

        /*double error = targetPos - encoder.getPosition();
        double dir = 1;

        if (error != 0) {
            dir = error / Math.abs(error);
        }

        double power = 0;

        if ( Math.abs(error) < 200 ) {
            servo.set(1 * dir);
        } else {
            servo.set(0);

        }*/
    }


}
