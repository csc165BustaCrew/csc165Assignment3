package myGameEngine;
import java.awt.Canvas;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;

import sage.display.*;
import sage.renderer.IRenderer;
import sage.renderer.RendererFactory;

public class MyDisplaySystem implements IDisplaySystem{
	private JFrame myFrame;
	private GraphicsDevice device;
	private IRenderer myRenderer;
	private int width, height, bitDepth, refreshRate;
	private Canvas rendererCanvas;
	private boolean isCreated;
	private boolean isFullScreen;
	private String title;
	
	public MyDisplaySystem(int w, int h, int depth, int rate, boolean isFS, String rName){
		this.setWidth(w);
		this.setHeight(h);
		this.setBitDepth(depth);
		this.setRefreshRate(rate);
		isFullScreen = isFS;
		
		myRenderer = RendererFactory.createRenderer(rName);
		if(myRenderer == null){
			throw new RuntimeException("Unable to find renderer");
		}
		rendererCanvas = myRenderer.getCanvas();
		myFrame = new JFrame("Gemcollector3D");
		myFrame.add(rendererCanvas);
		
		DisplayMode displayMode = new DisplayMode(width, height, bitDepth, refreshRate);
		initScreen(displayMode, isFullScreen);
		DisplaySystem.setCurrentDisplaySystem(this);
		myFrame.setVisible(true);
		isCreated = true;
	}
	private void initScreen(DisplayMode dispMode, boolean FSRequested){
		GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		device = environment.getDefaultScreenDevice();
		
		if(device.isFullScreenSupported() && FSRequested){
			myFrame.setUndecorated(true);
			myFrame.setResizable(false);
			myFrame.setIgnoreRepaint(true);
			device.setFullScreenWindow(myFrame);
			
			if(dispMode != null && device.isDisplayChangeSupported()){
				try{
					device.setDisplayMode(dispMode);
					myFrame.setSize(dispMode.getWidth(), dispMode.getHeight());
				}catch(Exception e){
					System.err.println("Exception setting DisplayMode: " + e);
				}
			}else{
				System.err.println("Cannot set display mode");
			}	
		}else{
			myFrame.setSize(dispMode.getWidth(), dispMode.getHeight());
			myFrame.setLocationRelativeTo(null);
		}
	}
	
	public void addKeyListener(KeyListener arg0) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void addMouseListener(MouseListener arg0) {
		// TODO Auto-generated method stub
	}
	@Override
	
	public void addMouseMotionListener(MouseMotionListener arg0) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void close() {
		if(device != null){
			Window window = device.getFullScreenWindow();
			if(window != null){
				window.dispose();
			}
			device.setFullScreenWindow(null);
		}
	}
	
	@Override
	public void convertPointToScreen(Point arg0) {
		// TODO Auto-generated method stub
	}
	@Override
	public int getBitDepth() {
		return bitDepth;
	}
	@Override
	public int getHeight() {
		return height;
	}
	@Override
	public int getRefreshRate() {
		return refreshRate;
	}
	@Override
	public IRenderer getRenderer() {
		return myRenderer;
	}
	@Override
	public int getWidth() {
		return width;
	}
	@Override
	public boolean isCreated() {
		return isCreated;
	}
	@Override
	public boolean isFullScreen() {
		return isFullScreen;
	}
	@Override
	public boolean isShowing() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void setBitDepth(int arg0) {
		this.bitDepth = arg0;
	}
	@Override
	public void setCustomCursor(String arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setHeight(int arg0) {
		this.height = arg0;
	}
	@Override
	public void setPredefinedCursor(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRefreshRate(int arg0) {
		this.refreshRate = arg0;
	}

	@Override
	public void setTitle(String arg0) {
		this.title = arg0;
	}

	@Override
	public void setWidth(int arg0) {
		this.width = arg0;
	}

}
