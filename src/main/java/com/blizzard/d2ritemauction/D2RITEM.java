package com.blizzard.d2ritemauction;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "D2R_ITEM")
public class D2RITEM {

	private String battleCode;
	private int time;
	private String rarity;
	private String category;
	private String name;
	private int count;
	private String[] sellingprice;
	private String ladder;
	private String hardcore;
	
	

	
	@DynamoDBHashKey(attributeName = "battleCode")
	public String getBattleCode() {
		return battleCode;
	}
	public void setBattleCode(String battleCode) {
		this.battleCode = battleCode;
	}
	
	@DynamoDBRangeKey(attributeName = "time")
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	

    @DynamoDBAttribute(attributeName = "rarity")
	public String getRarity() {
		return rarity;
	}
	public void setRarity(String rarity) {
		this.rarity = rarity;
	}
	@DynamoDBAttribute(attributeName = "category")
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	@DynamoDBAttribute(attributeName = "name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@DynamoDBAttribute(attributeName = "count")
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	@DynamoDBAttribute(attributeName = "sellingprice")
	public String[] getSellingprice() {
		return sellingprice;
	}
	public void setSellingprice(String[] sellingprice) {
		this.sellingprice = sellingprice;
	}
	@DynamoDBAttribute(attributeName = "ladder")
	public String getLadder() {
		return ladder;
	}
	public void setLadder(String ladder) {
		this.ladder = ladder;
	}
	@DynamoDBAttribute(attributeName = "hardcore")
	public String getHardcore() {
		return hardcore;
	}
	public void setHardcore(String hardcore) {
		this.hardcore = hardcore;
	}
	
	
	

	
	
	
	
}
