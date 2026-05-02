package org.firstinspires.ftc.teamcode;

import static java.lang.Math.max;
import static java.lang.Math.min;

import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.VoltageUnit;

public class MonkeyVelocityMotor {

    public final DcMotorEx motor;
    private LynxModule hub;
    public double velocity;
    public boolean idle;

    public static double friction = 0;
    public int target_rpm = 0;
    public static double Kp = 0.001;
    public static double TOLERANCE = 100;
    public static double MAX_VOLTAGE = 13;
    public static int IDLE_RPM = 2455;


    MonkeyVelocityMotor(HardwareMap hardwareMap, String Name, LynxModule hub) {//, int direction, int mode) {
        this.motor = hardwareMap.get(DcMotorEx.class, Name);
        this.hub = hub;

        this.motor.setDirection(DcMotor.Direction.FORWARD);
        this.motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

    }

    public void setPower(float power) {
        motor.setPower(power);
    }

    public void set(int rpm) {
        this.target_rpm = rpm;
    }

    public void run() {
        double rpm = getVelocity() * 60 / 28;
        double feedforward = 0;
        if (idle) {

            target_rpm = IDLE_RPM;
        } else {

        }

        double error = 0;
        double expected_voltage = 0;
        if (rpm < target_rpm - TOLERANCE) {
            feedforward = MAX_VOLTAGE;
        } else if (rpm > target_rpm + TOLERANCE) {
            feedforward = 0;
        } else {

        }

        //feedforward = 0;
        error = target_rpm - rpm;
        expected_voltage = MAX_VOLTAGE *  target_rpm / 5800;

        // 13 is "Absolute max" voltage
        double voltage = Math.min(feedforward + expected_voltage + friction + Kp * error, MAX_VOLTAGE);
        // set power to my voltage / max voltage

        this.motor.setPower(voltage / hub.getInputVoltage(VoltageUnit.VOLTS));
    }

    public void setIdle() {
        this.motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        this.idle = true;
    }

    public void clearIdle() {
        this.idle = false;
        this.motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    public double getVelocity() {
        return this.motor.getVelocity();
    }

    public double getPower() {
        return this.motor.getPower();
    }

    public double getAverageVoltage() {
        return this.motor.getPower() * hub.getInputVoltage(VoltageUnit.VOLTS);
    }

}