package net.sourceforge.fenixedu.presentationTier.gwt.frameworks.Raphael.client;

public abstract class AnimationCallback {
  public abstract void onComplete();
  static public void fire(AnimationCallback c) {
	c.onComplete();
  }
}

