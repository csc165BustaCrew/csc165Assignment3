package myGameEngine;

import game.ImageBasedHeightMap;
import game.TerrainBlock;

public class MyTerrain {

	private String name;
	
	public TerrainBlock initTerrain(){
		ImageBasedHeightMap myHeightMap = new ImageBasedHeightMap("src/Textures/Maps/map1GroundFile.png");
		TerrainBlock imageTerrain = createTerBlock(myHeightMap);
		
		//TODO add texture for map
		/*
		TextureState grassState;
		Texture GrassTexture = TextureManager.loadTexture2D("");
		grassTexture.setApplyMode(sage.texture.Texture.ApplyMode.Replace);
		grassState = (TextureState);
		
		imageTerrain.setRenderState(grassState);
		*/

		return imageTerrain;
	}
	
	private TerrainBlock createTerBlock(AbstractHeightMap heightMap){
		float heightScale = 0.005f;
		Vector3D terrainScale = new Vector3D(0.2, heightScale, 0.2);
		int terrainSize = heightMap.getSize();
		float cornerHeight = heightMap.getTrueHeightAtPoint(0,0) * heightScae;
		Point3D terrainOrigin = new Point3D(0, -cornerHeight, 0);
		
		name = "Terrain:" + heightMap.getClass().getSimpleName();
		TerrainBlock tb = new TerrainBlock(name, terrainSize, terrainScale, heightMap.getHeightData(), terrainOrigin);
		
		return tb;
	}
	
	
}
