package com.tim.bong.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.tim.bong.MyGame;
import com.tim.bong.util.FontHelper;
import de.tim.udp_connector.PeerConnection;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class SettingTestScreen extends BasicScreen {
    private final MyGame game;
    private Integer myId;
    private BitmapFont font;
    private SpriteBatch spriteBatch;
    private String process = "";
    private float project;

    private GameScreen gameScreen;

    public SettingTestScreen(final MyGame game) {
        super(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new Color(Color.WHITE));
        this.game = game;

        project = widthPx / 1080;
        font = FontHelper.getFont(Math.round(50 * project));
        font.setColor(Color.BLACK);

        spriteBatch = new SpriteBatch(2);
        spriteBatch.setProjectionMatrix(super.camera.combined);

        /*
        int a = 5004;
        int b = 5005;
        try {
            myId = a;
            DatagramSocket socket = new DatagramSocket(a);
            connectionListener.onSuccess(b, socket, new InetSocketAddress(InetAddress.getByName("127.0.0.1"), b));
        } catch (Exception e) {
            e.printStackTrace();
            try {
                myId = b;
                DatagramSocket socket = new DatagramSocket(b);
                connectionListener.onSuccess(a, socket, new InetSocketAddress(InetAddress.getByName("127.0.0.1"), a));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }*/


        Gdx.input.getTextInput(new Input.TextInputListener() {
            @Override
            public void input(final String text) {
                myId = Integer.parseInt(text);
                try {

                    InetSocketAddress serverAddress = new InetSocketAddress(InetAddress.getByName("tv_test.dd-dns.de"), 3845);

                    PeerConnection connection = new PeerConnection(serverAddress, myId, connectionListener);
                    connection.connect(-myId);

                    log("Trying to connect to " + -myId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void canceled() {
                Gdx.app.postRunnable(new Runnable() {
                    public void run() {
                        game.setScreen(new GameScreen(false, true));
                    }
                });
            }
        }, "Online Multiplayer", "", "player id");
    }

    private final PeerConnection.Listener connectionListener = new PeerConnection.Listener() {
        @Override
        public void onTimout(int i) {
            log("Failed to connect to other peer");
        }

        @Override
        public void onSuccess(final int id, final DatagramSocket socket, final InetSocketAddress inetSocketAddress) {
            try {
                socket.setSoTimeout(1000);
            } catch (Exception e) {
                log(e.getMessage());
            }

            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    gameScreen = new GameScreen(true, true);
                    gameScreen.init(socket, inetSocketAddress, myId, id);
                    game.setScreen(gameScreen);
                }
            });
        }
    };

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        spriteBatch.begin();
        font.draw(spriteBatch, process, (20 * project), (40 * project), (int) (widthPx - Math.round(20 * project)), Align.left, true);
        spriteBatch.end();
    }

    private void log(String log) {
        this.process = this.process + "\n\n" + log;
    }
}
