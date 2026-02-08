package org.firstinspires.ftc.teamcode;


import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

import java.util.List;

public class Auto_GetAprilTag extends State {

    MonkeyAprilTagCamera camera;

    int prevError = 0;
    int stallCount = 0;

    public Auto_GetAprilTag( MonkeyAprilTagCamera camera) {
        this.camera = camera;
    }

    public boolean truefalse(){
        List<AprilTagDetection> currentDetections = camera.getDetections();
        if (!currentDetections.isEmpty()) {
            for (AprilTagDetection detection : currentDetections) {
                if (detection.metadata != null) {
                    if (detection.id == 21 ) {
                        SaveData.detectedMotief = MonkeyTinyIterator.GPP;
                        return true;

                    } else if (detection.id == 22 ) {
                        SaveData.detectedMotief = MonkeyTinyIterator.PGP;
                        return true;

                    } else if (detection.id == 23 ) {
                        SaveData.detectedMotief = MonkeyTinyIterator.PPG  ;
                        return true;

                    }
                }
            }
        }
        return false;

    }


    public String readStateData() {
        return "April Tag State";
    }


}
