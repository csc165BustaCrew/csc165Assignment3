package myGameEngine;

import java.nio.FloatBuffer;

import sage.event.IEventListener;
import sage.event.IGameEvent;
import sage.scene.shape.Pyramid;
import sage.util.ColorRGBA;

public class MyPyramid extends Pyramid implements IEventListener{
	int crashCount = 0;
	float[] color1 = new float[] {1,0,0,1,1,0,0,1,1,0,0,1,1,0,0,1,1,0,0,1,1,0,0,1,1,0,0,1,1,0,0,1,1,0,0,1};
	float[] color2 = new float[] {1,1,0,1,1,1,0,1,1,1,0,1,1,1,0,1,1,1,0,1,1,1,0,1,1,1,0,1,1,1,0,1,1,1,0,1};
	FloatBuffer colorBuffer1 = this.getColorBuffer();
	FloatBuffer colorBuffer2 = com.jogamp.common.nio.Buffers.newDirectFloatBuffer(color2);
	
	@Override
	public boolean handleEvent(IGameEvent e) {
		CrashEvent cevent = (CrashEvent)e;
		this.setColorBuffer(colorBuffer2);
		return true;
	}
	public void setDefaultColor(){
		this.setColorBuffer(colorBuffer1);
	}
}
