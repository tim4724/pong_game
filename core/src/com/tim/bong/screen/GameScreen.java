package com.tim.bong.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Align;
import com.tim.bong.game.actor.Ball;
import com.tim.bong.game.actor.PlayerStick;
import com.tim.bong.game.playercontrol.*;
import com.tim.bong.game.world.GameWorldManager;
import com.tim.bong.game.world.GameWorldNetworkManager;
import com.tim.bong.game.world.MyContactListener;
import com.tim.bong.network.GameDataExchanger;
import com.tim.bong.util.FontHelper;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class GameScreen extends BasicScreen {
    private SpriteBatch spriteBatch;
    private ShapeRenderer shapeRenderer;

    private GameWorldManager worlManager;
    private static float project;
    private PlayerController controller[];

    private Color goalsColor = new Color(0.886f, 0.043f, 0, 1);
    private Color centerLineColor = new Color(0.5f, 0.5f, 0.5f, 1);
    private BitmapFont scoreFont, font;

    private boolean online;

    private boolean renderDebug = false;
    private GameDataExchanger.Stats stats;

    public GameScreen(boolean online, boolean touchControl) {
        super(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new Color(0.03f, 0.03f, 0.03f, 1));
        this.online = online;

        float w = 30;
        float h = w * (heightPx / widthPx);
        project = widthPx / w;

        if (online) {
            worlManager = new GameWorldNetworkManager(w, h);
        } else {
            worlManager = new GameWorldManager(w, h);
        }

        controller = new PlayerController[2];
        if (touchControl) {
            controller[0] = new TouchControl(worlManager.getBottomAnchor(), widthPx, w);
        } else {
            controller[0] = new SensorControl(worlManager.getBottomAnchor(), 100);
        }
        if (online) {
            controller[1] = new NetworkControl(worlManager.getTopAnchor(), worlManager.getTopPlayer());
            ((GameWorldNetworkManager) worlManager).registerNetworkControl((NetworkControl) controller[1]);
        } else {
            controller[1] = new AiControl(worlManager.getTopAnchor(), worlManager.getBall(), w, h);
        }

        scoreFont = FontHelper.getScoreFont(Math.round(15 * project));
        scoreFont.setColor(centerLineColor);
        font = FontHelper.getFont(Math.round(1.5f * project));
        font.setColor(Color.GREEN);

        //initialize render stuff
        spriteBatch = new SpriteBatch(2);
        spriteBatch.setProjectionMatrix(super.camera.combined);

        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
        shapeRenderer.setProjectionMatrix(camera.combined);
    }

    public void init(DatagramSocket socket, InetSocketAddress target, Integer multiplayerId, Integer opponnentId) {
        ((GameWorldNetworkManager) worlManager).init(socket, target, multiplayerId, opponnentId);
    }

    @Override
    public void show() {
        super.show();
        worlManager.getWorld().setContactListener(new MyContactListener(worlManager));
        worlManager.show();
        stats = ((GameWorldNetworkManager) worlManager).getStats();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        controller[0].update(delta);
        controller[1].update(delta);
        worlManager.update(delta);

        spriteBatch.begin();
        renderScore();
        spriteBatch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        renderField();
        renderBall();
        shapeRenderer.end();

        spriteBatch.begin();
        renderPlayers();

        if (online) {
            String s = "cur Ping: " + stats.getRtt()
                    + "\nReceived: " + stats.getPacketsReceived() + "/" + (stats.getPacketsReceived() + stats.getPacketsDropped())
                    + "\nSent: " + stats.getPacketsSent();
            font.draw(spriteBatch, s, 0, (7f * project), (int) (widthPx - Math.round(0.5f * project)), Align.right, true);
        }
        spriteBatch.end();

        if (renderDebug) {
            worlManager.renderDebug();
        }
    }

    private void renderField() {
        float borderThickness = 0.4f * project;

        shapeRenderer.setColor(goalsColor);
        shapeRenderer.rectLine(0, 0, widthPx, 0, borderThickness * 2);
        shapeRenderer.rectLine(0, heightPx, widthPx, heightPx, borderThickness * 2);

        shapeRenderer.setColor(centerLineColor);
        shapeRenderer.rectLine(0, heightPx / 2, widthPx, heightPx / 2, borderThickness);
    }

    private void renderScore() {
        int scoreA = worlManager.getBottomGoal().getScore();
        float y = heightPx / 5;
        scoreFont.draw(spriteBatch, "" + scoreA, 0, y, widthPx, Align.center, true);

        int scoreB = worlManager.getTopGoal().getScore();
        y = heightPx * 4 / 5 - scoreFont.getXHeight();
        scoreFont.draw(spriteBatch, "" + scoreB, 0, y, widthPx, Align.center, true);
    }

    private void renderBall() {
        Ball ball = worlManager.getBall();
        float x = ball.getX() * project;
        float y = ball.getY() * project;
        float radius = ball.getRadius() * project;

        shapeRenderer.setColor(ball.getColor());
        shapeRenderer.circle(x, y, radius);
    }

    private void renderPlayers() {
        renderPlayer(worlManager.getTopPlayer());
        renderPlayer(worlManager.getBottomPlayer());
    }

    private void renderPlayer(PlayerStick p) {
        p.updateSprite();
        p.getSprite().draw(spriteBatch);
    }

    public static float projectToScreen(float value) {
        return value * project;
    }
}
