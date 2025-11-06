package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;


@TeleOp(name="Drive Code", group="Concept")
public class DriveCode_Competiton extends LinearOpMode {

    public Chassis chassis;

    public Launcher launcher;

    public Intake intake;

    public Wheel wheel;

    // SERVOS GO HERE

    public Servo gate;

    //BUTTONS & CONTROLS GO HERE
    public float LeftStickUPDOWN;
    public float RightStickUPDOWN;
    public float LeftStickRIGHTLEFT;
    public float RightStickRIGHTLEFT;


    //OTHER FLAGS GO HERE
    public boolean g1_up_flag = false;

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

        launcher = new Launcher(new MonkeyVelocityMotor(hardwareMap, "pew")
                , new MonkeyVelocityMotor(hardwareMap, "pewpew")
        );

        intake = new Intake(new MonkeyMotor(hardwareMap, "TODO: GIVE INTAKE MOTOR A NAME"));

        wheel = new Wheel(new MonkeyMotor(hardwareMap, "TODO: GIVE WHEEL MOTOR A NAME")
                , hardwareMap.get(DigitalChannel.class, "TODO: Touch Sensor Name")
        );

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


            //TODO: ADD CONTROLS HERE

            //Example button with touch protection
            if (gamepad1.dpad_up && !g1_up_flag) {
                g1_up_flag = true;
                //Action here
            } else if (!gamepad1.dpad_up) {
                g1_up_flag = false;
            }


            telemetry.addData("X:", odo.getPosX());
            telemetry.addData("Y:", odo.getPosY());
            telemetry.addData("Angle", odo.getHeading());

            telemetry.update();
        }
    }
}