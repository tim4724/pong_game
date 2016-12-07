package com.tim.bong.game.actor;


import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.tim.bong.util.convenience.MyBodyDef;

public class Goal extends BasicActor {
    private int score;

    public Goal(boolean top) {
        super();
        setBody(createGoalBody());

        float x = worldService.getWidth() / 2;
        if (top) {
            setPos(x, -3);
        } else {
            setPos(x, worldService.getHeight() + 3);
        }
    }

    public void goalScored() {
        score++;
    }

    private Body createGoalBody() {
        BodyDef bodyDef = new MyBodyDef(BodyDef.BodyType.StaticBody, 0, 0);
        PolygonShape box = new PolygonShape();
        box.setAsBox(worldService.getWidth(), 0);
        Body goalBody = worldService.createBody(bodyDef);
        goalBody.createFixture(box, 0.0f);

        box.dispose();
        return goalBody;
    }
}
