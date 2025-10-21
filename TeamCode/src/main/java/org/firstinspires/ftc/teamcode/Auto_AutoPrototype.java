package org.firstinspires.ftc.teamcode;



import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.LinkedList;

@Autonomous(name = "Auto Prototype", group = "Concept")

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

    public int currentState = 0;
    public LinkedList<State> QueLinkList = new LinkedList<State>();

    public double keepH;

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

    public void initializeState() {
        //code runs between every state
        QueLinkList.get(currentState).initializeState();
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
        addDriveToTargetAction(500, 0, 0.4);
        add_FaceAHeadingAction(Math.PI/2, 0.4);
        addDriveToTargetAction(900, 0, 0.4);
        //addwaitaction(6700);
        //addDriveToTargetAction(2400, 0, 0.6);
        //addDriveToTargetAction(167,0, 0.6);


        //addDriveToTargetAction(1288, 0, 0.6);
        //addDriveToTargetAction(1288, 728, 0.6);
        //addDriveToTargetAction(862, 0, 0.6);
        //addDriveToTargetAction(1916, 0, 0.6);
        //addDriveToTargetAction(1916, 610, 0.6);
        //addDriveToTargetAction(862, 0, 0.6);
        //addDriveToTargetAction(1664, 631, 0.6);





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
        initializeState();

        while (opModeIsActive()) {
            //Runs after pressing 'Play'
            odo.update();
            double x = odo.getPosX();
            double y = odo.getPosY(); 
            double h = odo.getHeading();

            if (currentState < QueLinkList.size()) {
                if(  QueLinkList.get(currentState).truefalse() ){
                    QueLinkList.get(currentState).stop();

                    currentState++;
                    if (currentState < QueLinkList.size() ) {
                        QueLinkList.get(currentState).setCurrentLocationAndRotation(x, y, h); // must happen before init
                        initializeState();
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