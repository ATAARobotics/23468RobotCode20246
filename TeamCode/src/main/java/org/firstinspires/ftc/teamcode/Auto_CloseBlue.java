package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.hardware.motors.CRServo;
import com.qualcomm.hardware.lynx.LynxModule;
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
import java.util.List;
import java.util.concurrent.TimeUnit;

@Autonomous(name = "Auto_CloseBlue", group = "Concept")
//Welcome to 2025's basic Auto!
public class Auto_CloseBlue extends LinearOpMode {
    //Declare all your variables here. Keep similar variables together with a note as to what they are

    public int currentState = 0;
    public LinkedList<State> QueLinkList = new LinkedList<State>();

    public MonkeyMotor br;
    public MonkeyMotor bl;
    public MonkeyMotor fr;
    public MonkeyMotor fl;

    List<LynxModule> allHubs = null;
    LynxModule ControlHub = null;
    LynxModule ExpansionHub = null;

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
    public void addDriveToTargetActionWithIntakeAndSort(double targetx, double targety, double power, ArrayList<Integer> translated) {
        Auto_DriveToTargetWithIntakeAndSort a = new Auto_DriveToTargetWithIntakeAndSort( targetx, targety, power, this.br, this.bl, this.fr, this.fl, this.intake, this.wheel, translated);
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

    public void add_Sort(ArrayList<Integer> content) {
        Auto_Sort a = new Auto_Sort( wheel, content);
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

        allHubs = hardwareMap.getAll(LynxModule.class);
        for (LynxModule hub : allHubs) {
            //hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
            if (hub.isParent()) {
                ControlHub = hub;
            } else {
                ExpansionHub = hub;
            }
        }

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

        //this is P top P mid, g bottom
        wheel.itr.states = new ArrayList<>(Arrays.asList(MonkeyTinyIterator.GREEN, MonkeyTinyIterator.PURPLE, MonkeyTinyIterator.PURPLE));
        wheel.railServoLeft.setPosition(0.375);
        wheel.railServoRight.setPosition(0.625);



        // odo
        odo = hardwareMap.get(GoBildaPinpointDriver.class,"Odometry I2C");
        odo.setOffsets(  49.2, -136.95, DistanceUnit.MM);
        odo.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        odo.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD, GoBildaPinpointDriver.EncoderDirection.REVERSED);
        odo.resetPosAndIMU();

        /*-19.42   -800   0

            8.75   -858   1.5   Math.PI/2

            -871     3 x Math.PI/4

                -240  -1119

                -704  -642

                -711 -1536

                -1258  -962

                -1019 -1244

        */

        addSetLaunchSpeed(Launcher.MODE_SLOW);
        addDriveToTargetAction(0, -800, 0.8);
        add_getAprilTags();
        add_Sort(new ArrayList<>(Arrays.asList(MonkeyTinyIterator.GREEN, MonkeyTinyIterator.PURPLE, MonkeyTinyIterator.PURPLE)));
        add_FaceAHeadingAction(1.4, 0.7);
        addwaitaction(1500);
        addLaunch();
        addwaitaction(200);
        add_FaceAHeadingAction(3*Math.PI/4, 0.75);
        addDriveToTargetAction(-240, -1120, 0.8);
        addDriveToTargetActionWithIntake(-720, -660, 0.25);
        //add_Sort();
        addwaitaction(700);
        addDriveToTargetActionWithIntakeAndSort(-100, -710, 0.8, new ArrayList<>(Arrays.asList(MonkeyTinyIterator.GREEN, MonkeyTinyIterator.PURPLE, MonkeyTinyIterator.PURPLE)));
        add_FaceAHeadingAction(Math.PI/2, 0.7);
        //addwaitaction(0.500);
        addLaunch();
        addwaitaction(200);
        add_FaceAHeadingAction(3*Math.PI/4, 0.75);
        addDriveToTargetAction(-650-6, -1530-15, 0.9);
        addDriveToTargetActionWithIntake(-1255-36, -900-45, 0.25);
        addDriveToTargetActionWithIntake(-1025-6, -1180-15, 0.65);
        addwaitaction(700);
        // add_Sort();
        addDriveToTargetActionWithIntakeAndSort(-100,-710  ,0.85, new ArrayList<>(Arrays.asList(MonkeyTinyIterator.PURPLE, MonkeyTinyIterator.GREEN, MonkeyTinyIterator.PURPLE)));
        add_FaceAHeadingAction(Math.PI/2, 0.7);
        addLaunch();
        addwaitaction(200);
        addDriveToTargetAction(-600,-800,1);





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
                //wheel.gen_servo1.servo.set(0.2);
                //wheel.gen_servo2.servo.set(0.2);
            } else if (gamepad1.b) {
                //wheel.gen_servo1.servo.set(-0.2);
                //wheel.gen_servo2.servo.set(-0.2);
            } else {
                //wheel.gen_servo1.servo.set(0);
                //wheel.gen_servo2.servo.set(0);
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
            telemetry.addData("wheel content", wheel.itr.getCurrentContents());
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

            telemetry.update();


        }
    }
}