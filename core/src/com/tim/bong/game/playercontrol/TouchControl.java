package com.tim.bong.game.playercontrol;

import com.badlogic.gdx.Gdx;
import com.tim.bong.game.actor.StickAnchor;

public class TouchControl extends PlayerController {
    private float projectX;

    public TouchControl(StickAnchor anchor, float widthPx, float width) {
        super(anchor);
        projectX = (width / widthPx);
        //Gdx.input.setInputProcessor(inputAdapter);
    }

    @Override
    public void update(float deltaT) {
        super.update(deltaT);

        if (Gdx.input.isTouched(0)) {
            float x = Gdx.input.getX(0) * projectX;
            anchor.updatePos(x);
        }
    }

    /*
    private final InputAdapter inputAdapter = new InputAdapter() {
        private boolean touchEvent(float screenX, int pointer) {
            if (pointer == 0) {
                anchor.updatePos(screenX * projectX);
                return true;
            }
            return false;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            return touchEvent(screenX, pointer);
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            return touchEvent(screenX, pointer);
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            return touchEvent(screenX, pointer);
        }
    };*/
}
