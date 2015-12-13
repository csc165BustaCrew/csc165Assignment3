package myGameEngine;

import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;
import sage.display.IDisplaySystem;
import sage.scene.state.RenderState.RenderStateType;
import sage.scene.state.TextureState;
import sage.terrain.AbstractHeightMap;
import sage.terrain.ImageBasedHeightMap;
import sage.terrain.TerrainBlock;
import sage.texture.Texture;
import sage.texture.TextureManager;



public class MyTerrain {

	private String name;
	
	public TerrainBlock initTerrain(IDisplaySystem display){
		ImageBasedHeightMap myHeightMap = new ImageBasedHeightMap("src/Textures/Ground/roadWIPGroundFile.png");
		TerrainBlock imageTerrain = createTerBlock(myHeightMap);

		TextureState roadState;
		Texture roadTexture = TextureManager.loadTexture2D("src/Textures/Ground/road.jpg");
		roadTexture.setApplyMode(sage.texture.Texture.ApplyMode.Replace);
		roadState = (TextureState) display.getRenderer().createRenderState(RenderStateType.Texture);
		
		roadState.setTexture(roadTexture,0);
		roadState.setEnabled(true);
		imageTerrain.setRenderState(roadState);

		return imageTerrain;
	}
	
	private TerrainBlock createTerBlock(AbstractHeightMap heightMap){
		//float heightScale = 0.005f;
		float heightScale = 0.05f;
		//Vector3D terrainScale = new Vector3D(1, 0, 1);
		Vector3D terrainScale = new Vector3D(1, heightScale, 1);
		int terrainSize = heightMap.getSize();
		float cornerHeight = heightMap.getTrueHeightAtPoint(0,0) * heightScale;
		Point3D terrainOrigin = new Point3D(0, -cornerHeight, 0);
		
		name = "Terrain:" + heightMap.getClass().getSimpleName();
		TerrainBlock tb = new TerrainBlock(name, terrainSize, terrainScale, heightMap.getHeightData(), terrainOrigin);
		
		return tb;
	}
	
	
}
