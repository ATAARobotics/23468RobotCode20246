package org.firstinspires.ftc.teamcode;

import static java.lang.Math.max;
import static java.lang.Math.min;

import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class LaunchMotor extends Motor {

    public static int POWER = 0;
    public static int POSITION = 1;

    public static int FORWARD = 1;
    public static int REVERSE = -1;

    //public double curOutput = 0;


    public double targetPosition = 0.0;
    public double prevError = 0.0;
    public double coefficient_p = 0.00015;
    public double coefficient_d = 0.0;
    public double coefficient_i = 0.0001;

    double curSpeed = 0.0;
    double prevSetSpeed = 0.0;
    double maxAcceleration = 0.1;

    double setSpeed = 0.0;

    double last_error = 0.0;

    double LastPosition = 0.0;

    double LastSpeed = 0.0;
    double ErrorSum = 0.0;

    LaunchMotor(HardwareMap hardwareMap, String Name) {//, int direction, int mode) {
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

    public void pd_speed(double TargetSpeed, Telemetry telemetry){

        double CurrentPosition = super.getCurrentPosition();
        double CurrentSpeed = (CurrentPosition - LastPosition);
        double error = (TargetSpeed - CurrentSpeed);
        ErrorSum += error;
        double power = (coefficient_p * error) + ((curSpeed - LastSpeed) * coefficient_d) + ErrorSum * coefficient_i;
        LastPosition = CurrentPosition;
        LastSpeed = CurrentSpeed;

        telemetry.addData("Error", error);
        set(power);



    }












}
//4.40
//1700