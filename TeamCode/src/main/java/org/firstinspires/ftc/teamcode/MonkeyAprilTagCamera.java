package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.ExposureControl;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.GainControl;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class MonkeyAprilTagCamera {

    private AprilTagProcessor aprilTag;
    private VisionPortal visionPortal;

    private int exposureMS = 1;
    private int gain = 1;

    MonkeyAprilTagCamera(HardwareMap hardwareMap, String CameraName, int exposureMS, int gain) {//, int direction, int mode) {
        aprilTag = new AprilTagProcessor.Builder()
                .setLensIntrinsics(265.181, 265.181,320.635, 234.959)
                //.setLensIntrinsics(648.371, 648.371, 266.993, 392.548)
                .build();

        VisionPortal.Builder builder = new VisionPortal.Builder();
        builder.setCamera(hardwareMap.get(WebcamName.class, CameraName));
        builder.setStreamFormat(VisionPortal.StreamFormat.MJPEG);
        builder.addProcessor(aprilTag);

        visionPortal = builder.build();

        this.exposureMS = exposureMS;
        this.gain = gain;

    }

    private boolean initializeCamera() {
        if (visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING) {
            return false;
        } else {
            ExposureControl exposureControl = visionPortal.getCameraControl(ExposureControl.class);
            if (exposureControl.getMode() != ExposureControl.Mode.Manual) {
                exposureControl.setMode(ExposureControl.Mode.Manual);
            }
            exposureControl.setExposure((long) exposureMS, TimeUnit.MILLISECONDS);

            // Set Gain.
            GainControl gainControl = visionPortal.getCameraControl(GainControl.class);
            gainControl.setGain(gain);
            return true;
        }
    }

    public List<AprilTagDetection> getDetections() {
        return aprilTag.getDetections();
    }





}
