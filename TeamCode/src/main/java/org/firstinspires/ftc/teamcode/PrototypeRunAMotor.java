package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


@TeleOp(name="Prototype Drive", group="Concept")
public class PrototypeRunAMotor extends LinearOpMode {

    //MOTORS GO HERE!
    public MonkeyMotor t1;
    public MonkeyMotor t2;

    // SERVOS GO HERE

    //void - Nothing
    //int - integer
    //float - decimal
    //boolean - True or False
    //double - decimal




    //BUTTONS & CONTROLS GO HERE
    public float LeftStickUPDOWN;
    public float RightStickUPDOWN;

    public boolean up = false;
    public boolean down = false;


    //OTHER FLAGS GO HERE


    //CONSTRUCTS GO HERE
    public double targetSpeed = 0.0;


    @Override
    public void runOpMode() {
        //ROBOT SETUP CODE GOES HERE
        t1 = new MonkeyMotor(hardwareMap, "t1");
        t2 = new MonkeyMotor(hardwareMap, "t2");


        while (opModeInInit()) {
            //DURING STANDBY LOOP
            telemetry.addLine("In Standby");


            telemetry.update();
        }

        waitForStart();
        while (opModeIsActive()) {

            if (gamepad1.dpad_up && !up) {
                up = true;
                targetSpeed += 0.01;
            } else if (!gamepad1.dpad_up) {
                up = false;
            }

            if (gamepad1.dpad_down && !down) {
                down = true;
                targetSpeed -= 0.01;
            } else if (!gamepad1.dpad_down) {
                down = false;
            }

            t1.set(targetSpeed);
            t2.set(targetSpeed);//the motors go in same directions

            telemetry.addData("target Speed", targetSpeed);
            telemetry.addData("motor speed 1", t1.encoder.getRawVelocity());
            telemetry.addData("motor speed 2", t2.encoder.getRawVelocity());

            telemetry.update();
        }
    }
}