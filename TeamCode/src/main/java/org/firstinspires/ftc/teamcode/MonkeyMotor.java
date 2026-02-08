package org.firstinspires.ftc.teamcode;

import static java.lang.Math.max;
import static java.lang.Math.min;

import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class MonkeyMotor extends Motor {

    public static int POWER = 0;
    public static int POSITION = 1;

    public static int FORWARD = 1;
    public static int REVERSE = -1;


    public double targetPosition = 0.0;
    public double prevError = 0.0;
    public double coefficient_p = 1.1;
    public double coefficient_d = 1.2;

    public double coefficient_p_rot = 1.25;
    public double coefficient_d_rot = 0.93;

    double curSpeed = 0.0;
    double prevSetSpeed = 0.0;
    double maxAcceleration = 0.1;

    double setSpeed = 0.0;


    MonkeyMotor(HardwareMap hardwareMap, String Name) {//, int direction, int mode) {
        super(hardwareMap, Name);
        //this.direction = direction;
        //this.mode = mode;

        super.setRunMode(RunMode.RawPower);
        super.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);
    }

    @Override
    public void set(double output){
        curSpeed = output;
        super.set(output);
    }

    public void set_accelerate(double output){
        double pow;
        if(output > 0 && prevSetSpeed >=0 && output > prevSetSpeed){
            pow = min(prevSetSpeed + maxAcceleration, output);
        } else if(output < 0 && prevSetSpeed <=0 && output < prevSetSpeed){
            pow = max(prevSetSpeed - maxAcceleration, output);
        } else {
            pow = output;
        }
        prevSetSpeed = pow;
        set(pow);
    }

    public void set_pd(double output, double adjustment, double error) {

        //proportion=distance=error
        double test_auto = Math.min(
                (coefficient_p * 0.0075 * error) + (coefficient_d * 0.005 * (error - prevError))
                , 1);
        prevError = error;

        setSpeed = test_auto * output;

        double denominator = Math.max(Math.abs(setSpeed) + Math.abs(adjustment), 1);
        set_accelerate( (setSpeed + adjustment) / denominator );


    }

    public void set_pd_rotate(double output, double error) {

        //set_accelerate(-output);
        //return;

        //proportion=distance=error
        double test_auto = Math.min(
                (coefficient_p_rot * 1 * error) + (coefficient_d_rot * 1.2 * (error - prevError))
                , 1);
        prevError = error;

        setSpeed = test_auto * output;

        double denominator = Math.max(Math.abs(setSpeed), 1);
        set_accelerate( (setSpeed) / denominator );


    }






}
//4.40
//1700