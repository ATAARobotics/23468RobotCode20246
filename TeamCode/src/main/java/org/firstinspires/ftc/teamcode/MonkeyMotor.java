package org.firstinspires.ftc.teamcode;

import static java.lang.Math.max;
import static java.lang.Math.min;

import com.arcrobotics.ftclib.controller.PController;
import com.arcrobotics.ftclib.controller.PDController;
import com.arcrobotics.ftclib.controller.PIDController;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class MonkeyMotor extends Motor {

    public static int POWER = 0;
    public static int POSITION = 1;

    public static int FORWARD = 1;
    public static int REVERSE = -1;

    //public double curOutput = 0;


    public double targetPosition = 0.0;
    public double prevError = 0.0;
    public double coefficient_p = 1;
    public double coefficient_d = 1;

    double curSpeed = 0.0;
    double prevSetSpeed = 0.0;
    double maxAcceleration = 0.1;

    double setSpeed = 0.0;

    double last_error = 0.0;


    MonkeyMotor(HardwareMap hardwareMap, String Name) {//, int direction, int mode) {
        super(hardwareMap, Name);
        //this.direction = direction;
        //this.mode = mode;

        super.setRunMode(RunMode.RawPower);
        super.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
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
        double test_auto = (coefficient_p * error) + ((error - last_error) * coefficient_d);

        last_error = error;


        double denominator = Math.max(Math.abs(output) + Math.abs(adjustment), 1);
        set_accelerate( (test_auto + adjustment) / denominator );


    }






}
//4.40
//1700