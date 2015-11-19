package myGameEngine;

import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;
import sage.display.IDisplaySystem;
import sage.scene.state.TextureState;
import sage.terrain.AbstractHeightMap;
import sage.terrain.ImageBasedHeightMap;
import sage.terrain.TerrainBlock;
import sage.texture.Texture;
import sage.texture.TextureManager;



public class MyTerrain {

	private String name;
	
	public TerrainBlock initTerrain(IDisplaySystem display){
		ImageBasedHeightMap myHeightMap = new ImageBasedHeightMap("src/Textures/Ground/map1GroundFile.png");
		TerrainBlock imageTerrain = createTerBlock(myHeightMap);
		//TODO fix textures
		/*
		TextureState grassState;
		Texture grassTexture = TextureManager.loadTexture2D("texturez_4758.jpg");
		grassTexture.setApplyMode(sage.texture.Texture.ApplyMode.Replace);
		grassState = (TextureState) display.getRenderer().createRenderState(RenderState.RenderStateType.Texture);
		
		grassState.setTexture(grassTexture,0);
		grassState.setEnabled(true);
		imageTerrain.setRenderState(grassState);
		*/
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
