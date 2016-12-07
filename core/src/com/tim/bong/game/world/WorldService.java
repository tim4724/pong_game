package com.tim.bong.game.world;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointDef;

public class WorldService {
    private static WorldService inst;

    private GameWorldManager worldManager;

    public static WorldService getInst() {
        return inst;
    }

    public static WorldService createInst(GameWorldManager worldManager) {
        return new WorldService(worldManager);
    }

    private WorldService(GameWorldManager worldManager) {
        inst = this;
        this.worldManager = worldManager;
    }

    public Body createBody(BodyDef bodyDef) {
        return worldManager.getWorld().createBody(bodyDef);
    }

    public Joint createJoint(JointDef jointDef) {
        return worldManager.getWorld().createJoint(jointDef);
    }

    public void registerUpdatable(UpdatAble updatAble) {
        worldManager.registerForUpdate(updatAble);
    }

    public float getWidth() {
        return worldManager.getWidth();
    }

    public float getHeight() {
        return worldManager.getHeight();
    }

}
