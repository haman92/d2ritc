package com.blizzard.d2ritemauction;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.ScanOutcome;
//import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.document.Index;


@Service
public class DynamoDBService {
	@Autowired
	private AmazonDynamoDB amazonDynamoDB;

	//@Autowired
	//private DynamoDBMapper mapper;

	@Autowired
	private DynamoDB dynamoDB;



	Table table;

	public void getTableList()
	{
		//System.out.println("tablename");
		for(Table temp : dynamoDB.listTables())
		{
			//System.out.println(temp.getTableName());
		}
	}
	public boolean createitem(HashMap<String,String> map)
	{
		try
		{
			table  = dynamoDB.getTable("D2R_ITEM");
			Item item = new Item();
				
			item.withPrimaryKey("BattleTag",map.remove("battletag"),"InputTime",map.remove("InputTime"));
			
				for(String modifier : map.keySet())
				{
					
					if(modifier.equals("itemname")||
							modifier.equals("category")||
							modifier.equals("identifier")||
							modifier.equals("itemcomment")||
							modifier.equals("ladder")||
							modifier.equals("hardcore")||
							modifier.equals("sellcategory")||
							modifier.equals("sellingprice")||
							modifier.equals("rarity")||
							modifier.equals("runewordname")||
							modifier.equals("description"))
					{
					

						String value = map.get(modifier);
						item.withString(modifier, value);
					}else
					{
						Double a = Double.parseDouble(String.valueOf(map.get(modifier)));
						
						item.withDouble(modifier, a);
					}
				}


			table.putItem(item);
			
			//System.out.println("CreateItem");
			return true;
		}catch(Exception e)
		{
			
			System.err.println("Create items failed.");
			System.err.println(e.getMessage());
			return false;
		}		
	}

	public Iterator<Item> retrieveitem(String ScanRequestexpression,int size)
	{
		
		String expression[] = ScanRequestexpression.split("!");

		int resultsize=size;
		ScanRequest scanRequest = new ScanRequest();
		scanRequest.setTableName("D2R_ITEM");

		String firstStr = "#d4";
		String firstvalue= ":d4";
		String filterExpression ="";


		Map<String, String> expressionAttributeNames = new HashMap<String, String>();
		Map<String, Object> expressionAttributeValues = new HashMap<String, Object>();
		
		int scanfilter=0;
		String projectionExpression = "description, itemcount, itemcomment, sellingprice, BattleTag, InputTime, identifier, sellingnum";
		ScanSpec ss = new ScanSpec();
		for(int i =0 ;i<expression.length;i++)
		{
			String keyvalue[] = expression[i].split(" ");
			filterExpression = filterExpression+firstStr+String.valueOf(i)+" ";

			if(keyvalue[0].equals("itemname")&&scanfilter==0)
			{
					scanfilter=1;
			}else if(keyvalue[0].equals("runewordname"))
			{
				scanfilter=2;
			}
			

			expressionAttributeNames.put(firstStr+String.valueOf(i), keyvalue[0]);

			//System.out.println(firstStr+String.valueOf(i)+" "+ keyvalue[0]);
			if(keyvalue.length == 2)
			{
				filterExpression = filterExpression+" = "+firstvalue+String.valueOf(i)+"1";
				expressionAttributeValues.put(firstvalue+String.valueOf(i)+"1", keyvalue[1]);

			}
			else if(keyvalue.length == 3)
			{
				filterExpression = filterExpression+" BETWEEN "+firstvalue+String.valueOf(i)+"1 AND "+firstvalue+String.valueOf(i)+"2";
        		expressionAttributeValues.put(firstvalue+String.valueOf(i)+"1" , Double.parseDouble(keyvalue[1]));
        		expressionAttributeValues.put(firstvalue+String.valueOf(i)+"2" , Double.parseDouble(keyvalue[2]));
			}	     
			
			if(i!=expression.length-1)
			{
				filterExpression +=" AND ";
			}
		}

		try
		{
			ItemCollection<ScanOutcome> items = null;
			Index idx=null;
			ss.withFilterExpression(filterExpression).withProjectionExpression(projectionExpression).withNameMap(expressionAttributeNames).withValueMap(expressionAttributeValues).withMaxResultSize(resultsize);
			Table table = dynamoDB.getTable("D2R_ITEM");
			switch(scanfilter)
			{
			case 0:
				
				items =table.scan(ss);
				//System.out.println("table");
				break;
			case 1:
				idx = table.getIndex("itemname-sellingnum-index");
				items = idx.scan(ss);
				break;
			case 2:
				idx = table.getIndex("runewordname-sellingnum-index");
				//System.out.println(idx.getIndexName());
				items = idx.scan(ss);
				break;
			}
			
			 
			return items.iterator();


		}catch(Exception e)
		{
			System.err.println("scan items failed.");
			System.err.println(e.getMessage());
		}
		return null;

	}
	public boolean updateitem(HashMap<String,String> map)
	{
		
		
		
		Date today = new Date();
		Locale currentLocale = new Locale("KOREAN", "KOREA");
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyyMMddHHmmss",currentLocale);
		String time=formatter.format(today);
		map.put("updateInputTime", time);
		
		
		Table table = dynamoDB.getTable("D2R_ITEM");
		
		Item i =table.getItem("BattleTag", map.get("BattleTag"), "InputTime", map.get("InputTime")); 
		table.deleteItem("BattleTag", map.get("BattleTag"), "InputTime", map.get("InputTime"));
		i.withString("InputTime", map.get("updateInputTime"));
		table.putItem(i);
		
		return true;
	}
	public boolean deleteitem(HashMap<String,String> map)
	{
		Table table = dynamoDB.getTable("D2R_ITEM");
		
		table.deleteItem("BattleTag", map.get("BattleTag"), "InputTime", map.get("InputTime"));
		
		return true;
	}

}
