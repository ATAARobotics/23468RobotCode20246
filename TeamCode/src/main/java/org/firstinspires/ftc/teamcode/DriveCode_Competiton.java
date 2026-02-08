package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import com.arcrobotics.ftclib.hardware.motors.CRServo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.ExposureControl;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.WhiteBalanceControl;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;

import java.util.List;
import java.util.concurrent.TimeUnit;


@TeleOp(name="Drive Code", group="Concept")
public class DriveCode_Competiton extends LinearOpMode {

    public Chassis chassis;
    public Wheel wheel;
    public Intake intake;
    public Launcher launcher;

    //BUTTONS & CONTROLS GO HERE
    public float LeftStickUPDOWN;
    public float RightStickUPDOWN;
    public float LeftStickRIGHTLEFT;
    public float RightStickRIGHTLEFT;
    public Rev2mDistanceSensor distanceSensor;


    //OTHER FLAGS GO HERE
    public boolean g1_up_flag = false;
    public boolean g1_x_flag = false;
    public boolean g1_lb_flag = false;
    public boolean g2_y_flag = false;
    public boolean g2_b_flag = false;
    public boolean g2_a_flag = false;
    public boolean g2_x_flag = false;
    public boolean g2_up_flag = false;
    public boolean g2_down_flag = false;
    public boolean g2_right_flag = false;
    public boolean g2_left_flag = false;
    public boolean g2_rb_flag = false;
    public boolean g2_lb_flag = false;

    //CONSTRUCTS GO HERE
    public GoBildaPinpointDriver odo;
    public Pose2D odoPose;

    public MonkeyCameraPipeline cameraPipeline = new MonkeyCameraPipeline();



    @Override
    public void runOpMode() {
        //ROBOT SETUP CODE GOES HERE
        chassis = new Chassis( new MonkeyMotor(hardwareMap, "fr")
                , new MonkeyMotor(hardwareMap, "fl")
                , new MonkeyMotor(hardwareMap, "br")
                , new MonkeyMotor(hardwareMap, "bl")
        );

        //Interior Camera
        WebcamName webcamName = hardwareMap.get(WebcamName.class, "ColorSensor");
        OpenCvWebcam frontCamera = OpenCvCameraFactory.getInstance().createWebcam(webcamName);
        frontCamera.setPipeline( cameraPipeline );
        frontCamera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                frontCamera.startStreaming(640, 480, OpenCvCameraRotation.UPRIGHT, OpenCvWebcam.StreamFormat.MJPEG);
                frontCamera.getWhiteBalanceControl().setMode(WhiteBalanceControl.Mode.MANUAL);
                frontCamera.getWhiteBalanceControl().setWhiteBalanceTemperature(3000);// 3000
                frontCamera.getExposureControl().setMode(ExposureControl.Mode.Manual);
                frontCamera.getExposureControl().setExposure(10, TimeUnit.MILLISECONDS); // 3
                frontCamera.getGainControl().setGain(55); // 20
            }
            @Override
            public void onError(int errorCode) {}
        });

        //AprilTag Camera
        MonkeyAprilTagCamera atcam = new MonkeyAprilTagCamera(hardwareMap, "AprilTagCamera", 10, 55);


        //wheel
        Motor genevaEncoderMotor = new Motor(hardwareMap, "Geneva Encoder");
        CRServo genevaServo1 = new CRServo(hardwareMap, "Geneva Servo 1");
        CRServo genevaServo2 = new CRServo(hardwareMap, "Geneva Servo 2");
        Servo railServoLeft = hardwareMap.get(Servo.class, "Rail Servo L");
        Servo railServoRight = hardwareMap.get(Servo.class, "Rail Servo R");
        wheel = new Wheel( genevaServo1, genevaServo2,
                railServoLeft, railServoRight,
                genevaEncoderMotor.encoder, cameraPipeline
        );

        //intake
        //CRServo intakeMotor = new CRServo(hardwareMap, "Intake Servo 1");
        //CRServo intakeMotor2 = new CRServo(hardwareMap, "Intake Servo 2");
        MonkeyMotor intakeMotor = new MonkeyMotor(hardwareMap, "Intake Motor");
        CRServo innerMotorL = new CRServo(hardwareMap, "Inner Servo L");
        CRServo innerMotorR = new CRServo(hardwareMap, "Inner Servo R");
        intake = new Intake( intakeMotor,// intakeMotor2,
                innerMotorL, innerMotorR
        );

        launcher = new Launcher(new MonkeyVelocityMotor(hardwareMap, "Launcher 1")
                , new MonkeyVelocityMotor(hardwareMap, "Launcher 2")
        );

        // odo
        odo = hardwareMap.get(GoBildaPinpointDriver.class,"Odometry I2C");
        odo.setOffsets(49.2, -136.95, DistanceUnit.MM);
        odo.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        odo.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD, GoBildaPinpointDriver.EncoderDirection.FORWARD);
        odo.resetPosAndIMU();

        while (opModeInInit()) {
            odo.update();

            //DURING STANDBY LOOP
            //telemetry.addData("DistanceSensorInfo",distanceSensor.getDistance(DistanceUnit.MM));
            telemetry.addLine("In Standby");

            telemetry.addData("encoder", wheel.wheelEncoder.getPosition());

            if (gamepad1.a) {
                wheel.gen_servo1.servo.set(0.2);
                wheel.gen_servo2.servo.set(0.2);
            } else if (gamepad1.b) {
                wheel.gen_servo1.servo.set(-0.2);
                wheel.gen_servo2.servo.set(-0.2);
            } else {
                wheel.gen_servo1.servo.set(0);
                wheel.gen_servo2.servo.set(0);
            }


            telemetry.update();

        }

        waitForStart();
        wheel.wheelEncoder.reset();
        //launcher.setLauncherNotIdle();
        //launcher.setLauncherSlow();

        while (opModeIsActive()) {
            telemetry.addLine("In Run Loop");
            odo.update();

            //odoPose = odo.getPosition();//get xm y and heading in one step

            if (gamepad1.y) {
                telemetry.addLine("In Camera aim mode");

                //we want to fine tune angles
                List<AprilTagDetection> currentDetections = atcam.getDetections();
                if (!currentDetections.isEmpty()) {
                    for (AprilTagDetection detection : currentDetections) {
                        if (detection.metadata != null) {
                            if (detection.id == 20 || detection.id == 24  ) {
                                telemetry.addData("detected:", detection.id);
                                telemetry.addData("detection.ftcPose.yaw:", Math.toRadians(detection.ftcPose.yaw));
                                telemetry.addData("detection.ftcPose.range:", detection.ftcPose.range);
                                chassis.turnTowards(Math.toRadians(detection.ftcPose.yaw));
                                launcher.setToDistance(detection.ftcPose.range);
                                break;

                            }
                        }
                    }
                }


            } else {
                telemetry.addLine("In manual aim mode");
                //telemetry.addData("Curheading", chassis.currentHeading);
                launcher.manualControl();
                chassis.setHeading(odo.getHeading()); //Must call before DRIVE
                chassis.DRIVE(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x, gamepad1.right_bumper);
                telemetry.addData("rotate", gamepad1.right_stick_x);
                telemetry.addData("Curheading", chassis.currentHeading);
                telemetry.addData("Target heading", chassis.targetHeading);
                telemetry.addData("Delta norm",  Math.atan2(Math.sin(chassis.currentHeading - chassis.targetHeading), Math.cos(chassis.currentHeading - chassis.targetHeading)));
            }

            if (gamepad1.x && !g1_x_flag) {
                g1_x_flag = true;
                //Action here
                wheel.toggleWheelForwardForce();
            } else if (!gamepad1.x) {
                g1_x_flag = false;
            }

            if (gamepad2.right_bumper && !g2_rb_flag) {
                g2_rb_flag = true;
                //Action here
                wheel.shoot();
            } else if (!gamepad2.right_bumper) {
                g2_rb_flag = false;
            }

            if (gamepad2.x && !g2_x_flag) {
                g2_x_flag = true;
                //Action here
                wheel.order();
            } else if (!gamepad2.x) {
                g2_x_flag = false;
            }


            if (gamepad2.y && !g2_y_flag) {
                g2_a_flag = true;
                //Action here
                wheel.abandonOperation();
            } else if (!gamepad2.y) {
                g2_y_flag = false;
            }

            if (gamepad1.left_bumper) {
                //Action here
                intake.startHold();
            } else {
                intake.stopHold();
            }

            if (gamepad1.right_bumper) {
                //Action here
                intake.startReverse();
            } else {
                intake.stopReverse();
            }

            if (gamepad2.dpad_right && !g2_right_flag) {
                g2_right_flag = true;
                //Action here
                launcher.incrementModeFaster();
                launcher.setLauncherNotIdle();
            } else if (!gamepad2.dpad_right) {
                g2_right_flag = false;
            }

            if (gamepad2.dpad_left && !g2_left_flag) {
                g2_left_flag = true;
                //Action here
                launcher.incrementModeSlower();
                launcher.setLauncherNotIdle();
            } else if (!gamepad2.dpad_left) {
                g2_left_flag = false;
            }


            //either controller can stop the launcher
            if (gamepad1.back || gamepad2.back ) {
                launcher.setLauncherStop();
            }

            //RUN Subclasses
            wheel.run();
            intake.run();
            launcher.run();

            telemetry.addData( "amISorting", wheel.amISorting);

            telemetry.addData("ittr Raw", wheel.itr.states.toString());
            telemetry.addData("index", wheel.itr.cursor);
            telemetry.addData("Wheel Contents", wheel.itr.getCurrentContents().toString());
            telemetry.addData("Wheel Target", wheel.encoderTargetPos);
            telemetry.addData("Wheel Pos", wheel.wheelEncoder.getPosition());
            telemetry.addData("delta", Math.abs(wheel.encoderTargetPos - wheel.wheelEncoder.getPosition()));

            telemetry.addData("launch speed", launcher.getMode());
            telemetry.addData("launch speed", (launcher.target + launcher.adjustment ));
            telemetry.addData("launch speed", launcher.motor1.encoder.getRawVelocity() );



            telemetry.update();

        }
    }
}