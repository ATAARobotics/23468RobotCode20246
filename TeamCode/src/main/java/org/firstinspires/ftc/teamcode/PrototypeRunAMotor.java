package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


@TeleOp(name="Run A Motor Drive", group="Concept")
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


    //OTHER FLAGS GO HERE


    //CONSTRUCTS GO HERE


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

            /////////////////// GOTTA GOOOOOOO! ///////////////////
            //from Last year's code
            LeftStickUPDOWN = -gamepad1.left_stick_y;
            RightStickUPDOWN = -gamepad1.right_stick_y;
            //rotate motor
            t1.set(LeftStickUPDOWN);
            t2.set(RightStickUPDOWN);

            telemetry.addData("motor speed 1", LeftStickUPDOWN);
            telemetry.addData("motor speed 2", RightStickUPDOWN);

            telemetry.update();
        }
    }
}