package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.ExposureControl;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.WhiteBalanceControl;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;

import java.util.concurrent.TimeUnit;

@TeleOp(name="cam", group="Concept")
public class CameraConcept2025_26 extends LinearOpMode{

    boolean A;
    boolean aFlag= false;

    public MonkeyCameraPipelineBlobObject2025_26 cameraPipeline = new MonkeyCameraPipelineBlobObject2025_26();

    @Override
    public void runOpMode() {

        WebcamName webcamName = hardwareMap.get(WebcamName.class, "TestCam");
        OpenCvWebcam frontCamera = OpenCvCameraFactory.getInstance().createWebcam(webcamName);

        frontCamera.setPipeline( cameraPipeline );

        frontCamera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                frontCamera.startStreaming(960, 600, OpenCvCameraRotation.UPRIGHT);
                frontCamera.getWhiteBalanceControl().setMode(WhiteBalanceControl.Mode.MANUAL);
                frontCamera.getWhiteBalanceControl().setWhiteBalanceTemperature(3000);// 3000
                frontCamera.getExposureControl().setMode(ExposureControl.Mode.Manual);
                frontCamera.getExposureControl().setExposure(5, TimeUnit.MILLISECONDS); // 20
                frontCamera.getGainControl().setGain(90); // 20
            }
            @Override
            public void onError(int errorCode) {}
        });

        while (opModeInInit()) {

            //
        }

        waitForStart();
        while (opModeIsActive()) {
            telemetry.addLine("I don't do anything!");
            telemetry.update();
        }
    }
}
