package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import com.arcrobotics.ftclib.hardware.motors.CRServo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.ExposureControl;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.WhiteBalanceControl;
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

    List<LynxModule> allHubs = null;
    LynxModule ControlHub = null;
    LynxModule ExpansionHub = null;

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

        allHubs = hardwareMap.getAll(LynxModule.class);
        for (LynxModule hub : allHubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
            if (hub.isParent()) {
                ControlHub = hub;
            } else {
                ExpansionHub = hub;
            }
        }


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


        //intake
        //CRServo intakeMotor = new CRServo(hardwareMap, "Intake Servo 1");
        //CRServo intakeMotor2 = new CRServo(hardwareMap, "Intake Servo 2");
        MonkeyMotor intakeMotor = new MonkeyMotor(hardwareMap, "Intake Motor");
        CRServo innerMotorL = new CRServo(hardwareMap, "Inner Servo L");
        CRServo innerMotorR = new CRServo(hardwareMap, "Inner Servo R");
        intake = new Intake( intakeMotor,// intakeMotor2,
                innerMotorL, innerMotorR
        );

        //wheel
        //Motor genevaEncoderMotor = new Motor(hardwareMap, "Geneva Encoder");
        //CRServo genevaServo1 = new CRServo(hardwareMap, "Geneva Servo 1");
        //CRServo genevaServo2 = new CRServo(hardwareMap, "Geneva Servo 2");
        MonkeyPositionMotorv2 genevaMotor = new MonkeyPositionMotorv2(hardwareMap, "Geneva Motor");
        Servo railServoLeft = hardwareMap.get(Servo.class, "Rail Servo L");
        Servo railServoRight = hardwareMap.get(Servo.class, "Rail Servo R");

        Servo rampServo = hardwareMap.get(Servo.class, "Ramp Servo");
        launcher = new Launcher(new MonkeyVelocityMotor(hardwareMap, "Launcher 1", ControlHub)
                , new MonkeyVelocityMotor(hardwareMap, "Launcher 2", ControlHub)
                , rampServo
        );

        wheel = new Wheel( genevaMotor,
                railServoLeft, railServoRight, cameraPipeline,
                intake, launcher
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
            //telemetry.addLine("In Standby");

            //telemetry.addData("encoder", wheel.wheelEncoder.getPosition());

            if (gamepad1.a) {
                wheel.gen_motor.set(0.3);
                //wheel.gen_servo1.servo.set(0.2);
                //wheel.gen_servo2.servo.set(0.2);
            } else if (gamepad1.b) {
                wheel.gen_motor.set(-0.3);
                //wheel.gen_servo1.servo.set(-0.2);
                //wheel.gen_servo2.servo.set(-0.2);
            } else {
                wheel.gen_motor.set(0);
                //wheel.gen_servo1.servo.set(0);
                //wheel.gen_servo2.servo.set(0);
            }

            telemetry.addData("Wheel Target", wheel.encoderTargetPos);
            telemetry.addData("Wheel At Target", wheel.gen_motor.atTargetPosition());
            telemetry.addData("Wheel Pos", wheel.gen_motor.getCurrentPosition());
            telemetry.addData("Ramp Pos", rampServo.getPosition());
            telemetry.addData("test m1", launcher.motor1.getVelocity());
            telemetry.addData("test m2 ", launcher.motor2.getVelocity());

            telemetry.update();

        }

        waitForStart();
        wheel.gen_motor.resetEncoder();
        wheel.encoderTargetPos = 0;
        //launcher.setLauncherNotIdle();
        //launcher.setLauncherSlow();

        wheel.setRailsToInnerPos(true);
        launcher.Servo60();

        while (opModeIsActive()) {
            //telemetry.addLine("In Run Loop");
            odo.update();

            //odoPose = odo.getPosition();//get xm y and heading in one step

            if (gamepad1.y) {
                //telemetry.addLine("In Camera aim mode");
                boolean ctag = false;
                //we want to fine tune angles
                List<AprilTagDetection> currentDetections = atcam.getDetections();
                if (!currentDetections.isEmpty()) {
                    for (AprilTagDetection detection : currentDetections) {
                        if (detection.metadata != null) {
                            if (detection.id == 20 || detection.id == 24  ) {
                                telemetry.addLine(String.format("\n==== (ID %d) %s", detection.id, detection.metadata.name));
                                telemetry.addLine(String.format("XYZ %6.1f %6.1f %6.1f  (inch)", detection.ftcPose.x, detection.ftcPose.y, detection.ftcPose.z));
                                telemetry.addLine(String.format("PRY %6.1f %6.1f %6.1f  (deg)", detection.ftcPose.pitch, detection.ftcPose.roll, detection.ftcPose.yaw));
                                telemetry.addLine(String.format("RBE %6.1f %6.1f %6.1f  (inch, deg, deg)", detection.ftcPose.range, detection.ftcPose.bearing, detection.ftcPose.elevation));
                                telemetry.addData("delta", Math.atan2(Math.sin(chassis.currentHeading - detection.ftcPose.bearing), Math.cos(chassis.currentHeading - detection.ftcPose.bearing)));
                                //chassis.turnTowards(Math.toRadians(detection.ftcPose.bearing));

                                ctag = true;
                                //launcher.setToDistance(detection.ftcPose.range);
                                break;

                            }
                        }
                    }
                }
                if ( !ctag) {
                    chassis.stop();
                }


            } else {
                //telemetry.addLine("In manual aim mode");
                //telemetry.addData("Curheading", chassis.currentHeading);
                launcher.manualControl();

                double heading = odo.getHeading();
                chassis.setHeading(heading); //Must call before DRIVE


                chassis.DRIVE(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x, gamepad1.right_bumper);
                //telemetry.addData("rotate", gamepad1.right_stick_x);
                //telemetry.addData("Curheading", chassis.currentHeading);
               // telemetry.addData("Target heading", chassis.targetHeading);
                //telemetry.addData("Delta norm",  Math.atan2(Math.sin(chassis.currentHeading - chassis.targetHeading), Math.cos(chassis.currentHeading - chassis.targetHeading)));
            }

            if (gamepad1.x && !g1_x_flag) {
                g1_x_flag = true;
                //Action here
                wheel.toggleWheelForwardForce();
            } else if (!gamepad1.x) {
                g1_x_flag = false;
            }



            if (gamepad1.right_bumper && !g1_x_flag) {
                g1_x_flag = true;
            }

            if (gamepad2.right_bumper && !g2_rb_flag) {
                g2_rb_flag = true;
                //Action here
                wheel.shootWithCooldown();
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

            if (gamepad1.left_bumper && !g1_lb_flag) {
                //Action here
                intake.startHold();
                g1_lb_flag = true;
            } else if (!gamepad1.left_bumper && g1_lb_flag){
                intake.stopHold();
                g1_lb_flag = false;
            }


            if (gamepad1.right_bumper) {
                //Action here
                intake.startReverse();
            } else {
                intake.stopReverse();
            }

            if (gamepad1.a) {
                wheel.encoderTargetPos += 5;
            }

            if (gamepad1.b) {
                wheel.encoderTargetPos -= 5;
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

            if (gamepad2.dpad_up && !g2_up_flag) {
                g2_up_flag = true;
                //Action here
                launcher.adjustLauncherFaster();
            } else if (!gamepad2.dpad_up) {
                g2_up_flag = false;
            }

            if (gamepad2.dpad_down && !g2_down_flag) {
                g2_up_flag = true;
                //Action here
                launcher.adjustLauncherSlower();
            } else if (!gamepad2.dpad_down) {
                g2_down_flag = false;
            }

            /*
            if (gamepad2.a && !g2_a_flag) {
                g2_a_flag = true;
                //Action here
                launcher.Servo45();
            } else if (!gamepad2.a) {
                g2_a_flag = false;
            }

            if (gamepad2.b && !g2_b_flag) {
                g2_b_flag = true;
                //Action here
                launcher.Servo60();
            } else if (!gamepad2.b) {
                g2_b_flag = false;
            }
            */

            //either controller can stop the launcher
            if (gamepad1.back || gamepad2.back ) {
                launcher.setLauncherStop();
            }

            //RUN Subclasses
            wheel.run();
            intake.run();
            launcher.run();

            telemetry.addData( "amISorting", wheel.amISorting);

            //telemetry.addData("ittr Raw", wheel.itr.states.toString());
            //telemetry.addData("index", wheel.itr.cursor);
            //telemetry.addData("Wheel Contents", wheel.itr.getCurrentContents().toString());
            //telemetry.addData("Wheel Target", wheel.encoderTargetPos);
            //telemetry.addData("Wheel At Target", wheel.gen_motor.atTargetPosition());
            //telemetry.addData("Wheel Pos", wheel.gen_motor.getCurrentPosition());
            //telemetry.addData("Wheel Stall Count", wheel.stallCount);

            //telemetry.addData("p", wheel.gen_motor.getPositionCoefficient());
            //telemetry.addData("delta", Math.abs(wheel.encoderTargetPos - wheel.wheelEncoder.getPosition()));

            telemetry.addData("MODE", launcher.getMode());

            telemetry.addData("TARGET:", (launcher.target + launcher.adjustment ));
            telemetry.addData("ACTUAL SPEED:", launcher.motor1.motor.getVelocity( )  * 60 / 28 );// get rpm
            telemetry.addLine();
            telemetry.addData("launch base", (launcher.target ));
            telemetry.addData("launch adjust", (launcher.adjustment ));


            //telemetry.addData("ramp", launcher.rampServo.getPosition());



            telemetry.update();

        }
    }
}