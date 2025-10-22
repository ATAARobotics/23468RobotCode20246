package org.firstinspires.ftc.teamcode;

import org.opencv.core.CvType;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.List;

class MonkeyCameraPipelineBlobObject2025_26 extends OpenCvPipeline {

    // Best practice is to put working vars in the pipeline object, do not create local variables as that causes memory leaks. There is no automatic garbage collection here
    Mat LAB = new Mat(); // Mat objects are like pictures, a list of pixel values



    Mat regionToSamplea = new Mat();

    Mat flat;

    //Initialization function
    public MonkeyCameraPipelineBlobObject2025_26()
    {
        //this function left intentionally empty
    }


    //"init" does not mean initialization weirdly enough, it means: what do I do with the first frame?
    @Override
    public void init(Mat firstFrame)
    {
        flat = new Mat(firstFrame.rows(), firstFrame.cols(), CvType.CV_8UC1, new Scalar(255));
        //
    }

    //What actions do I take with each picture the camera takes?
    @Override
    public Mat processFrame(Mat input)
    {
        Imgproc.cvtColor(input, LAB, Imgproc.COLOR_RGB2Lab);
        Core.extractChannel(LAB, regionToSamplea, 1);// a (Green-magenta);// A channel only considers one colour at a time, red, green, and blue



        return regionToSamplea;
    }































}
