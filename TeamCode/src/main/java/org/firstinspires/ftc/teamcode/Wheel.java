package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Servo;

import java.util.ArrayList;

public class Wheel {

    public MonkeyPositionMotorv2 gen_motor;

    public Servo railServoLeft;
    public Servo railServoRight;


    public boolean CanTurn = true;
    public int encoderTargetPos = 0;
    public int count = 0;
    public boolean isShooting = false;

    public boolean amISorting = false;

    public static int NOTCH = 512 ;
    //public static int ERROR = 100;

    public int stallCount = 0;
    public int prevPos = 0;
    public boolean stallMode = false;

    public long lastSortTime = 0;
    public boolean delayShoot = false;
    //

    public MonkeyCameraPipeline cameraPipeline;

    public MonkeyTinyIterator itr = new MonkeyTinyIterator();

    public int Motif = 1;


    public Wheel(MonkeyPositionMotorv2 gen_motor,
                 Servo railServoLeft, Servo railServoRight,
                 MonkeyCameraPipeline cameraPipeline) {

        // save motors, initialize to desired starting position
        this.gen_motor = gen_motor;

        this.railServoLeft = railServoLeft;
        this.railServoRight = railServoRight;

        this.cameraPipeline = cameraPipeline;

    }

    public void setRailsToInnerPos(boolean RailPositionGoTo) {
        if(RailPositionGoTo){
            railServoLeft.setPosition(0.325);
            railServoRight.setPosition(0.7);
        } else {
            railServoLeft.setPosition(0.5+0.00);
            railServoRight.setPosition(0.5-0.00);
        }

    }

    public void toggleWheelForwardForce() {
        itr.next();
        this.count = 99;
        this.encoderTargetPos -= NOTCH;
        this.CanTurn = false;
    }

    public void toggleWheelForwardForceCount(int moveCount) {
        if(moveCount==2){
            itr.next();
            itr.next();
        } else if(moveCount==3){
            itr.next();
            itr.next();
            itr.next();
        } else if(moveCount==1){
            itr.next();
        }

        this.count = 99;
        this.encoderTargetPos -= NOTCH * moveCount;
        this.CanTurn = false;
    }

    public void toggleWheelForward() {
        if (CanTurn && count < 2     ) {
            itr.next();
            this.CanTurn = false;
            this.encoderTargetPos -= NOTCH;
            this.count += 1;
        }
    }

    public void abandonOperation() {
        setRailsToInnerPos(true);
        isShooting = false;
        this.count = 99;
    }

    public void order() {
        if (!amISorting) {
            amISorting = true;
            this.gen_motor.setIsSorting(true);
            this.count = itr.getCount();
            this.setRailsToInnerPos(true);

            toggleWheelForwardForceCount(itr.getNumberOfRotatesToOrder());
        }

    }

    public void orderWithPredefinedSet(ArrayList<Integer> translated) {
        if (!amISorting) {
            amISorting = true;
            this.gen_motor.setIsSorting(true);
            this.count = itr.getCount();
            this.setRailsToInnerPos(true);

            toggleWheelForwardForceCount(itr.getNumberOfRotatesToOrderGivenASpecificSet(translated));
        }


    }

    public void shoot() {
        setRailsToInnerPos(false);
        toggleWheelForwardForceCount(3);
        this.isShooting = true;
        itr.erase();
    }

    public void shootWithCooldown() {
        setRailsToInnerPos(false);
        this.isShooting = true;
        this.delayShoot = true;
    }


    public void run() {

        if (delayShoot) {
            if(System.currentTimeMillis() - lastSortTime > 300 && !amISorting) {
                delayShoot = false;
                shoot();
            }
        }

        boolean complete = Math.abs(gen_motor.getCurrentPosition() - this.encoderTargetPos) < MonkeyPositionMotorv2.TOLERANCE; //THIS IS HERE BECAUSE THE MOTOR POSITION GETS SET AFTER THIS CALL SO .atTargetPosition DOES NOT WORK

        if( complete ) {
            gen_motor.setTargetPosition(encoderTargetPos);

            stallCount = 0;
            CanTurn = true;

            if (cameraPipeline.detectedGreen()) {
                itr.set(MonkeyTinyIterator.GREEN);
                toggleWheelForward();
            } else if (cameraPipeline.detectedPurp()) {
                itr.set(MonkeyTinyIterator.PURPLE);
                toggleWheelForward();
            } else {
                itr.set(MonkeyTinyIterator.NONE);
            }

            if(amISorting) {
                amISorting = false;
                this.setRailsToInnerPos(false);
                this.gen_motor.setIsSorting(false);
                this.lastSortTime = System.currentTimeMillis();
            }

            if(isShooting) {
                isShooting = false;
                count = 0;
                this.setRailsToInnerPos(true);
            }


        } else {
            int curPos = gen_motor.getCurrentPosition();
            if (stallMode) {
                gen_motor.setTargetPosition( curPos + 200);
                stallCount--;

                if (stallCount <= 0) {
                    stallMode = false;
                    stallCount = 0;
                }

            } else {
                if (curPos == prevPos) {
                    stallCount++;
                }
                if (stallCount >= 10) {
                    stallMode = true;
                    stallCount = 4 ;
                }
                gen_motor.setTargetPosition(encoderTargetPos);
            }
            prevPos = curPos;
        }


        gen_motor.run();
    }

    public boolean getSorting() {
        return amISorting;
    }

}
