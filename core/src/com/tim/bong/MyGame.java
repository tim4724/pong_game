package com.tim.bong;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.tim.bong.screen.SettingTestScreen;

public class MyGame extends Game {

    @Override
    public void create() {
        Gdx.input.setCatchBackKey(true);
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        laodGameScreen();
    }

    private void laodGameScreen() {
        SettingTestScreen startScreen = new SettingTestScreen(this);
        setScreen(startScreen);
    }
}
