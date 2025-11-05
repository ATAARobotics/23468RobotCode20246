package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;


@TeleOp(name="Drive Code", group="Concept")
public class DriveCode_Competiton extends LinearOpMode {

    public Chassis chassis;

    public MonkeyMotor pew;
    public MonkeyMotor pewpew;

    // SERVOS GO HERE

    public Servo gate;

    //BUTTONS & CONTROLS GO HERE
    public float LeftStickUPDOWN;
    public float RightStickUPDOWN;
    public float LeftStickRIGHTLEFT;
    public float RightStickRIGHTLEFT;


    //OTHER FLAGS GO HERE


    //CONSTRUCTS GO HERE
    GoBildaPinpointDriver odo;




    @Override
    public void runOpMode() {
        //ROBOT SETUP CODE GOES HERE
        chassis = new Chassis( new MonkeyMotor(hardwareMap, "fr")
                , new MonkeyMotor(hardwareMap, "fl")
                , new MonkeyMotor(hardwareMap, "br")
                , new MonkeyMotor(hardwareMap, "bl")
        );


        pew = new MonkeyMotor(hardwareMap, "pew");
        pewpew = new MonkeyMotor(hardwareMap, "pewpew");

        Servo gate = hardwareMap.get(Servo.class, "gate");

        odo = hardwareMap.get(GoBildaPinpointDriver.class,"odo");
        odo.setOffsets(-181.8, 24.4, DistanceUnit.MM);
        odo.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        odo.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD, GoBildaPinpointDriver.EncoderDirection.REVERSED);
        odo.resetPosAndIMU();


        while (opModeInInit()) {
            odo.update();

            //DURING STANDBY LOOP
            telemetry.addLine("In Standby");

            telemetry.addData("X:", odo.getPosX());
            telemetry.addData("Y:", odo.getPosY());
            telemetry.addData("Angle", odo.getHeading());

            telemetry.update();
        }

        waitForStart();
        while (opModeIsActive()) {
            telemetry.addLine("In Run Loop");
            odo.update();

            chassis.setHeading(odo.getHeading()); //Must call before DRIVE
            chassis.DRIVE(-gamepad1.right_stick_y, gamepad1.right_stick_x, gamepad1.left_stick_x );



            telemetry.addData("X:", odo.getPosX());
            telemetry.addData("Y:", odo.getPosY());
            telemetry.addData("Angle", odo.getHeading());

            telemetry.update();
        }
    }
}