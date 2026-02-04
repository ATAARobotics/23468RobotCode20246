package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
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

@TeleOp(name = "Camearaaaa", group = "Concept")
@Disabled
public class CameraStreamTeleop extends LinearOpMode {

    public MonkeyCameraPipeline cameraPipeline = new MonkeyCameraPipeline(); // -------------------------------------------------------------------CHANGE THIS TO DETECT RED/BLUE

    @Override
    public void runOpMode() throws InterruptedException {

        WebcamName webcamName = hardwareMap.get(WebcamName.class, "Armcam");
        OpenCvWebcam frontCamera = OpenCvCameraFactory.getInstance().createWebcam(webcamName);


        frontCamera.setPipeline( cameraPipeline );

        frontCamera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                frontCamera.startStreaming(640, 480, OpenCvCameraRotation.UPRIGHT);
                frontCamera.getWhiteBalanceControl().setMode(WhiteBalanceControl.Mode.MANUAL);
                frontCamera.getWhiteBalanceControl().setWhiteBalanceTemperature(3000);// 3000
                frontCamera.getExposureControl().setMode(ExposureControl.Mode.Manual);
                frontCamera.getExposureControl().setExposure(10, TimeUnit.MILLISECONDS); // 3
                frontCamera.getGainControl().setGain(55); // 20

            }
            @Override
            public void onError(int errorCode) {}
        });

        while (opModeInInit()) {

            //telemetry.addData("lines Detected?", cameraPipeline.getDetectedBricks());
            telemetry.update();
        }

        waitForStart();

        while (opModeIsActive()) {


            telemetry.update();

        }

    }

}
