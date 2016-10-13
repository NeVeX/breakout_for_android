package com.mark.breakout.game.entity;

import android.util.Log;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mark.breakout.debug.LogConstants;
import com.mark.breakout.game.entity.tiles.BaseTile;

public class CollisionListener implements ContactListener {

	@Override
	public void beginContact(Contact contact) {
		Sphere sphere = null;
		Paddle paddle = null;
		Body bodyA = contact.getFixtureA().getBody();
		Body bodyB = contact.getFixtureB().getBody();
		Object bodyObjectA = bodyA == null ? null : bodyA.getUserData();
		Object bodyObjectB = bodyB == null ? null : bodyB.getUserData();
		
		if ( bodyObjectA instanceof Sphere)
		{
			sphere = (Sphere) bodyObjectA;
		}
		else if ( bodyObjectA instanceof Paddle)
		{
			paddle = (Paddle) bodyObjectA;
		}
		if ( bodyObjectB instanceof Sphere)
		{
			sphere = (Sphere) bodyObjectB;
		}
		else if ( bodyObjectB instanceof Paddle)
		{
			paddle = (Paddle) bodyObjectB;
		}
		// check if we have the necessary objects for ball and paddle
		if ( paddle != null && sphere != null)
		{
			sphere.performPaddleCollisionLogic(paddle);
		}
	}

	@Override
	public void endContact(Contact contact) {
		Body bodyA = contact.getFixtureA().getBody();
		Body bodyB = contact.getFixtureB().getBody();
		if ( bodyA != null && bodyA.getUserData() instanceof BaseTile)
		{
			this.performSphereWithBaseTileCollision((BaseTile) bodyA.getUserData());
			return;
		}
		if ( bodyB != null && bodyB.getUserData() instanceof BaseTile)
		{
			this.performSphereWithBaseTileCollision((BaseTile) bodyB.getUserData());
			return;
		}
		
	}
	
	
	private void performSphereWithBaseTileCollision(BaseTile bt)
	{
		if ( bt != null )
		{
			bt.performTileCollision();
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}
	

}
