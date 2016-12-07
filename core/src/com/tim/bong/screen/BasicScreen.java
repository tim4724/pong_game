package com.tim.bong.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class BasicScreen implements Screen {

    final float widthPx, heightPx;
    OrthographicCamera camera;
    private Color backGround;

    public BasicScreen(float widthPx, float heightPx, Color backGround) {
        this.widthPx = widthPx;
        this.heightPx = heightPx;
        this.backGround = backGround;

        camera = new OrthographicCamera();
        camera.setToOrtho(true, widthPx, heightPx);

        Gdx.gl.glClearColor(backGround.r, backGround.g, backGround.b, backGround.a);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glClearColor(backGround.r, backGround.g, backGround.b, backGround.a);//TODO: do i need this call
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }
}
