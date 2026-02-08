package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.hardware.motors.CRServo;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.ExposureControl;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.WhiteBalanceControl;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

@Autonomous(name = "Auto_FarBlue ", group = "Concept")
//Welcome to 2025's basic Auto!
public class Auto_FarBlue extends LinearOpMode {
    //Declare all your variables here. Keep similar variables together with a note as to what they are

    public int currentState = 0;
    public LinkedList<State> QueLinkList = new LinkedList<State>();

    public MonkeyMotor br;
    public MonkeyMotor bl;
    public MonkeyMotor fr;
    public MonkeyMotor fl;

    public Wheel wheel;
    public Intake intake;
    public Launcher launcher;

    public GoBildaPinpointDriver odo;
    public MonkeyCameraPipeline cameraPipeline = new MonkeyCameraPipeline();

    public MonkeyAprilTagCamera atcam;

    public double keepH = 0;


    public void addDriveToTargetAction(double targetx, double targety, double power) {
        Auto_DriveToTarget a = new Auto_DriveToTarget( targetx, targety, power, this.br, this.bl, this.fr, this.fl );
        this.QueLinkList.add(a);
    }

    public void add_FaceAHeadingAction(double targeth, double power) {
        Auto_FaceAHeading a = new Auto_FaceAHeading( targeth, power, this.br, this.bl, this.fr, this.fl );
        this.QueLinkList.add(a);
    }

    public void addDriveToTargetActionWithIntake(double targetx, double targety, double power) {
        Auto_DriveToTargetWithIntake a = new Auto_DriveToTargetWithIntake( targetx, targety, power, this.br, this.bl, this.fr, this.fl, this.intake);
        this.QueLinkList.add(a);
    }

    public void addSetLaunchSpeed(int mode) {
        Auto_SetLaunchSpeed a = new Auto_SetLaunchSpeed(this.launcher, mode);
        this.QueLinkList.add(a);
    }

    public void addLaunch() {
        Auto_Launch a = new Auto_Launch( this.wheel );
        this.QueLinkList.add(a);
    }

    public void addwaitaction(double wait_TimeInMs) {
        Auto_WaitState a = new Auto_WaitState( wait_TimeInMs );
        this.QueLinkList.add(a);
    }

    public void add_Sort() {
        Auto_Sort a = new Auto_Sort( wheel );
        this.QueLinkList.add(a);
    }
    public void add_getAprilTags() {
        Auto_GetAprilTag a = new Auto_GetAprilTag( this.atcam );
        this.QueLinkList.add(a);
    }
    public void add_FaceAHeadingActionWithAprilTag(double targeth, double power) {
        Auto_FaceAHeadingWithCamera a = new Auto_FaceAHeadingWithCamera( targeth, power, this.br, this.bl, this.fr, this.fl, this.atcam );
        this.QueLinkList.add(a);
    }

    public void initializeState(double targetH) {
        //code runs between every state
        QueLinkList.get(currentState).initializeState(targetH);
    }



    @Override
    public void runOpMode() throws InterruptedException {
        //Place all objects for the auto here, so jeff and dave, motors, claw servos, etc.
        br = new MonkeyMotor(hardwareMap, "br");
        bl = new MonkeyMotor(hardwareMap, "bl");
        fr = new MonkeyMotor(hardwareMap, "fr");
        fl = new MonkeyMotor(hardwareMap, "fl");

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
        this.atcam = new MonkeyAprilTagCamera(hardwareMap, "AprilTagCamera", 10, 55);


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

        //this is P top P mid, g bottom
        wheel.itr.states = new ArrayList<>(Arrays.asList(MonkeyTinyIterator.GREEN, MonkeyTinyIterator.PURPLE, MonkeyTinyIterator.PURPLE));

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
        odo.setOffsets(  49.2, -136.95, DistanceUnit.MM);
        odo.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        odo.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD, GoBildaPinpointDriver.EncoderDirection.REVERSED);
        odo.resetPosAndIMU();

        addSetLaunchSpeed(Launcher.MODE_FAST);
        addwaitaction(1000);
        add_getAprilTags();
        addwaitaction(1000);
        add_Sort();
        addDriveToTargetAction(100, 0, 0.6);
        addwaitaction(3000);
        add_FaceAHeadingAction(0.46, 0.7);

        addLaunch();
        addwaitaction(200);
        add_FaceAHeadingAction(Math.PI/2, 0.75);
        addDriveToTargetAction(580, 100,0.75);

        addDriveToTargetActionWithIntake(580, 1400, 0.3);

        add_Sort();

        addDriveToTargetActionWithIntake(100, 100, 0.7);
        add_FaceAHeadingAction(0.46, 0.7);
        addLaunch();
        addwaitaction(200);

        addDriveToTargetAction(600, 0, 0.8);

        /*
        //first launch
        addSetLaunchSpeed(Launcher.MODE_SLOW);
        addwaitaction(2250);
        addDriveToTargetAction(-580, 200, 0.55);
        add_FaceAHeadingAction(-Math.PI/4, 0.65);
        addLaunch();
        //second launch
        add_FaceAHeadingAction(-Math.PI/2, 0.65);
        //addDriveToTargetAction(-1195, -360, 0.7);
        addDriveToTargetAction(-1100, 500, 0.65);
        addDriveToTargetWithIntakeAction(-1100, -150);
        addDriveToTargetActionWithIntake(-930, 780, 0.65);
        add_FaceAHeadingAction(-Math.PI/4, 0.65);
        addLaunch();
        //third launch
        addDriveToTargetAction(-1700, 500, 0.65);
        add_FaceAHeadingAction(-Math.PI/2, 0.65);
        addDriveToTargetWithIntakeAction(-1700, -150);
        addDriveToTargetActionWithIntake(-930, 780, 0.65);
        add_FaceAHeadingAction(-Math.PI/4, 0.65);
        addLaunch();
        addDriveToTargetAction(-1600, 0, 1);
        */






        while (opModeInInit()) {
            odo.update();
            //Runs after pressing "INIT' and before pressing 'play'

            //sa.setPosition(1);

            fr.resetEncoder();
            fl.resetEncoder();
            br.resetEncoder();
            bl.resetEncoder();

            wheel.count = 3;

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

            telemetry.addData("fr", fr.getCurrentPosition());
            telemetry.addData("fl", fl.getCurrentPosition());
            telemetry.addData("br", br.getCurrentPosition());
            telemetry.addData("bl", bl.getCurrentPosition());

            telemetry.addData("X:", odo.getPosX());
            telemetry.addData("Y:", odo.getPosY());
            telemetry.addData("H:", odo.getHeading());
            telemetry.addData("wheel", wheel.itr.getCurrentContents());

            telemetry.update();
            //wheelServo.run();

        }


        waitForStart();

        //odo.resetPosAndIMU();
        QueLinkList.get(currentState).setCurrentLocationAndRotation(odo.getPosX(), odo.getPosY(), odo.getHeading());
        initializeState(keepH);
        wheel.count = 3;

        while (opModeIsActive()) {
            //Runs after pressing 'Play'
            odo.update();
            double x = odo.getPosX();
            double y = odo.getPosY(); 
            double h = odo.getHeading();

            if (currentState < QueLinkList.size()) {
                if(  QueLinkList.get(currentState).truefalse() ){
                    keepH = QueLinkList.get(currentState).stop();

                    currentState++;
                    if (currentState < QueLinkList.size() ) {
                        QueLinkList.get(currentState).setCurrentLocationAndRotation(x, y, h); // must happen before init
                        initializeState(keepH);
                    }
                } else {
                    QueLinkList.get(currentState).setCurrentLocationAndRotation(x, y, h);
                    QueLinkList.get(currentState).action();

                }
            }
            else {

                fr.set(0);
                fl.set(0);
                br.set(0);
                bl.set(0);
                //visionPortal.stopStreaming();

            }

            intake.run();
            launcher.run();
            wheel.run();


            telemetry.addData("Current State:", currentState);

            telemetry.addData("CurrentTag", SaveData.detectedMotief);
            telemetry.addData("wheel", wheel.count);
            telemetry.addData("X:", x);
            telemetry.addData("Y:", y);
            telemetry.addData("H:", h);

            if (currentState < QueLinkList.size() ) {
                telemetry.addData("Stall Count", QueLinkList.get(currentState).stallcount);
                telemetry.addData("", QueLinkList.get(currentState).readStateData());
            }

            telemetry.addData("br:", br.curSpeed);
            telemetry.addData("bl:", bl.curSpeed);
            telemetry.addData("fr:", fr.curSpeed);
            telemetry.addData("fl:", fl.curSpeed);
            telemetry.addData("IamSorting: ", wheel.getSorting());

            telemetry.update();


        }
    }
}