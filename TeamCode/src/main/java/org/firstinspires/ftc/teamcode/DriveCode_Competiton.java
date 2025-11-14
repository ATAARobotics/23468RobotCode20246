package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.hardware.rev.RevTouchSensor;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;

import com.arcrobotics.ftclib.hardware.motors.CRServo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;


@TeleOp(name="Drive Code", group="Concept")
public class DriveCode_Competiton extends LinearOpMode {

    public Chassis chassis;

    public Launcher launcher;

    public Intake intake;

    public Wheel wheel;

    // SERVOS GO HERE

    public MonkeyCRServo wheelServo;

    //BUTTONS & CONTROLS GO HERE
    public float LeftStickUPDOWN;
    public float RightStickUPDOWN;
    public float LeftStickRIGHTLEFT;
    public float RightStickRIGHTLEFT;
    public Rev2mDistanceSensor distanceSensor;


    //OTHER FLAGS GO HERE
    public boolean g1_up_flag = false;
    public boolean g2_y_flag = false;
    public boolean g2_b_flag = false;
    public boolean g2_a_flag = false;
    public boolean g2_x_flag = false;
    public boolean g2_up_flag = false;
    public boolean g2_down_flag = false;
    public boolean g2_right_flag = false;
    public boolean g2_left_flag = false;
    public boolean g2_rb_flag = false;

    //CONSTRUCTS GO HERE
    GoBildaPinpointDriver odo;

    public MonkeyMotor intake_motor_wheel_encoder;


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

        intake_motor_wheel_encoder = new MonkeyMotor(hardwareMap, "intakeMotor"); //TODO: GIVE INTAKE MOTOR A NAME


        intake = new Intake(intake_motor_wheel_encoder);

        wheelServo = new MonkeyCRServo( new CRServo(hardwareMap, "wheel"), intake_motor_wheel_encoder);

        wheel = new Wheel(wheelServo
                , hardwareMap.get(Rev2mDistanceSensor.class, "SensorOfDistance")
                , intake
        );


      //  distanceSensor = hardwareMap.get(Rev2mDistanceSensor.class, "SensorOfDistance");

        odo = hardwareMap.get(GoBildaPinpointDriver.class,"odo");
        odo.setOffsets(-157.1, -71.3, DistanceUnit.MM);
        odo.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        odo.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD, GoBildaPinpointDriver.EncoderDirection.FORWARD);
        odo.resetPosAndIMU();

        intake_motor_wheel_encoder.resetEncoder();

        while (opModeInInit()) {
            odo.update();

            //DURING STANDBY LOOP
            //telemetry.addData("DistanceSensorInfo",distanceSensor.getDistance(DistanceUnit.MM));
            telemetry.addLine("In Standby");

            telemetry.addData("X:", odo.getPosX());
            telemetry.addData("Y:", odo.getPosY());
            telemetry.addData("Angle", odo.getHeading());


            //wheel.run();
            telemetry.addData("distance", wheel.distanceSensor.getDistance(DistanceUnit.MM) );
            telemetry.addData( "flag", wheel.flag );
            //wheelServo.run();

            telemetry.addData("wheel pos", intake_motor_wheel_encoder.getCurrentPosition() );
            telemetry.addData("servoPos", wheelServo.targetPos);

            telemetry.update();

            wheelServo.run();

        }

        waitForStart();
        while (opModeIsActive()) {
            telemetry.addLine("In Run Loop");
            odo.update();

            chassis.setHeading(odo.getHeading()); //Must call before DRIVE

            if (gamepad1.left_bumper) {
                //Intake mode

                intake.setDirection(Intake.DIRECTION_FORWARD);
                intake.start();
                chassis.intakeDrive(gamepad1.right_stick_x);


            } else {
                chassis.DRIVE(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x, gamepad1.right_bumper);
                intake.stop();
            }


            //TODO: ADD CONTROLS HERE


            if (gamepad2.b && !g2_b_flag) {
                g2_b_flag = true;
                //Action here
                launcher.setLauncherIdle();
            } else if (!gamepad2.b) {
                g2_b_flag = false;
            }

            //resume intake
            if (gamepad2.a && !g2_a_flag) {
                g2_a_flag = true;
                //Action here
                launcher.setLauncherNotIdle();
            } else if (!gamepad2.a) {
                g2_a_flag = false;
            }


            if (gamepad2.dpad_up && !g2_up_flag) {
                g2_up_flag = true;
                //Action here
                launcher.adjustLauncherFaster();
            } else if (!gamepad2.dpad_up) {
                g2_up_flag = false;
            }

            if (gamepad2.dpad_down && !g2_down_flag) {
                g2_down_flag = true;
                //Action here
                launcher.adjustLauncherSlower();
            } else if (!gamepad2.dpad_down) {
                g2_down_flag = false;
            }

            if (gamepad2.dpad_right && !g2_right_flag) {
                g2_right_flag = true;
                //Action here
                launcher.incrementModeFaster();
            } else if (!gamepad2.dpad_right) {
                g2_right_flag = false;
            }

            if (gamepad2.dpad_left && !g2_left_flag) {
                g2_left_flag = true;
                //Action here
                launcher.incrementModeSlower();
            } else if (!gamepad2.dpad_left) {
                g2_left_flag = false;
            }


            if (gamepad1.x && !g2_x_flag) {
                g2_x_flag = true;
                //Action here
                wheel.toggleWheelForwardForce();
            } else if (!gamepad1.x) {
                g2_x_flag = false;
            }

            if (gamepad2.right_bumper && !g2_rb_flag) {
                g2_rb_flag = true;
                //Action here
                intake.runForIncrement();
                wheel.toggleWheelFireAll();
            } else if (!gamepad2.right_bumper) {
                g2_rb_flag = false;
            }




            //either controller can stop the launcher
            if (gamepad1.start || gamepad2.start) {
               launcher.setLauncherStop();
            }


            //RUN Subclasses
            intake.run();
            launcher.run();
            wheel.run();
            wheelServo.run();

            telemetry.addData("X:", odo.getPosX());
            telemetry.addData("Y:", odo.getPosY());
            telemetry.addData("Angle", odo.getHeading());

            telemetry.addData("distance", wheel.distanceSensor.getDistance(DistanceUnit.MM) );
            telemetry.addData( "flag", wheel.flag );

            telemetry.addData("wheel pos", intake_motor_wheel_encoder.getCurrentPosition() );

            telemetry.addData("launch speed", launcher.getMode());

            telemetry.update();
        }
    }
}