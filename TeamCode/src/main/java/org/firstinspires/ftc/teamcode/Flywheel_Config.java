package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.VoltageUnit;


@Config
public class Flywheel_Config {

    public static double friction = 0.05;
    public int target_rpm = 0;
    public static double Kp = 0.13;
    public static double TOLERANCE = 100;
    public static double MAX_VOLTAGE = 13;



    private final DcMotorEx motor;
    private LynxModule hub;

    public Flywheel_Config(LynxModule hub, HardwareMap hardwareMap, String motor1) {
        this.motor = hardwareMap.get(DcMotorEx.class, motor1);
        this.hub = hub;

        this.motor.setDirection(DcMotor.Direction.FORWARD);
        this.motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void setPower(float power) {
        motor.setPower(power);
    }

    public void setRPM(int rpm) {
        this.target_rpm = rpm;
        runRPM();
    }

    public void runRPM() {
        double rpm = getVelocity() * 60 / 28;
        double feedforward = 0;
        if (rpm < target_rpm - TOLERANCE) {
            feedforward = MAX_VOLTAGE;
        } else if (rpm > target_rpm + TOLERANCE) {
            feedforward = -MAX_VOLTAGE;
        }
        double error = target_rpm - rpm;
        double expected_voltage = MAX_VOLTAGE *  rpm / 5800;

        // 13 is "Absolute max" voltage
        double voltage = Math.min(feedforward + expected_voltage + friction + Kp * error, MAX_VOLTAGE);
        // set power to my voltage / max voltage
        this.motor.setPower(voltage / hub.getInputVoltage(VoltageUnit.VOLTS));
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
