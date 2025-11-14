package org.firstinspires.ftc.teamcode;


public class Intake {

    public static int DIRECTION_FORWARD = 1;
    public static int DIRECTION_REVERSE = -1;

    public double POWER = 1;

    //vars: motors and var for current mode
    public MonkeyMotor intakeMotor;
    public int direction = DIRECTION_FORWARD;
    public double isRunning = 0;

    public long runForIncrementStart;
    public boolean runForIncrement = false;


    public Intake(MonkeyMotor motor) {
        // save motors
        this.intakeMotor = motor;
    }

    public int getDirection() {
        return direction;
    }

    public void toggleDirection() {
        // swap direction
        direction *= -1;
    }

    public void setDirection(int direction) {
        // swap direction
        this.direction = direction;
    }

    public void runForIncrement() {
        runForIncrement = true;
        runForIncrementStart = System.currentTimeMillis();
    }

    public void start() {
        //start the intake in the desired direction
        isRunning = 1;
    }

    public void slow() {
        //start the intake in the desired direction
        isRunning = 0.4;
    }

    public void stop() {
        //turn the intake off
        isRunning = 0;
    }

    public void run() {
        if (isRunning == 1 ) {
            intakeMotor.set(POWER * direction);
        } else if (runForIncrement) {
            intakeMotor.set(POWER * direction);
            if (System.currentTimeMillis() - runForIncrementStart > 1000) {
                runForIncrement = false;
            }
        } else {
            intakeMotor.set(0);
        }

    }

}
