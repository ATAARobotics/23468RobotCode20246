package org.firstinspires.ftc.teamcode;



import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

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

    GoBildaPinpointDriver odo;

    public int currentState = 0;
    public LinkedList<State> QueLinkList = new LinkedList<State>();

    public double keepH;

    public void addDriveToTargetAction(double targetx, double targety, double power) {
        Auto_DriveToTarget a = new Auto_DriveToTarget( targetx, targety, power, this.br, this.bl, this.fr, this.fl );
        this.QueLinkList.add(a);
    }
    public void add_FaceAHeadingAction(double targeth, double power) {
        Auto_FaceAHeading a = new Auto_FaceAHeading( targeth, power, this.br, this.bl, this.fr, this.fl );
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

        odo = hardwareMap.get(GoBildaPinpointDriver.class,"odo");
        odo.setOffsets(24.0, 84.0, DistanceUnit.MM);
        odo.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        odo.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD, GoBildaPinpointDriver.EncoderDirection.FORWARD);
        odo.resetPosAndIMU();

        //Initialize our states for auto:

        //addDriveToTargetAction(1000, 700, 0.5);
        add_FaceAHeadingAction(Math.PI, 0.5);

        while (opModeInInit()) {
            //  odo.resetPosAndIMU();
            //Runs after pressing "INIT' and before pressing 'play'

            fr.resetEncoder();
            fl.resetEncoder();
            br.resetEncoder();
            bl.resetEncoder();

            telemetry.addData("fr", fr.getCurrentPosition());
            telemetry.addData("fl", fl.getCurrentPosition());
            telemetry.addData("br", br.getCurrentPosition());
            telemetry.addData("bl", bl.getCurrentPosition());

            telemetry.update();


        }


        waitForStart();

        odo.resetPosAndIMU();
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
                        QueLinkList.get(currentState).setCurrentLocationAndRotation(x, y, h);
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