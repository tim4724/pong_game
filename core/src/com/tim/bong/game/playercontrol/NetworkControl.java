package com.tim.bong.game.playercontrol;

import com.tim.bong.game.actor.PlayerStick;
import com.tim.bong.game.actor.StickAnchor;

public class NetworkControl extends PlayerController {
    private PlayerStick playerStick;
    private float x, angle;
    private boolean angleUpdated;

    public NetworkControl(StickAnchor anchor, PlayerStick playerStick) {
        super(anchor);
        this.playerStick = playerStick;
        x = playerStick.getX();
        angle = playerStick.getAngle();
    }

    public void updateStickPosition(float newX) {
        x = newX;
    }

    public void updateStickAngle(float newAngle) {
        angle = newAngle;
        angleUpdated = true;
    }

    @Override
    public void update(float deltaT) {
        anchor.updatePos(x);
        if (angleUpdated) {
            playerStick.setAngle(angle);
            angleUpdated = false;
        }
    }
}
