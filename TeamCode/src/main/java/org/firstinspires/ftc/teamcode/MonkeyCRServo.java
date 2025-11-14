package org.firstinspires.ftc.teamcode;


import com.arcrobotics.ftclib.hardware.motors.CRServo;

public class MonkeyCRServo {

    public static double P_coefficient = 1.2;
    public static double D_coefficient = 0.7;

    public static int tolerance = 100;

    private double prevError = 0.0;

    public int stallCount = 0;
    public boolean stallMode = false;

    public CRServo servo;
    public MonkeyMotor encoder;
    public int targetPos = 0;

    public MonkeyCRServo(CRServo servo, MonkeyMotor encoder) {
        this.servo = servo;
        this.encoder = encoder;
        encoder.resetEncoder();
    }

    public void setTargetPosition(int target) {
        this.targetPos = target;
        stallCount = 0;
        stallMode = false;
    }

    public void run() {
        double error = targetPos - encoder.getCurrentPosition();
        double power = 0;
        if (stallMode) {
            power = Math.min(
                    (P_coefficient * -0.0006 * error) + (D_coefficient * -0.00002 * (error - prevError))
                    , 1);
            prevError = error;

            servo.set( power );

            stallCount--;

            if (stallCount <= 0) {
                stallMode = false;
                stallCount = 0;
            }

        } else if ( error < 0 ) {

            if (Math.abs(prevError - error) < 1) {
                stallCount++;
            }
            if (stallCount >= 15) {
                stallMode = true;
                stallCount = 5 ;
            }
            if (error > 1000) {
                power =  1.0;
            } else if (error < -1000) {
                power = -1.0;
            } else {
                power = Math.min(
                        (P_coefficient * 0.0006 * error) + (D_coefficient * 0.00002 * (error - prevError))
                        , 1);
            }
            prevError = error;
            servo.set( power );
        } else {
            servo.set(0);
        }
    }


}
