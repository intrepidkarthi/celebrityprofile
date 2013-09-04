package com.skp.scrapper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableHeaderCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
public class DataExtract extends Thread {

	private static final String folder_name = "/home/senthilkumar/xml/";

	private static final String WIKI_BASE_URL = "http://en.wikipedia.org";

	public static void main(String[] args) throws IOException, InterruptedException {
		
		Mongo client = new Mongo("localhost",27017);
		DB db = client.getDB("profile");
		DBCollection collection = db.getCollection("profile");
		DBCursor cursor = collection.find();
		
		while(cursor.hasNext()) {
			DBObject dbObject = cursor.next();
			Collection<String> keySet = dbObject.keySet();
			ArrayList<String> list = new ArrayList<String>(keySet);
			for(int i=1;i<keySet.size();i=i+5) {
				
				Thread t1 = new DataExtract();
				Thread t2 = new DataExtract();
				Thread t3 = new DataExtract();
				Thread t4=  new DataExtract();
				Thread t5 =	new DataExtract();
				
				t1.setName(list.get(i)+"-"+dbObject.get(list.get(i))+"-"+"tamil cinema");
				t2.setName(list.get(i+1)+"-"+dbObject.get(list.get(i+1))+"-"+"tamil cinema");
				t3.setName(list.get(i+2)+"-"+dbObject.get(list.get(i+2))+"-"+"tamil cinema");
				t4.setName(list.get(i+3)+"-"+dbObject.get(list.get(i+3))+"-"+"tamil cinema");
				t5.setName(list.get(i+4)+"-"+dbObject.get(list.get(i+4))+"-"+"tamil cinema");

				t1.start();
				t2.start();
				t3.start();
				t4.start();
				t5.start();
				
				Thread.currentThread().sleep(15000);
				
			}
		}
		
	}
	
	public void run() {
		try {
			//processThread();
			String tName[] = Thread.currentThread().getName().split("-");
			
			processWiki(tName[0],tName[1],tName[2]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void processThread() throws IOException {
		String threadName = Thread.currentThread().getName();
		String file = folder_name + "actressinput"+threadName+".txt";
		FileReader in = new FileReader(file);
		BufferedReader br = new BufferedReader(in);
		String line = br.readLine();
		while (line != null) {
			String data[] = line.split("	");
			System.out.println(line);
			if(!"Link".equalsIgnoreCase(data[0])) {
				//processWiki(data[0], data[1]);
			}
			line = br.readLine();
		}
		br.close();
	}

	public static void processWiki(String name, String url, String category) throws IOException, InterruptedException {
		WebClient webClient = new WebClient();
		url = WIKI_BASE_URL + url;
		HtmlPage page = webClient.getPage(url);
		final List<?> tr =  page.getByXPath("//div[@id='mw-content-text']/table[@class='infobox biography vcard']/tbody/tr");
		String key = "";
		String value = "";
		Map<String, Object> valuesMap = new HashMap<String, Object>();
		valuesMap.put("name", name);
		for(Object row: tr) {
			HtmlTableRow trs = (HtmlTableRow)row;
			List<HtmlTableCell> cells = trs.getCells();
			for(HtmlTableCell cell:cells) {
				if(cell instanceof HtmlTableHeaderCell) {
					System.out.println("* "+cell.getNodeName()+"  "+cell.getTextContent());
					key = cell.getTextContent();

				}
				else {
					System.out.println("** "+cell.getNodeValue()+"  "+cell.getTextContent());
					Iterator nodeItr = cell.getChildElements().iterator();
					while(nodeItr.hasNext()) {
						DomElement elm = (DomElement) nodeItr.next();
						System.out.println("URL    "+elm.getAttribute("href"));
					}
					
					value = cell.getTextContent();
					valuesMap.put(key, value);
					value = "";
					key = "";
				}
			}
		}
		valuesMap.put("category", category);
		System.out.println("&&&&&&&&&&&&           "+valuesMap);
		saveObject(valuesMap);
		valuesMap.clear();

		webClient.closeAllWindows();
		//Thread.currentThread().notify();
	}
	
	public static void saveObject(Map<String, Object> map) throws UnknownHostException {
		//MongoClient  client = new MongoClient();
		Mongo client = new Mongo("localhost",27017);
		DB db = client.getDB("profile");
		DBCollection collection = db.getCollection("datacollection");
		DBObject obje = new BasicDBObject();
		obje.putAll(map);
		collection.insert(obje);
		client.close();
	}
	
	/*public Profile createProfile(Profile profile, String key, String value) {
		
		if(key !=null && key!="") {
			
			if("Born".equalsIgnoreCase(key)) {
				profile.setBorn(value);
			} else if("Occupation".equalsIgnoreCase(key)) {
				profile.setOccupation(value);
			} else if("Residence".equalsIgnoreCase(key)) {
				profile.setResidence(value);
			}
		}
		
		return profile;
	}*/

}

