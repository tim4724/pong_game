package com.tim.bong.game.playercontrol;

import com.badlogic.gdx.Gdx;
import com.tim.bong.game.actor.StickAnchor;

public class TouchControl extends PlayerController {
    private float projectX;

    public TouchControl(StickAnchor anchor, float widthPx, float width) {
        super(anchor);
        projectX = (1 / widthPx) * width;
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        if (Gdx.input.isTouched(0)) {
            float x = Gdx.input.getX(0) * projectX;
            anchor.setPos(x, anchor.getY());
        }
    }
}
