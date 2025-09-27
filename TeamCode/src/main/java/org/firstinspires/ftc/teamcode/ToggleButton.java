package org.firstinspires.ftc.teamcode;

public class ToggleButton {

    public boolean state = false;
    public boolean rapidtoggleprotection = false;

    public ToggleButton() {

    }

    public void toggle() {
        if ( !rapidtoggleprotection ) {
            state = !state;
            rapidtoggleprotection = true;
        }
    }

    public void clear() {
        rapidtoggleprotection = false;
    }
}
