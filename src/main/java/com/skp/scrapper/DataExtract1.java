package com.skp.scrapper;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlUnorderedList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
public class DataExtract1 extends Thread {

	private static final String folder_name = "/home/senthilkumar/xml/";

	private static final String WIKI_BASE_URL = "http://en.wikipedia.org";

	public static void main(String[] args) throws IOException {
		Thread t1 = new DataExtract1();
		Thread t2 = new DataExtract1();
		Thread t3 = new DataExtract1();
		Thread t4=  new DataExtract1();
		Thread t5 =	new DataExtract1();
		t1.setName("/wiki/List_of_Tamil_film_actors");
		t2.setName("/wiki/List_of_India_ODI_cricketers");
		t3.setName("/wiki/List_of_political_parties_in_India");
		t4.setName("/wiki/Category:Tamil_film_directors");
		t5.setName("/wiki/Category:Tamil_film_producers");
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		t5.start();
	}

	public void run() {
		try {
			//processThread();
			processWiki();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*public static void processThread() throws IOException {
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
	}*/

	public static void processWiki() throws IOException {

		String url = Thread.currentThread().getName();
		WebClient webClient = new WebClient();
		url = WIKI_BASE_URL + url;
		HtmlPage page = webClient.getPage(url);
		final List<?> ulList =  page.getByXPath("//div[@id='mw-content-text']/ul");
		String key = "";
		String value = "";
		System.out.println(ulList.get(0));
		Map<String, Object> valuesMap = new HashMap<String, Object>();

		for(Object ul:ulList) {
			HtmlUnorderedList list = (HtmlUnorderedList)ul;
			System.out.println(list.getChildElementCount());
			Iterable<DomElement> domElement = (Iterable<DomElement>) list.getChildElements();
			Iterator itr = (Iterator) domElement.iterator();
			while(itr.hasNext()) {
				DomElement element = (DomElement) itr.next();
				System.out.println("$$$$$$ "+element.getChildElementCount());
				Iterable<DomElement> domElement1 = (Iterable<DomElement>) element.getChildElements();
				Iterator itr1 = (Iterator) domElement1.iterator();
				while(itr1.hasNext()) {
					DomElement element1 = (DomElement) itr1.next();
					key = element1.asText();
					if(null != key && key.contains("."))
						key = key.replaceAll("\\.", "");
					valuesMap.put(key,element1.getAttribute("href"));
				}
			}
		}
		
		valuesMap.put("_id", url.split("/")[1]);
		//System.out.println(domElement.getChildElementCount());
	/*for(Object row: ulList) {
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
	
*/	
		System.out.println(valuesMap);
		saveObject(valuesMap);
	valuesMap.clear();

	webClient.closeAllWindows();
}

public static void saveObject(Map<String, Object> map) throws UnknownHostException {
	//MongoClient  client = new MongoClient();
	Mongo client = new Mongo("localhost",27017);
	DB db = client.getDB("profile");
	DBCollection collection = db.getCollection("profile");
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

