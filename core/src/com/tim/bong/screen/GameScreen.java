package com.tim.bong.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.tim.bong.game.actor.Ball;
import com.tim.bong.game.playercontrol.AiControl;
import com.tim.bong.game.playercontrol.TouchControl;
import com.tim.bong.game.world.GameWorldManager;
import com.tim.bong.game.world.MyContactListener;

public class GameScreen extends BasicScreen {

    private SpriteBatch spriteBatch;
    private ShapeRenderer shapeRenderer;

    private GameWorldManager worlManager;
    private TouchControl touchControl;
    private AiControl aiControl;

    public GameScreen() {
        super(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Color.BLACK);

        //game world
        float w = 30;
        float h = w * (heightPx / widthPx);
        worlManager = new GameWorldManager(w, h);

        Ball ball = worlManager.getBall();
        touchControl = new TouchControl(worlManager.getBottomAnchor(), ball, widthPx, w);
        aiControl = new AiControl(worlManager.getTopAnchor(), ball);

        //initialize render stuff
        /*
        spriteBatch = new SpriteBatch(2);
        spriteBatch.setProjectionMatrix(super.camera.combined);

        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
        shapeRenderer.setProjectionMatrix(camera.combined);*/
    }

    @Override
    public void show() {
        super.show();
        worlManager.getWorld().setContactListener(new MyContactListener(worlManager));
        worlManager.start();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        touchControl.update(delta);
        aiControl.update(delta);

        worlManager.update(delta);
        worlManager.renderDebug();
    }

    /*
    private void renderField() {
        int borderThickness = 26;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rectLine(0, 0, widthPx, 0, borderThickness * 2);//top
        shapeRenderer.rectLine(0, heightPx, widthPx, heightPx, borderThickness * 2);

        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rectLine(0, heightPx / 2, widthPx, heightPx / 2, borderThickness);//right

        shapeRenderer.end();
    }

    private void renderPlayers() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        renderPlayer(player, Color.BLUE);
        renderPlayer(enemy, Color.BLACK);
        shapeRenderer.end();
    }

    private void renderPlayer(PlayerStick p, Color color) {
        shapeRenderer.setColor(color);
        float thickness = p.getThickness();
        float offset = p.getLen() / 2 - thickness / 2;

        shapeRenderer.circle(p.getX() - offset, p.getY(), thickness / 2);
        shapeRenderer.rectLine(p.getX() - offset, p.getY(), p.getX() + offset, p.getY(), thickness);
        shapeRenderer.circle(p.getX() + offset, p.getY(), thickness / 2);
    }

    private void renderBall() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.PURPLE);

        shapeRenderer.circle(ball.getX(), ball.getY(), ball.getRadius());

        shapeRenderer.end();
    }

*/
}
