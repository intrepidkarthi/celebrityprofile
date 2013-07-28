package com.skp.scrapper;


import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableHeaderCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;


public class DataExtract {

	private static final String folder_name= "D://xml//";

	public static void main(String... args) throws FailingHttpStatusCodeException, MalformedURLException, IOException, SAXException, XPathExpressionException {
		WebClient webClient = new WebClient();
		HtmlPage page = webClient.getPage("http://en.wikipedia.org/wiki/Kamal_Haasan");
		String opFileName = folder_name+"kamal.csv";


		FileWriter fw = new FileWriter(opFileName);

		//get div which has a 'name' attribute of 'John'
		final List<?> tr =  page.getByXPath("//div[@id='mw-content-text']/table[@class='infobox biography vcard']/tbody/tr");
		for(Object row: tr) {
			HtmlTableRow trs = (HtmlTableRow)row;
			List<HtmlTableCell> cells = trs.getCells();
			for(HtmlTableCell cell:cells) {
				if(cell instanceof HtmlTableHeaderCell) {
					System.out.println("* "+cell.getNodeName()+"  "+cell.getTextContent());
					fw.append(cell.getTextContent());
					fw.append(",");

				}
				else {
					System.out.println("** "+cell.getNodeValue()+"  "+cell.getTextContent());
					fw.append(cell.getTextContent());
					fw.append("\n");
				}
			}
		}
		fw.close();

		webClient.closeAllWindows();
	}


	public void parseWikipedia() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		WebClient webClient = new WebClient();
		HtmlPage page = webClient.getPage("http://en.wikipedia.org/wiki/Kamal_Haasan");
		String opFileName = folder_name+"kamal.csv";

		/*String pageAsXml = page.asXml();
		pageAsXml = pageAsXml.replaceAll("//]]>", "");
		pageAsXml = pageAsXml.replaceAll("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "");
		pageAsXml.trim();
		File output = new File(folder_name+"kamal.xml");
		FileOutputStream fos = new FileOutputStream(output);
		fos.write(pageAsXml.getBytes());
		fos.flush();
		fos.close();
		System.out.println("Page XML :"+pageAsXml);
		XPath xPath = XPathFactory.newInstance().newXPath();


		String expression = "//div[@id=\"mw-content-text\"]/table[2]/tbody/tr";

		DocumentBuilderFactory builderFactory =
				DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		Document xmlDocument = null;
		FileInputStream fis = new FileInputStream(folder_name+"kamal.xml");
		Reader reader = new InputStreamReader(fis,"UTF-8");

		InputSource is = new InputSource(reader);
		is.setEncoding("UTF-8");
		try {
			builder = builderFactory.newDocumentBuilder();
			xmlDocument = builder.parse(is);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}


		//read a string value
		String email = xPath.compile(expression).evaluate(xmlDocument);

		//read an xml node using xpath
		Node node = (Node) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODE);

		//read a nodelist using xpath
		NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);*/

		//get list of all divs
		final List<?> divs = page.getByXPath("//div");

		FileWriter fw = new FileWriter(opFileName);

		//get div which has a 'name' attribute of 'John'
		final List<?> tr =  page.getByXPath("//div[@id='mw-content-text']/table[@class='infobox biography vcard']/tbody/tr");
		for(Object row: tr) {
			HtmlTableRow trs = (HtmlTableRow)row;
			List<HtmlTableCell> cells = trs.getCells();
			for(HtmlTableCell cell:cells) {
				if(cell instanceof HtmlTableHeaderCell) {
					System.out.println("* "+cell.getNodeName()+"  "+cell.getTextContent());
					fw.append(cell.getTextContent());
					fw.append(",");

				}
				else {
					System.out.println("** "+cell.getNodeValue()+"  "+cell.getTextContent());
					fw.append(cell.getTextContent());
					fw.append("\n");
				}
			}
		}


		webClient.closeAllWindows();

	}

}


