package org.firstinspires.ftc.teamcode;


import com.arcrobotics.ftclib.hardware.motors.CRServo;

public class Intake {

    public double POWER = 0.5;

    //vars: motors and var for current mode
    //public CRServo intakeMotor;
    //public CRServo intakeMotor2;

    public MonkeyMotor intakeMotor;

    public CRServo innerMotorL;
    public CRServo innerMotorR;

    public int direction = 1;
    public int isRunning = 0;
    public int isHold = 0;

    //public long runForIncrementStart;
    //public boolean runForIncrement = false;


    public Intake(MonkeyMotor intakeMotor,
                  //CRServo intakeMotor2,
                  CRServo innerMotorL,
                  CRServo innerMotorR) {
        // save motors
        this.intakeMotor = intakeMotor;
        //this.intakeMotor2 = intakeMotor2;
        this.innerMotorL = innerMotorL;
        this.innerMotorR = innerMotorR;

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
            intakeMotor.set(POWER * direction * -1);
            //intakeMotor2.set(POWER * direction);

        } else {
            intakeMotor.set(0);
            //intakeMotor2.set(0);
        }

        innerMotorL.set(POWER * direction * -1);
        innerMotorR.set(POWER * direction);

    }

}
