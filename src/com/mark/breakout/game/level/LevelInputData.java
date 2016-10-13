package com.mark.breakout.game.level;

public class LevelInputData {

	public String getTileType() {
		return tileType;
	}
	public void setTileType(String tileType) {
		this.tileType = tileType;
	}
	public int getRowNumber() {
		return rowNumber;
	}
	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}
	public int getStartX() {
		return startX;
	}
	public void setStartX(int startX) {
		this.startX = startX;
	}
	public int getStartY() {
		return startY;
	}
	public void setStartY(int startY) {
		this.startY = startY;
	}
	private String tileType;
	private int rowNumber;
	private int startX;
	private int startY;
	
}
