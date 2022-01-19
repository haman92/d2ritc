package com.blizzard.d2ritemauction;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.ScanOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.google.gson.Gson;



@SpringBootTest
public class ApplicationTest {

	@Value("${amazon.dynamodb.endpoint}")
	private String amazonDynamoDBEndpoint;

	@Value("${amazon.aws.accesskey}")
	private String amazonAWSAccessKey;

	@Value("${amazon.aws.secretkey}")
	private String amazonAWSSecretKey;


	
	private AmazonDynamoDB amazonDynamoDB;

	private DynamoDB dynamoDB;

	Table table;

	public AWSCredentials amazonAWSCredentials() {
		return new BasicAWSCredentials(
				amazonAWSAccessKey, amazonAWSSecretKey);
	}

	@BeforeEach
	@Disabled
	void setup() {
		try
		{

			AmazonDynamoDB dynamodb = AmazonDynamoDBClientBuilder.standard()
					.withCredentials(new AWSStaticCredentialsProvider(amazonAWSCredentials()))
					.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(this.amazonDynamoDBEndpoint,"ap-northeast-2"))
					.build();
			dynamoDB = new DynamoDB(dynamodb);
		}catch(Exception e)
		{
			System.err.println("Create items failed.");
			System.err.println(e.getMessage());
		}

	}

	@BeforeEach
	@Disabled
	void setup_addb()
	{
		try
		{
			amazonDynamoDB = AmazonDynamoDBClientBuilder.standard()
					.withCredentials(new AWSStaticCredentialsProvider(amazonAWSCredentials()))
					.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(this.amazonDynamoDBEndpoint,"us-west-2"))
					.build();	
		}catch(Exception e)
		{
			System.err.println("Create items failed.");
			System.err.println(e.getMessage());
		}

	}

	
	
	
	@Test
	@DisplayName("gettable")
	@Disabled
	public void getTableList()
	{
		try
		{
			System.out.println("tablename");
			for(Table temp : dynamoDB.listTables())
			{
				System.out.println(temp.getTableName());
			}
			System.out.println("amazondynamodb");
/*
			for(String temp : this.amazonDynamoDb.listTables().getTableNames())
			{
				System.out.println(temp);
			}
			*/
		}catch(Exception e)
		{
			System.err.println("get items failed.");
			System.err.println(e.getMessage());
		}


	}
	@Test
	@DisplayName("getItem")
	@Disabled
	public void retrieveitem1()
	{
		Table table = dynamoDB.getTable("D2R_ITEM");
		String ScanRequestexpression = "itemcount 1 3";
		//String ScanRequestexpression = "category gloves!hardcore Y!ladder Y!itemname TEST";
		
		String expression[] = ScanRequestexpression.split("!");
		
		
		ScanRequest scanRequest = new ScanRequest();
        scanRequest.setTableName("D2R_ITEM");
        
        String firstStr = "#d4";
        String firstvalue= ":d4";
        String filterExpression ="";
        
        
        Map<String, String> expressionAttributeNames = new HashMap<String, String>();
        Map<String, Object> expressionAttributeValues = new HashMap<String, Object>();
        
        
        for(int i =0 ;i<expression.length;i++)
        {
        	String keyvalue[] = expression[i].split(" ");
        	filterExpression = filterExpression+keyvalue[0]+" ";
        	
        	
        	if(keyvalue.length == 2)
        	{
        		filterExpression = filterExpression+" = "+keyvalue[1];
        		
        		System.out.println(firstvalue+String.valueOf(i)+"1"+" "+ keyvalue[1]);
        	}
        	else if(keyvalue.length == 3)
        	{
        		filterExpression = filterExpression+" BETWEEN "+keyvalue[1]+" AND "+keyvalue[2];
        		
        		System.out.println(firstvalue+String.valueOf(i)+"1"+" "+ keyvalue[1]+" "+firstvalue+String.valueOf(i)+"2 "+keyvalue[2]);
        	}	     
        	if(i!=expression.length-1)
        	{
        		filterExpression +=" AND ";
        	}
        }
        
        System.out.println(filterExpression);
        
        try
        {
        	// filterExpression = "#d40 = gloves AND #d41 = Y AND #d42 = Y and #d43 = TEST";
        	// filterExpression = "category = :d401 AND hardcore = :d411 AND ladder = :d421 and itemname = :d431";
        	//expression. = "category = gloves and hardcore = Y and ladder = Y and itemname = TEST";
        	
        	ItemCollection<ScanOutcome> items = table.scan(filterExpression, // FilterExpression
                    
            		null,null
                    );

        	 Iterator<Item> iterator = items.iterator();
             while (iterator.hasNext()) {
                 System.out.println(iterator.next().toJSONPretty());
             }
        	
        	System.out.println("scan success");
        }catch(Exception e)
        {
			System.err.println("scan items failed.");
			System.err.println(e.getMessage());
        }

	}
	@Disabled
	public void retrieveitem( )
	{
		Table table = dynamoDB.getTable("D2R_ITEM");
		String ScanRequestexpression = "itemcount 1 1";
		//String ScanRequestexpression = "category gloves!hardcore Y!ladder Y!itemname TEST";
		
		String expression[] = ScanRequestexpression.split("!");
		
		
		ScanRequest scanRequest = new ScanRequest();
        scanRequest.setTableName("D2R_ITEM");
        
        String firstStr = "#d4";
        String firstvalue= ":d4";
        String filterExpression ="";
        
        
        Map<String, String> expressionAttributeNames = new HashMap<String, String>();
        Map<String, Object> expressionAttributeValues = new HashMap<String, Object>();
        
        
        for(int i =0 ;i<expression.length;i++)
        {
        	String keyvalue[] = expression[i].split(" ");
        	filterExpression = filterExpression+firstStr+String.valueOf(i)+" ";
        	
        	
        	
        	expressionAttributeNames.put(firstStr+String.valueOf(i), keyvalue[0]);
        	
        	System.out.println(firstStr+String.valueOf(i)+" "+ keyvalue[0]);
        	if(keyvalue.length == 2)
        	{
        		filterExpression = filterExpression+" = "+firstvalue+String.valueOf(i)+"1";
        		expressionAttributeValues.put(firstvalue+String.valueOf(i)+"1", keyvalue[1]);
        		System.out.println(firstvalue+String.valueOf(i)+"1"+" "+ keyvalue[1]);
        	}
        	else if(keyvalue.length == 3)
        	{
        		filterExpression = filterExpression+" BETWEEN "+firstvalue+String.valueOf(i)+"1 AND "+firstvalue+String.valueOf(i)+"2";
        		expressionAttributeValues.put(firstvalue+String.valueOf(i)+"1" , Integer.parseInt(keyvalue[1]));
        		expressionAttributeValues.put(firstvalue+String.valueOf(i)+"2" , Integer.parseInt(keyvalue[1]));
        		System.out.println(firstvalue+String.valueOf(i)+"1"+" "+ keyvalue[1]+" "+firstvalue+String.valueOf(i)+"2 "+keyvalue[2]);
        	}	     
        	if(i!=expression.length-1)
        	{
        		filterExpression +=" AND ";
        	}
        }
        
        System.out.println(filterExpression);
        
        try
        {
        	// filterExpression = "#d40 = gloves AND #d41 = Y AND #d42 = Y and #d43 = TEST";
        	// filterExpression = "category = :d401 AND hardcore = :d411 AND ladder = :d421 and itemname = :d431";
        	//expression. = "category = gloves and hardcore = Y and ladder = Y and itemname = TEST";
        	
        	ItemCollection<ScanOutcome> items = table.scan(filterExpression, // FilterExpression
                    
            		expressionAttributeNames,
            		
            		expressionAttributeValues 
                    );
        	/*
        	filterExpression = "TRUE and #d40 = gloves AND #d41 = Y AND #d42 = Y and #d43 = TEST";
            ItemCollection<ScanOutcome> items = table.scan(filterExpression, // FilterExpression
                    //null,
            		expressionAttributeNames,
            		null
            		//expressionAttributeValues 
                    );
                    */
                    
            /*
            filterExpression = "category = :d401 AND hardcore = :d411 AND ladder = :d421 and itemname = :d431";
            
            ItemCollection<ScanOutcome> items = table.scan(filterExpression, // FilterExpression
                    //null,
            		//expressionAttributeNames,
            		null,
            		expressionAttributeValues 
                    );
              */      
        	/*
        	filterExpression= null;
        	ItemCollection<ScanOutcome> items = table.scan(filterExpression,null,null);
        	*/
        	 Iterator<Item> iterator = items.iterator();
             while (iterator.hasNext()) {
                 System.out.println(iterator.next().toJSONPretty());
             }
        	
        	System.out.println("scan success");
        }catch(Exception e)
        {
			System.err.println("scan items failed.");
			System.err.println(e.getMessage());
        }

        //String filterExpression = "#d42d0 BETWEEN :d42d0 AND :d42d1 And #d42d2 = :d42d2 And #d42d3 = :d42d3";
        /*scanRequest.setFilterExpression(filterExpression);
        scanRequest.setConsistentRead(false);
        scanRequest.setExpressionAttributeNames(expressionAttributeNames);
        scanRequest.setExpressionAttributeValues(expressionAttributeValues);
*/
	}

	
	@Test
	@DisplayName("createItem")
	@Disabled
	public void createitem( )
	{
		//Table table = dynamoDB.getTable("D2R_ITEM");
		String BattleTag  = "trapcard#3319";
		String time = "20211115032702";
		String rarity = "노말";
		String category = "장갑";
		String itemname = "1958";
		int count =1;
		String[] sellingprice = new String[1];
		sellingprice[0]="PULL 1";
		String ladder ="Y";
		String hardcore="Y";
		HashMap<String, String> modifiers=null;
		try
		{
			table  = dynamoDB.getTable("D2R_ITEM");
			Item item = new Item();

			item.withPrimaryKey("BattleTag", BattleTag).withString("InputTime", time);
			item.withString("rarity", rarity).withString("category", category).withString("itemname", itemname).withInt("count",count).withStringSet("sellingprice", sellingprice).withString("ladder",ladder).withString("hardcore",hardcore);

			if(modifiers!=null)
			{
				for(String modifier : modifiers.values())
				{
					String value = modifiers.get(modifier);
					item.withString(modifier, value);
				}
			}
			
			System.out.println(item.toJSONPretty());

			table.putItem(item);
			System.out.println("create items Success");
		}catch(Exception e)
		{
			System.err.println("Create items failed.");
			System.err.println(e.getMessage());
		}



	}
	@Test
	@DisplayName("update")
	@Disabled
	public void update()
	{
		
		HashMap<String,String> map = new HashMap<String,String>();
		
		Date today = new Date();
		Locale currentLocale = new Locale("KOREAN", "KOREA");
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyyMMddHHmmss",currentLocale);
		String time=formatter.format(today);
		map.put("updateInputTime", time);
		map.put("BattleTag", "trapcard#3319");
		map.put("InputTime", "20211123170512");
		
		
		Table table = dynamoDB.getTable("D2R_ITEM");
		
		Item i =table.getItem("BattleTag", map.get("BattleTag"), "InputTime", map.get("InputTime")); 
		table.deleteItem("BattleTag", map.get("BattleTag"), "InputTime", map.get("InputTime"));
		i.withString("InputTime", map.get("updateInputTime"));
		table.putItem(i);
		
	}
	
	/*
	@Test
	@DisplayName("H2save")
	public void h2save()
	{

		@Autowired
		private UserRepository userrepository;
		
		System.out.println("1");
		User user = new User("trapcard#3319",Role.USER);
		this.userrepository.save(user);
		
		System.out.println(this.userrepository.existsById("trapcard#3319"));
		System.out.println("1");
	}
	*/


}
