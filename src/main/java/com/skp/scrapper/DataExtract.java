package com.thoughtworks.interview;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableHeaderCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

import com.mongodb.*;
public class DataExtract extends Thread {

	private static final String folder_name = "/home/senthilkumar/xml/";

	private static final String WIKI_BASE_URL = "http://en.wikipedia.org";

	public static void main(String[] args) throws IOException {
		Thread t1 = new DataExtract();
		Thread t2 = new DataExtract();
		Thread t3 = new DataExtract();
		Thread t4=  new DataExtract();
		Thread t5 =	new DataExtract();
		t1.setName("1");
		t2.setName("2");
		t3.setName("3");
		t4.setName("4");
		t5.setName("5");
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		t5.start();
	}
	
	public void run() {
		try {
			processThread();
		} catch (IOException e) {
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
				processWiki(data[0], data[1]);
			}
			line = br.readLine();
		}
		br.close();
	}

	public static void processWiki(String fileName, String url) throws IOException {

		WebClient webClient = new WebClient();
		url = WIKI_BASE_URL + url;
		HtmlPage page = webClient.getPage(url);
		final List<?> tr =  page.getByXPath("//div[@id='mw-content-text']/table[@class='infobox biography vcard']/tbody/tr");
		String key = "";
		String value = "";
		Map<String, Object> valuesMap = new HashMap<>();
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
					value = cell.getTextContent();
					valuesMap.put(key, value);
					value = "";
					key = "";
				}
			}
		}
		saveObject(valuesMap);
		valuesMap.clear();

		webClient.closeAllWindows();
	}
	
	public static void saveObject(Map<String, Object> map) throws UnknownHostException {
		MongoClient  client = new MongoClient();
		DB db = client.getDB("profile");
		DBCollection collection = db.getCollection("profiles");
		DBObject obje = new BasicDBObject();
		obje.putAll(map);
		collection.insert(obje);
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

