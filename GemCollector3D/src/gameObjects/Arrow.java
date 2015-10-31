package gameObjects;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import sage.scene.TriMesh;

public class Arrow extends TriMesh{
	private static float[] verts = new float[]
			{
			0, 0, 0, 	//0 //head
			-2, 0, 0, 	//1
			0, 4, 0.5f,  	//2
			2, 0, 0,	//3 
			1, 0, 0,	//4 //body
			1, -4, 0,	//5
			-1, -4, 0,	//6
			-1, 0, 0,	//7
			-1, -4, 0,	//8 //right wall
			-1, 0, 1, 	//9
			-1, -4, 1,	//10
			 1, -4, 0,	//11 //left wall
			 1, 0, 1, 	//12
			 1, -4, 1,	//13
			 0, 0, 1, 	//14 //back head
			-2, 0, 1, 	//15
			 2, 0, 1    //16
			};
	
	private static float[] colors = new float[]
			{
			1, 0, 0, 1,	//0
			0, 1, 0, 1, //1
			1, 1, 0, 1, //2
			1, 1, 0, 1,	//3
			1, 0, 0, 1, //4
			0, 1, 0, 1, //5
			1, 1, 0, 1,	//6
			1, 0, 0, 1, //7
			0, 1, 0, 1, //8
			1, 1, 0, 1,	//9
			1, 0, 0, 1, //10
			1, 0, 0, 1,	//11
			0, 1, 0, 1, //12
			1, 1, 0, 1, //13
			1, 1, 0, 1,	//14
			1, 1, 0, 1, //15
			0, 1, 0, 1 //16
			};
	
	private static int[] triangles = new int[]
			{ 
			0, 1, 2, //left head 
			0, 3, 2, //right head
			4, 5, 6, //body
			4, 7, 6, 
			7, 8, 9, //right wall
			9, 10, 8, 
			4, 5, 12, //left wall
			12, 13, 5,
			12, 13, 9, //back wall
			9, 10, 13,
			14, 15, 2, //back head
			14, 16, 2,
			1, 2, 15,	//sides of head
			3, 2, 16,
			5, 6, 10, //base
			5, 14, 10
			};
	
	public Arrow(){
		 FloatBuffer vertBuf =
		 com.jogamp.common.nio.Buffers.newDirectFloatBuffer(verts);
		 FloatBuffer colorBuf =
		 com.jogamp.common.nio.Buffers.newDirectFloatBuffer(colors);
		 IntBuffer triangleBuf =
		 com.jogamp.common.nio.Buffers.newDirectIntBuffer(triangles);
		 this.translate(0, 1, 0);
		 this.setVertexBuffer(vertBuf);
		 this.setColorBuffer(colorBuf);
		 this.setIndexBuffer(triangleBuf); 
	 } 
	

	
}
