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

    public void run() {
        double error = targetPos - encoder.getPosition();
        double dir = 1;

        if (error != 0) {
            dir = error / Math.abs(error);
        }

        double power = 0;

        if ( error < 0 ) {
            /*
            power = Math.min(
                    (pid_p * 0.0006 * error) + (pid_d * 0.00002 * (error - prevError))
                    , 1);

            prevError = error;
            servo.set( power );
            */
            servo.set(1 * dir);
        } else {
            servo.set(0);
            //targetPos = encoder.getCurrentPosition();
        }
    }


}
