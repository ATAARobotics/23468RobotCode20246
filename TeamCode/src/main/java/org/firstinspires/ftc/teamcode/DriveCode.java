package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.arcrobotics.ftclib.hardware.motors.CRServo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.robotcore.external.navigation.UnnormalizedAngleUnit;


@TeleOp(name="Player Controlled Drive Code_OLD", group="Concept")
@Disabled
public class DriveCode extends LinearOpMode {

    //MOTORS GO HERE!
    public MonkeyMotor br;
    public MonkeyMotor bl;
    public MonkeyMotor fr;
    public MonkeyMotor fl;

    public MonkeyMotor pew;
    public MonkeyMotor pewpew;

    // SERVOS GO HERE

    public Servo gate;
    public MonkeyCRServo wheelServo;

    //BUTTONS & CONTROLS GO HERE
    public float LeftStickUPDOWN;
    public float RightStickUPDOWN;
    public float LeftStickRIGHTLEFT;
    public float RightStickRIGHTLEFT;


    //OTHER FLAGS GO HERE


    //CONSTRUCTS GO HERE
    GoBildaPinpointDriver odo;

    public double heading;


    @Override
    public void runOpMode() {
        //ROBOT SETUP CODE GOES HERE
        br = new MonkeyMotor(hardwareMap, "br");
        bl = new MonkeyMotor(hardwareMap, "bl");
        fr = new MonkeyMotor(hardwareMap, "fr");
        fl = new MonkeyMotor(hardwareMap, "fl");

        pew = new MonkeyMotor(hardwareMap, "pew");
        pewpew = new MonkeyMotor(hardwareMap, "pewpew");

        Servo gate = hardwareMap.get(Servo.class, "gate");

        odo = hardwareMap.get(GoBildaPinpointDriver.class,"odo");
        odo.setOffsets(-181.8, 24.4, DistanceUnit.MM);
        odo.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        odo.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD, GoBildaPinpointDriver.EncoderDirection.REVERSED);
        odo.resetPosAndIMU();


        while (opModeInInit()) {
            odo.update();

            heading = odo.getHeading();

            //DURING STANDBY LOOP
            telemetry.addLine("In Standby");

            telemetry.addData("X:", odo.getPosX());
            telemetry.addData("Y:", odo.getPosY());
            telemetry.addData("Angle", odo.getHeading());
            telemetry.addData("heading", heading);

            telemetry.update();
        }

        waitForStart();
        while (opModeIsActive()) {
            odo.update();
            /////////////////// GOTTA GOOOOOOO! ///////////////////
            //from Last year's code

            if(gamepad1.x){
                gate.setPosition(-1);
            }
            else if (!gamepad1.x ) {
                gate.setPosition(1);
            }


            //TODO name = opposite of input
            LeftStickUPDOWN = gamepad1.left_stick_y;
            LeftStickRIGHTLEFT = gamepad1.left_stick_x;
            RightStickUPDOWN = -gamepad1.right_stick_y;
            RightStickRIGHTLEFT= gamepad1.right_stick_x;

            double rx = RightStickRIGHTLEFT;
            double ry = RightStickUPDOWN;
            double sped = 1.0;
            double lx = LeftStickRIGHTLEFT;
            double rxSped = 0.75;
            double adjustment = 0;
            double coefficient = 0.8;

            if (LeftStickRIGHTLEFT == 0) {
                //keep heading as 0
                adjustment = coefficient*(odo.getHeading() - heading);
                lx = adjustment;
            } else {
                heading = odo.getHeading();
            }
            telemetry.addData("delta heading",(odo.getHeading() - heading));
            telemetry.addData("adjustment",adjustment);

            double denominator = Math.max(Math.abs(ry) + Math.abs(rx) + Math.abs(lx), 1);

            fr.set((ry - rx - lx * rxSped) / denominator * sped);
            fl.set((ry + rx + lx * rxSped) / denominator * sped);
            br.set((-ry - rx + lx * rxSped) / denominator * sped);
            bl.set((ry - rx + lx * rxSped) / denominator * sped);

            telemetry.addLine("In Run Loop");

            pew.set(0.75);
            pewpew.set(0.75);

            telemetry.addData("X:", odo.getPosX());
            telemetry.addData("Y:", odo.getPosY());
            telemetry.addData("Angle", odo.getHeading());

            telemetry.update();
        }
    }
}