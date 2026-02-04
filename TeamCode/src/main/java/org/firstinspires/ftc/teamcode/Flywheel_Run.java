/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.hardware.motors.CRServo;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.Motor.Encoder;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.List;


/*
 * This file contains an example of an iterative (Non-Linear) "OpMode".
 * An OpMode is a 'program' that runs in either the autonomous or the teleop period of an FTC match.
 * The names of OpModes appear on the menu of the FTC Driver Station.
 * When a selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all iterative OpModes contain.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this OpMode to the Driver Station OpMode list
 */

@TeleOp(name="Flywheel_Run", group="Iterative OpMode")
@Config
public class Flywheel_Run extends OpMode
{
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    List<LynxModule> allHubs = null;
    LynxModule ControlHub = null;
    LynxModule ExpansionHub = null;

    public static int RPM_SETPOINT = 1000;

    private Flywheel_Config flywheel = null;
    private Flywheel_Config flywheel1 = null;

    private Wheel Indexer=null;
    private CRServo Servo1=null;
    private CRServo Servo2=null;
    private Servo ServoRight=null;
    private Servo ServoLeft=null;
    private LaunchSelector LaunchSelector=null;
    private Motor WheelEncoder=null;

    private boolean x_pressed = false;

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        allHubs = hardwareMap.getAll(LynxModule.class);
        for (LynxModule hub : allHubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
            if (hub.isParent()) {
                ControlHub = hub;
            } else {
                ExpansionHub = hub;
            }
        }

        flywheel = new Flywheel_Config(ControlHub, hardwareMap, "flywheel");
        flywheel1 = new Flywheel_Config(ControlHub, hardwareMap, "flywheel1");
        Servo1=new CRServo(hardwareMap, "Servo1");
        Servo2=new CRServo(hardwareMap, "Servo2");
        ServoRight=hardwareMap.get(Servo.class, "ServoRight");
        ServoLeft=hardwareMap.get(Servo.class, "ServoLeft");
        WheelEncoder=new Motor(hardwareMap, "WheelEncoder");
        //Indexer=new Wheel(Servo1, Servo2, WheelEncoder.encoder);
        LaunchSelector = new LaunchSelector(ServoRight, ServoLeft);
        telemetry.addData("Status", "Initialized");

    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit START
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits START
     */
    @Override
    public void start() {
        runtime.reset();
    }

    /*
     * Code to run REPEATEDLY after the driver hits START but before they hit STOP
     */
    @Override
    public void loop() {

        for (LynxModule hub : allHubs) {
            hub.clearBulkCache();
        }

        long curr_time = runtime.nanoseconds();

        if (gamepad1.left_bumper) {
            flywheel.setRPM(RPM_SETPOINT);
            flywheel1.setRPM(RPM_SETPOINT);
        } else {
            flywheel.setPower(0);
            flywheel1.setPower(0);
        }
        if (gamepad1.right_bumper) {
            Indexer.toggleWheelForwardForce();
        }
        if (gamepad1.x && !x_pressed) {
            LaunchSelector.ToggleLauncher();
            x_pressed = true;
        } else if (!gamepad1.x) {
            x_pressed = false;
        }

        double rpm = flywheel.getVelocity() * 60 / 28;
        Indexer.run();

        telemetry.addData("RPM Value: ", rpm);
        telemetry.addData("Power: ", flywheel.getPower());
        telemetry.addData("Avg V: ", flywheel.getAverageVoltage());
        telemetry.addData("Time: ", curr_time);
        telemetry.addData("X: ", x_pressed);

        telemetry.update();

    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }

}
