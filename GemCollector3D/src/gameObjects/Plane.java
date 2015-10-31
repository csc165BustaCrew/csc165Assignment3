package gameObjects;

import java.awt.Color;

import sage.event.IEventListener;
import sage.event.IGameEvent;
import sage.renderer.IRenderer;
import sage.scene.shape.Rectangle;

public class Plane extends Rectangle implements IEventListener{
	private boolean anim = false;
	private boolean isIncreasing = true;
	private Color color;
	
	private float r = 0;
	private float g = 0;
	private float b = 0;
	Color tempColor;
	public Plane(Color color){
		this.color = color;
	}
	
	public void draw(IRenderer rend){
		super.draw(rend);
		if(anim){
			
			if(isIncreasing){
				r += 01f;
				if(r > .99f){
					isIncreasing = false;
				}
			}
			else if(r > 0.01 && !isIncreasing){
				r -= 0.01f;
			}
			else{
				anim = false;
				isIncreasing = true;
				r = 0f;
			}
			tempColor = new Color(r,g,b);

			this.setColor(tempColor);
		}
		else{
			this.setColor(color);
		}
		
	}
	
	@Override
	public boolean handleEvent(IGameEvent arg0) {
		anim = true;
		return true;
	}

}
