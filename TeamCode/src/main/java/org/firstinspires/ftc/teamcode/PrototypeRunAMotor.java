package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


@TeleOp(name="Prctice Drive", group="Concept")
public class PrototypeRunAMotor extends LinearOpMode {

    //MOTORS GO HERE!
    public LaunchMotor t1;
    public LaunchMotor t2;

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
        t1 = new LaunchMotor(hardwareMap, "t1");
        t2 = new LaunchMotor(hardwareMap, "t2");


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
            t1.pd_speed(LeftStickUPDOWN, telemetry);
            t2.pd_speed(RightStickUPDOWN, telemetry);

            telemetry.addData("motor speed 1", t1.curSpeed);
            telemetry.addData("motor speed 2", t2.curSpeed);


            telemetry.update();
        }
    }
}