package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Servo;

public class LaunchSelector {

    public static double OUT_POSITION=0.5;
    public static double IN_POSITION=0;

    private Servo ServoRight = null;
    private Servo ServoLeft = null;
    public LaunchSelector(Servo ServoRight, Servo ServoLeft) {
        this.ServoRight=ServoRight;
        this.ServoLeft=ServoLeft;
    }
    public void ToggleLauncher(){
        if(ServoRight.getPosition() == OUT_POSITION) {
            ServoRight.setPosition(IN_POSITION);
        } else {
            ServoRight.setPosition(OUT_POSITION);
        }

        if(ServoLeft.getPosition() == OUT_POSITION) {
            ServoLeft.setPosition(IN_POSITION);
        } else {
            ServoRight.setPosition(OUT_POSITION);
        }
    }
}
