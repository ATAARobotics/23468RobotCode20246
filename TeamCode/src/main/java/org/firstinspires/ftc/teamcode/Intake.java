package org.firstinspires.ftc.teamcode;


public class Intake {

    public double POWER = 1;

    //vars: motors and var for current mode
    public MonkeyCRServo_PowerMode intakeMotor;
    public MonkeyCRServo_PowerMode intakeMotor2;

    public MonkeyCRServo_PowerMode innerMotor;
    public MonkeyCRServo_PowerMode innerMotor2;

    public int direction = 1;
    public int isRunning = 0;
    public int isHold = 0;

    //public long runForIncrementStart;
    //public boolean runForIncrement = false;


    public Intake(MonkeyCRServo_PowerMode intakeMotor,
                  MonkeyCRServo_PowerMode intakeMotor2,
                  MonkeyCRServo_PowerMode innerMotor,
                  MonkeyCRServo_PowerMode innerMotor2) {
        // save motors
        this.intakeMotor = intakeMotor;
        this.intakeMotor2 = intakeMotor2;
        this.innerMotor = innerMotor;
        this.innerMotor2 = innerMotor2;

    }


    public void toggleDirection() {
        // swap direction
        direction *= -1;
    }

    public void setDirection(int direction) {
        // swap direction
        this.direction = direction;
    }

    public void start() {
        //start the intake in the desired direction
        isRunning = 1;
    }

    public void startHold() {
        //start the intake in the desired direction
        isHold = 1;
    }

    public void stop() {
        //turn the intake off
        isRunning = 0;
    }

    public void stopHold() {
        //turn the intake off
        isHold = 0;
    }

    public void run() {
        if (isRunning == 1 || isHold == 1) {
            intakeMotor.set(POWER * direction);
            intakeMotor2.set(POWER * direction);

        } else {
            intakeMotor.set(0);
            intakeMotor2.set(0);
        }

        innerMotor.set(POWER * direction);
        innerMotor2.set(POWER * direction);

    }

}
