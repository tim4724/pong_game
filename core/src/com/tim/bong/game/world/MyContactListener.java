package com.tim.bong.game.world;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.tim.bong.game.actor.Ball;
import com.tim.bong.game.actor.Goal;

public class MyContactListener implements ContactListener {
    private GameWorldManager worldManager;

    public MyContactListener(GameWorldManager worldManager) {
        this.worldManager = worldManager;
    }

    @Override
    public void beginContact(Contact contact) {
    }

    @Override
    public void endContact(Contact contact) {
        Object a = contact.getFixtureA().getBody().getUserData();
        Object b = contact.getFixtureB().getBody().getUserData();

        if (a == null || b == null) {
            return;
        }

        Goal goal = goalScored(a, b);
        if (goal != null) {
            goal.goalScored();
            worldManager.reset();
        }
    }

    private Goal goalScored(Object a, Object b) {
        if (a instanceof Goal && b instanceof Ball) {
            return ((Goal) a);
        }
        if (a instanceof Ball && b instanceof Goal) {
            return ((Goal) b);
        }
        return null;
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
