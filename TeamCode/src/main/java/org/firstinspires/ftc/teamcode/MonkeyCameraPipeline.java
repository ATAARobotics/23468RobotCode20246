package org.firstinspires.ftc.teamcode;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

class MonkeyCameraPipeline extends OpenCvPipeline {

    //TODO, is lower green or purple
    private int LOWERTHESHOLD = 10;
    private int UPPERTHESHOLD = 50;

    private boolean detectedGreen = false;
    private boolean detectedPurp = false;

    Mat regionMat = new Mat();
    Mat downsizedRegion = new Mat();

    Mat regionToSampleL = new Mat();
    Mat regionToSamplea = new Mat();
    Mat regionToSampleb = new Mat();

    Size s = new Size();

    private Scalar tempval;

    private double nextTime = System.currentTimeMillis() + 200;

    Rect region = new Rect(325, 10, 200,  420);

    //Initialization function
    public MonkeyCameraPipeline()
    {
        //this function left intentionally empty
    }

    //"init" does not mean initialization weirdly enough, it means: what do I do with the first frame?
    @Override
    public void init(Mat firstFrame)
    {

    }

    @Override
    public Mat processFrame(Mat input)
    {
        if (System.currentTimeMillis() < nextTime) {
            return input; //limit framerate to save CPU time
        }
        nextTime = System.currentTimeMillis() + 41 ;


        regionMat = input.submat(region);
        Imgproc.resize(regionMat, downsizedRegion, s, 0.5, 0.5);
        Imgproc.cvtColor(downsizedRegion, downsizedRegion, Imgproc.COLOR_RGB2YCrCb);
        Core.extractChannel(downsizedRegion, regionToSamplea, 1);// a (Green-magenta)
        Core.extractChannel(downsizedRegion, regionToSampleb, 2);// a (Green-magenta)
        Core.bitwise_not(regionToSamplea, regionToSamplea); //!a

        //Green
        Core.subtract(regionToSamplea, regionToSampleb, downsizedRegion); //Really nice green
        downsizedRegion.convertTo(downsizedRegion, -1, 1.0, -50);
        Imgproc.threshold(downsizedRegion, downsizedRegion, 100, 255, Imgproc.THRESH_OTSU);
        tempval = Core.mean(downsizedRegion);

        if (tempval.val[0] > 40) {
            detectedGreen = true;
            detectedPurp = false;
            return input;
        } else {
            detectedGreen = false;
        }

        //Purp
        Core.subtract(regionToSampleb, regionToSamplea, downsizedRegion); //Meh Purple
        downsizedRegion.convertTo(downsizedRegion, -1, 2.0, 0);
        Imgproc.threshold(downsizedRegion, downsizedRegion, 120, 255, Imgproc.THRESH_OTSU);
        tempval = Core.mean(downsizedRegion);

        if (tempval.val[0] > 40) {
            detectedPurp = true;
            detectedGreen = false;
            return input;
        } else {
            detectedPurp = false;
        }


        return input;
    }

    public boolean detectedPurp(){
        return detectedPurp;
    }

    public boolean detectedGreen(){
        return detectedGreen;
    }

}
