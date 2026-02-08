package org.firstinspires.ftc.teamcode;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

class GetThresholdsPipeline extends OpenCvPipeline {

    private int PURPLETHRESHOLD = -1;
    private int GREENTHESHOLD = -1;
    private double meanval = -1;
    private Scalar tempval;

    public volatile int mode = 0;

    Mat regionMat = new Mat();
    Mat downsizedRegion = new Mat();

    Mat regionToSampleL = new Mat();
    Mat regionToSamplea = new Mat();
    Mat regionToSampleb = new Mat();

    Size s = new Size();

    private boolean detectedGreen = false;
    private boolean detectedPurp = false;

    Rect region = new Rect(325, 10, 200,  420);

    static final Scalar PURPLE = new Scalar(255, 0, 255);
    static final Scalar GREEN = new Scalar(0, 255, 0);
    static final Scalar GREY = new Scalar(40, 40, 40);
    static final Scalar WHITE = new Scalar(255);

    static final Point putText = new Point(5,20);
    static final Point putText_Lower = new Point(5,50);

    public Core.MinMaxLocResult mm;

    //Initialization function
    public GetThresholdsPipeline()
    {
        //this function left intentionally empty
    }

    //"init" does not mean initialization weirdly enough, it means: what do I do with the first frame?
    @Override
    public void init(Mat firstFrame)
    {

    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public double getMeanval() {
        return meanval;
    }

    public int getGREENTHESHOLD() {
        return GREENTHESHOLD;
    }

    public int getPURPLETHRESHOLD() {
        return PURPLETHRESHOLD;
    }

    @Override
    public Mat processFrame(Mat input)
    {


        regionMat = input.submat(region);
        Imgproc.resize(regionMat, downsizedRegion, s, 0.5, 0.5);
        Imgproc.cvtColor(downsizedRegion, downsizedRegion, Imgproc.COLOR_RGB2YCrCb);
        Core.extractChannel(downsizedRegion, regionToSamplea, 1);// a (Green-magenta)
        Core.extractChannel(downsizedRegion, regionToSampleb, 2);// a (Green-magenta)
        Core.bitwise_not(regionToSamplea, regionToSamplea); //!a

        if (mode == 0) {

            Core.subtract(regionToSamplea, regionToSampleb, downsizedRegion); //Really nice green
            mm = Core.minMaxLoc(downsizedRegion);
            downsizedRegion.convertTo(downsizedRegion, -1, 1.0, -50);
            //Imgproc.medianBlur(downsizedRegion, downsizedRegion, 15);
            //Imgproc.putText(downsizedRegion, "", putText, Imgproc.FONT_HERSHEY_SIMPLEX, 1.0, WHITE ); // add a few pixels so OTSU uses a high threshold
            Imgproc.threshold(downsizedRegion, downsizedRegion, 80, 255, Imgproc.THRESH_OTSU);
            GREENTHESHOLD = (int) Core.mean(downsizedRegion).val[0];
            //Imgproc.threshold(downsizedRegion, downsizedRegion, 50, 255, Imgproc.THRESH_BINARY);
            Imgproc.putText(downsizedRegion, String.format("%2f", mm.maxVal ), putText, Imgproc.FONT_HERSHEY_SIMPLEX, 1.0, WHITE );
            Imgproc.putText(downsizedRegion, String.format("%2d", GREENTHESHOLD ), putText_Lower, Imgproc.FONT_HERSHEY_SIMPLEX, 1.0, WHITE );
            return downsizedRegion;
        }
        else if (mode == 1) {
            Core.subtract(regionToSampleb, regionToSamplea, downsizedRegion); //meh purp
            mm = Core.minMaxLoc(downsizedRegion);
            downsizedRegion.convertTo(downsizedRegion, -1, 2.0, 0);
            //Imgproc.medianBlur(downsizedRegion, downsizedRegion, 15);

            Imgproc.threshold(downsizedRegion, downsizedRegion, 100, 255, Imgproc.THRESH_OTSU);
            PURPLETHRESHOLD = (int) Core.mean(downsizedRegion).val[0];
            Imgproc.putText(downsizedRegion, String.format("%2f", mm.maxVal ), putText, Imgproc.FONT_HERSHEY_SIMPLEX, 1.0, WHITE );

            Imgproc.putText(downsizedRegion, String.format("%2d", PURPLETHRESHOLD ), putText_Lower , Imgproc.FONT_HERSHEY_SIMPLEX, 1.0, WHITE );

            return downsizedRegion;
        }
        else {

            Core.subtract(regionToSamplea, regionToSampleb, downsizedRegion); //Really nice green
            downsizedRegion.convertTo(downsizedRegion, -1, 1.0, -50);
            Imgproc.threshold(downsizedRegion, downsizedRegion, 80, 255, Imgproc.THRESH_OTSU);
            tempval = Core.mean(downsizedRegion);
            Imgproc.putText(input, String.format("%2f", tempval.val[0] ), putText , Imgproc.FONT_HERSHEY_SIMPLEX, 1.0, GREEN );

            if (tempval.val[0] > 40) {
                detectedGreen = true;
                detectedPurp = false;

            } else {
                detectedGreen = false;
            }

            Core.subtract(regionToSampleb, regionToSamplea, downsizedRegion); //Meh Purple
            downsizedRegion.convertTo(downsizedRegion, -1, 2.0, 0);
            Imgproc.threshold(downsizedRegion, downsizedRegion, 100, 255, Imgproc.THRESH_OTSU);
            tempval = Core.mean(downsizedRegion);
            Imgproc.putText(input, String.format("%2f", tempval.val[0] ), putText_Lower , Imgproc.FONT_HERSHEY_SIMPLEX, 1.0, PURPLE );

            if (tempval.val[0] > 40) {
                detectedPurp = true;
                detectedGreen = false;
            } else {
                detectedPurp = false;
            }

            if (detectedPurp) {
                Imgproc.rectangle(input, region, PURPLE, 2, Imgproc.LINE_AA, 0);
            } else if (detectedGreen) {
                Imgproc.rectangle(input, region, GREEN, 2, Imgproc.LINE_AA, 0);
            }
            else {
                Imgproc.rectangle(input, region, GREY, 2, Imgproc.LINE_AA, 0);
            }

            //downsizedRegion = input;

        }


        return input;


    }
}
