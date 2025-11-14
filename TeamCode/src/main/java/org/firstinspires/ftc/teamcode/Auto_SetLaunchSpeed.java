package org.firstinspires.ftc.teamcode;


public class Auto_SetLaunchSpeed extends State {

    public Launcher launcher;
    public int targetSpeed;

    public Auto_SetLaunchSpeed(Launcher launcher, int targetSpeed) {
        this.launcher = launcher;
        this.targetSpeed = targetSpeed;
    }

    public boolean truefalse(){
        this.action();
        return true;
    }

    public void action(){
        if (targetSpeed == Launcher.MODE_STOP) {
            launcher.setLauncherStop();
        } else if (targetSpeed == Launcher.MODE_SLOW) {
            launcher.setLauncherSlow();
        } else if (targetSpeed == Launcher.MODE_MID) {
            launcher.setLauncherMedium();
        } else if (targetSpeed == Launcher.MODE_FAST) {
            launcher.setLauncherFast();
        } else  {
            launcher.setLauncherStop();
        }

    }

    public String readStateData() {
        return "set speed state";
    }


}
