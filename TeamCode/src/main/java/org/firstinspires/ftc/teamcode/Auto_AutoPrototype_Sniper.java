package org.firstinspires.ftc.teamcode;



import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.LinkedList;

@Autonomous(name = "Auto Sniper Prototype1.5", group = "Concept")

//Welcome to 2025's basic Auto!
public class Auto_AutoPrototype_Sniper extends LinearOpMode {
    //Declare all your variables here. Keep similar variables together with a note as to what they are
    //These are our 4 motors
    public MonkeyMotor br;
    public MonkeyMotor bl;
    public MonkeyMotor fr;
    public MonkeyMotor fl;

    Servo sa;

    GoBildaPinpointDriver odo;

    public int currentState = 0;
    public LinkedList<State> QueLinkList = new LinkedList<State>();

    public double keepH = 0;

    public void addDriveToTargetAction(double targetx, double targety, double power) {
        Auto_DriveToTarget a = new Auto_DriveToTarget( targetx, targety, power, this.br, this.bl, this.fr, this.fl );
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

        sa = hardwareMap.get(Servo.class, "gate");

        odo = hardwareMap.get(GoBildaPinpointDriver.class,"odo");
        odo.setOffsets(-181.8, 24.4, DistanceUnit.MM);
        odo.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        odo.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD, GoBildaPinpointDriver.EncoderDirection.REVERSED);
        odo.resetPosAndIMU();

        //Initialize our states for auto:

        //addDriveToTargetAction(9000, 600, 0.3);
        //add_FaceAHeadingAction(-3, 0.5);
        //add_SetServoAction(sa, 1);



//auto config
        addDriveToTargetAction(200, 0, 0.4);
        add_FaceAHeadingAction(-Math.PI/8, 0.7);
        addwaitaction(3000);
        add_FaceAHeadingAction(-19*Math.PI/36, 0.4);
        addDriveToTargetAction(125, 100, 0.4);
        addDriveToTargetAction(100, -1010, 0.4);
        addwaitaction(2000);
        addDriveToTargetAction(300, 0, 0.4);
        add_FaceAHeadingAction(-Math.PI/7, 0.7);
        addwaitaction(3000);
        addDriveToTargetAction(700, -350, 0.4);
        add_FaceAHeadingAction(-Math.PI/2, 0.6);
        addDriveToTargetAction(700, -750, 0.4);
        addDriveToTargetAction(250, 0, 0.4);
        add_FaceAHeadingAction(-Math.PI/7, 0.7);
        addwaitaction(3000);
        add_FaceAHeadingAction(-Math.PI/2, 0.7);
        addDriveToTargetAction(1630, -400, 0.4);
        addDriveToTargetAction(1630, -850, 0.4);




        //

        while (opModeInInit()) {
            odo.update();
            //Runs after pressing "INIT' and before pressing 'play'

            sa.setPosition(1);

            fr.resetEncoder();
            fl.resetEncoder();
            br.resetEncoder();
            bl.resetEncoder();

            telemetry.addData("fr", fr.getCurrentPosition());
            telemetry.addData("fl", fl.getCurrentPosition());
            telemetry.addData("br", br.getCurrentPosition());
            telemetry.addData("bl", bl.getCurrentPosition());

            telemetry.addData("X:", odo.getPosX());
            telemetry.addData("Y:", odo.getPosY());
            telemetry.addData("H:", odo.getHeading());

            telemetry.update();


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