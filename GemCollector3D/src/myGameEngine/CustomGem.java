package myGameEngine;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import sage.scene.TriMesh;

public class CustomGem extends TriMesh{
	
	public void setGem(){
		float[] floatArray = new float[] {0,0,0, 1,0,0, 0,1,0, 1,1,0, 1,(float) 0.5,1, 0,(float) 0.5,1};
		FloatBuffer vertBuf = FloatBuffer.wrap(floatArray);
		int[] intArray = new int[] {0,1,2, 2,1,3, 
									2,3,4, 2,5,4, 
									2,0,5, 
									0,5,4, 0,1,4,
									1,3,4};
		IntBuffer intBuf = IntBuffer.wrap(intArray);
		setVertexBuffer(vertBuf);
		setNormalBuffer(vertBuf);
		setIndexBuffer(intBuf);
	}
}
