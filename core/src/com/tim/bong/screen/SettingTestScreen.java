package com.tim.bong.screen;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.tim.bong.MyGame;

/**
 * Created by Tim on 18.12.2016.
 */
public class SettingTestScreen extends BasicScreen {
    public SettingTestScreen(final MyGame game) {
        super(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new Color(0.03f, 0.03f, 0.03f, 1));

        Gdx.input.getTextInput(new Input.TextInputListener() {
            @Override
            public void input(final String text) {
                Gdx.app.postRunnable(new Runnable(){
                    public void run(){
                        game.setScreen(new GameScreen(Integer.parseInt(text), Gdx.app.getType() != Application.ApplicationType.Android));
                    }
                });
            }

            @Override
            public void canceled() {
                Gdx.app.postRunnable(new Runnable(){
                    public void run(){
                        game.setScreen(new GameScreen(null, Gdx.app.getType() != Application.ApplicationType.Android));
                    }
                });            }
        }, "Online Multiplayer", "", "player id");


    }

    @Override
    public void show() {
        super.show();
    }
}
