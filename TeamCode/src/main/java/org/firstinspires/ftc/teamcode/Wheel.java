package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.hardware.motors.CRServo;
import com.arcrobotics.ftclib.hardware.motors.Motor.Encoder;
import com.qualcomm.robotcore.hardware.Servo;

public class Wheel {

    public MonkeyEncodedCRServo_PositionMode gen_servo1;
    public MonkeyEncodedCRServo_PositionMode gen_servo2;

    public Servo railServoLeft;
    public Servo railServoRight;

    public Encoder wheelEncoder;

    public boolean CanTurn = true;
    public int encoderTargetPos = 0;
    public int count = 0;
    private boolean isShooting = false;

    public boolean amISorting = false;

    public static int NOTCH = 2731;
    public static int ERROR = 100;

    public int stallCount = 0;
    public int prevLoc = 0;
    //

    public MonkeyCameraPipeline cameraPipeline;

    public MonkeyTinyIterator itr = new MonkeyTinyIterator();

    public int Motif = 1;


    public Wheel(CRServo gen_servo1, CRServo gen_servo2,
                 Servo railServoLeft, Servo railServoRight,
                 Encoder wheelEncoder, MonkeyCameraPipeline cameraPipeline) {

        // save motors, initialize to desired starting position
        this.gen_servo1 = new MonkeyEncodedCRServo_PositionMode(gen_servo1, wheelEncoder);
        this.gen_servo2 = new MonkeyEncodedCRServo_PositionMode(gen_servo2, wheelEncoder);

        this.railServoLeft = railServoLeft;
        this.railServoRight = railServoRight;

        railServoLeft.setPosition(0.375);
        railServoRight.setPosition(0.625);

        wheelEncoder.reset();

        this.wheelEncoder = wheelEncoder;
        this.cameraPipeline = cameraPipeline;

    }

    public void toggleWheelForwardForce() {
        itr.next();
        this.count = 99;
        this.encoderTargetPos += NOTCH;
        this.CanTurn = false;
    }

    public void toggleWheelForwardForceCount(int moveCount) {
        if(moveCount==2){
            itr.next();
            itr.next();
        }
        if(moveCount==3){
            itr.next();
            itr.next();
            itr.next();
        }
        if(moveCount==1){
            itr.next();

        }
        if(moveCount==0){


        }

        //itr.next();
        this.count = 99;
        this.encoderTargetPos += NOTCH * moveCount;
        this.CanTurn = false;
    }

    public void toggleWheelForward() {
        if (CanTurn && count < 2) {
            itr.next();
            this.CanTurn = false;
            this.encoderTargetPos += NOTCH;
            this.count += 1;
        }
    }

    public void abandonOperation() {
        this.railServoLeft.setPosition(0.375);
        this.railServoRight.setPosition(0.625);
        isShooting = false;
        this.count = 99;
    }

    public void order() {
        if (!amISorting) {
            amISorting = true;
            this.count = itr.getCount();
            this.railServoLeft.setPosition(0.375);
            this.railServoRight.setPosition(0.625);

            toggleWheelForwardForceCount(itr.getNumberOfRotatesToOrder());
        }


    }

    public void shoot() {
        this.railServoLeft.setPosition(0.5+0.03);//0.5
        this.railServoRight.setPosition(0.5-0.03);
        toggleWheelForwardForceCount(3);
        this.isShooting = true;
        itr.erase();
    }


    public void run() {


        int pos = wheelEncoder.getPosition();
        boolean complete = Math.abs(encoderTargetPos - pos) < ERROR;

        if( complete ) {
            CanTurn = true;
            amISorting = false;
            if ( cameraPipeline.detectedGreen() ){
                itr.set(MonkeyTinyIterator.GREEN);
                toggleWheelForward();
            } else if ( cameraPipeline.detectedPurp() ){
                itr.set(MonkeyTinyIterator.PURPLE);
                toggleWheelForward();
            } else {
                itr.set(MonkeyTinyIterator.NONE);
            }


            if(isShooting) {
                isShooting = false;
                count = 0;
                this.railServoLeft.setPosition(0.375);
                this.railServoRight.setPosition(0.625);
            }
            gen_servo1.setTargetPosition(pos);
            gen_servo2.setTargetPosition(pos);
            //milis = System.currentTimeMillis();

        } else {
            gen_servo1.setTargetPosition(encoderTargetPos);
            gen_servo2.setTargetPosition(encoderTargetPos);
        }


        gen_servo1.run(pos);
        gen_servo2.run(pos);
    }

    public boolean getSorting() {
        return amISorting;
    }

}
