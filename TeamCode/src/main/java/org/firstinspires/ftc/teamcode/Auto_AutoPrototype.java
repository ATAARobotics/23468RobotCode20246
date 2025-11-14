package org.firstinspires.ftc.teamcode;



import com.arcrobotics.ftclib.hardware.motors.CRServo;
import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.hardware.rev.RevTouchSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.LinkedList;

@Autonomous(name = "Auto Prototype1_Test", group = "Concept")

//Welcome to 2025's basic Auto!
public class Auto_AutoPrototype extends LinearOpMode {
    //Declare all your variables here. Keep similar variables together with a note as to what they are
    //These are our 4 motors
    public MonkeyMotor br;
    public MonkeyMotor bl;
    public MonkeyMotor fr;
    public MonkeyMotor fl;

    Servo sa;

    GoBildaPinpointDriver odo;

    public Launcher launcher;

    public Intake intake;

    public Wheel wheel;

    public MonkeyMotor intake_motor_wheel_encoder;

    public MonkeyCRServo wheelServo;

    public int currentState = 0;
    public LinkedList<State> QueLinkList = new LinkedList<State>();

    public double keepH = 0;

    public boolean g2_x_flag = false;

    public void addDriveToTargetAction(double targetx, double targety, double power) {
        Auto_DriveToTarget a = new Auto_DriveToTarget( targetx, targety, power, this.br, this.bl, this.fr, this.fl );
        this.QueLinkList.add(a);
    }

    public void addDriveToTargetWithIntakeAction(double targetx, double targety) {
        Auto_Intake a = new Auto_Intake( targetx, targety, 0.3, this.br, this.bl, this.fr, this.fl, this.intake );
        this.QueLinkList.add(a);
    }

    public void addSetLaunchSpeed(int mode) {
        Auto_SetLaunchSpeed a = new Auto_SetLaunchSpeed(this.launcher, mode);
        this.QueLinkList.add(a);
    }

    public void addLaunch() {
        Auto_Launch a = new Auto_Launch( this.wheel, this.intake );
        this.QueLinkList.add(a);
    }

    public void addwaitaction(double wait_TimeInMs) {
        Auto_WaitState a = new Auto_WaitState( wait_TimeInMs );
        this.QueLinkList.add(a);
    }
    public void add_FaceAHeadingAction(double targeth, double power) {
        Auto_FaceAHeading a = new Auto_FaceAHeading( targeth, power, this.br, this.bl, this.fr, this.fl );
        this.QueLinkList.add(a);
    }

    public void add_SetServoAction(Servo servo, double target) {
        Auto_SetServo a = new Auto_SetServo( servo, target );
        this.QueLinkList.add(a);
    }

    public void initializeState(double targetH) {
        //code runs between every state
        QueLinkList.get(currentState).initializeState(targetH);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        //Place all objects for the auto here, so jeff and dave, motors, claw servos, etc.
        //initlaize jeff + dave


        // drive motors
        br = new MonkeyMotor(hardwareMap, "br");
        bl = new MonkeyMotor(hardwareMap, "bl");
        fr = new MonkeyMotor(hardwareMap, "fr");
        fl = new MonkeyMotor(hardwareMap, "fl");

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

        sa = hardwareMap.get(Servo.class, "gate");

        odo = hardwareMap.get(GoBildaPinpointDriver.class,"odo");
        odo.setOffsets(-157.1, -71.3, DistanceUnit.MM);
        odo.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        odo.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD, GoBildaPinpointDriver.EncoderDirection.FORWARD);
        odo.resetPosAndIMU();



        //Initialize our states for auto:

        //addDriveToTargetAction(9000, 600, 0.3);
        //add_FaceAHeadingAction(-3, 0.5);
        //add_SetServoAction(sa, 1);



//auto config
        /*
        addDriveToTargetAction(1200, 380, 0.4);
        add_FaceAHeadingAction(25*Math.PI/36, 0.4);
        addwaitaction(3000);
        add_FaceAHeadingAction(Math.PI/2, 0.7);
        addDriveToTargetAction(1300, 950, 0.4);
        addDriveToTargetAction(1200, 380, 0.4);
        add_FaceAHeadingAction(3*Math.PI/4, 0.7);
        addwaitaction(3000);
        add_FaceAHeadingAction(Math.PI/2, 0.7);
        addDriveToTargetAction(1900, 380, 0.4);
        addDriveToTargetAction(1900, 950, 0.4);
        addDriveToTargetAction(1900,380,0.4);
        addDriveToTargetAction(1200, 380, 0.4);
        add_FaceAHeadingAction(7*Math.PI/9, 0.7);
        addwaitaction(3000);
        addDriveToTargetAction(1600, 380, 0.4);
        add_FaceAHeadingAction(Math.PI/2, 0.7);
        addDriveToTargetAction(1600, 1000, 0.4);
                 */


        addDriveToTargetWithIntakeAction(1000, 0);
        addDriveToTargetAction(1000, 500, 0.5);
        add_FaceAHeadingAction(Math.PI/2, 0.5);
        addSetLaunchSpeed(Launcher.MODE_SLOW);
        addwaitaction(2000);
        addLaunch();
        addSetLaunchSpeed(Launcher.MODE_STOP);
        addDriveToTargetAction(0, 0, 0.7);






        //

        while (opModeInInit()) {
            odo.update();
            //Runs after pressing "INIT' and before pressing 'play'

            sa.setPosition(1);

            fr.resetEncoder();
            fl.resetEncoder();
            br.resetEncoder();
            bl.resetEncoder();

            if (gamepad2.x && !g2_x_flag) {
                g2_x_flag = true;
                //Action here
                wheel.toggleWheelForward();
            } else if (!gamepad2.x) {
                g2_x_flag = false;
            }

            telemetry.addData("fr", fr.getCurrentPosition());
            telemetry.addData("fl", fl.getCurrentPosition());
            telemetry.addData("br", br.getCurrentPosition());
            telemetry.addData("bl", bl.getCurrentPosition());

            telemetry.addData("X:", odo.getPosX());
            telemetry.addData("Y:", odo.getPosY());
            telemetry.addData("H:", odo.getHeading());

            telemetry.update();
            wheelServo.run();

        }


        waitForStart();

        //odo.resetPosAndIMU();
        QueLinkList.get(currentState).setCurrentLocationAndRotation(odo.getPosX(), odo.getPosY(), odo.getHeading());
        initializeState(keepH);

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


            }

            intake.run();
            launcher.run();
            wheel.run();
            wheelServo.run();


            telemetry.addData("Current State:", currentState);
            telemetry.addData("X:", x);
            telemetry.addData("Y:", y);
            telemetry.addData("H:", h);

            if (currentState < QueLinkList.size() ) {
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