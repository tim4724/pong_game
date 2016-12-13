package com.tim.bong.game.actor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.tim.bong.screen.GameScreen;
import com.tim.bong.util.convenience.MyBodyDef;
import com.tim.bong.util.convenience.MyFixtureDef;


public class PlayerStick extends BasicActor {
    private final static float standartLen = 10f;
    private final static float standartThickness = 1.6f;

    private float len;
    private final static Texture tx = new Texture("stick.png");
    private Sprite sprite;

    public PlayerStick(boolean top) {
        super();
        this.len = standartLen;
        setBody(createPlayerBody(len, standartThickness));

        float x = worldService.getWidth() / 2;
        float height = worldService.getHeight();
        if (top) {
            getBody().setTransform(x, 5, MathUtils.PI / 2);
        } else {
            getBody().setTransform(x, height - 5, -MathUtils.PI / 2);
        }

        sprite = new Sprite(tx);
        sprite.setScale(GameScreen.projectToScreen(len) / sprite.getWidth(), GameScreen.projectToScreen(getThickness()) / sprite.getHeight());
        sprite.setOriginCenter();
    }

    public void updateSprite() {
        sprite.setCenter(GameScreen.projectToScreen(getX()), GameScreen.projectToScreen(getY()));
        float rot = (getAngle() + MathUtils.PI / 2) * MathUtils.radDeg;
        sprite.setRotation(rot);
    }

    public float getThickness() {
        return standartThickness * (len / PlayerStick.standartLen);
    }

    public float getLen() {
        return len;
    }

    private Body createPlayerBody(float height, float width) {
        BodyDef stickBodDef = new MyBodyDef(BodyDef.BodyType.DynamicBody);

        PolygonShape box = new PolygonShape();
        box.setAsBox(width / 2, (height - width) / 2);
        CircleShape circle = new CircleShape();
        circle.setRadius(width / 2);//something is wrong but hey it works

        FixtureDef rect = new MyFixtureDef(box, 1, 0, 0);
        FixtureDef leftCircle = new MyFixtureDef(circle, 1, 0, 0);
        FixtureDef rightCircle = new MyFixtureDef(circle, 1, 0, 0);

        Body body = worldService.createBody(stickBodDef);
        body.createFixture(rect);
        circle.setPosition(new Vector2(0, +(height - width) / 2));
        body.createFixture(leftCircle);
        circle.setPosition(new Vector2(0, -(height - width) / 2));
        body.createFixture(rightCircle);

        box.dispose();
        circle.dispose();

        return body;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public float getAngle() {
        return getBody().getAngle();
    }
}
