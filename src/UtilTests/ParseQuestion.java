/*
 * Created on 25/Jul/2003
 *
 */
package UtilTests;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

import DataBeans.InfoQuestion;

/**
 * @author Susana Fernandes
 */
public class ParseQuestion extends DefaultHandler {
	private String text;
	private Element current = null;
	private List listQuestion, listOptions, listResponse;
	private Integer responseValue;
	private boolean question = false, option = false, response = false;
	public void MySAXParserBean() {
	}

	public InfoQuestion parseQuestion(String file, InfoQuestion infoQuestion)
		throws Exception {
		listQuestion = new ArrayList();
		listOptions = new ArrayList();
		listResponse = new ArrayList();

		SAXParserFactory spf = SAXParserFactory.newInstance();
		spf.setValidating(false);
		SAXParser saxParser = spf.newSAXParser();
		XMLReader reader = saxParser.getXMLReader();
		reader.setContentHandler(this);
		try {
			StringReader sr = new StringReader(file);
			InputSource input = new InputSource(sr);
			reader.parse(input);
		} catch (MalformedURLException e) {
			throw e;
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		}
		return list2Question(infoQuestion);
	}
	public String parseQuestionImage(String file, int imageId)
		throws Exception {
		listQuestion = new ArrayList();
		listOptions = new ArrayList();
		listResponse = new ArrayList();
		SAXParserFactory spf = SAXParserFactory.newInstance();
		spf.setValidating(false);
		SAXParser saxParser = spf.newSAXParser();
		XMLReader reader = saxParser.getXMLReader();
		reader.setContentHandler(this);
		try {
			StringReader sr = new StringReader(file);
			InputSource input = new InputSource(sr);
			reader.parse(input);
		} catch (MalformedURLException e) {
			throw e;
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		}
		return imageById(imageId);
	}

	public void startElement(
		String uri,
		String localName,
		String qName,
		Attributes attributes)
		throws SAXException {
		current =
			new Element(uri, localName, qName, new AttributesImpl(attributes));

		if (qName.equals("presentation")) {
			question = true;
		} else if (qName.equals("response_lid")) {
			question = false;
			option = true;
		} else if (qName.equals("resprocessing")) {
			response = true;
			question = false;
			option = false;
		}

		if (question)
			listQuestion.add(current);
		else if (option)
			listOptions.add(current);
		else if (response)
			listResponse.add(current);

		text = new String();
	}

	public void endElement(String uri, String localName, String qName)
		throws SAXException {
		if (current != null && text != null) {
			current.setValue(text.trim());
		}
		current = null;
		if (qName.equals("response_lid")) {
			option = false;
			question = true;
		}
	}

	public void characters(char[] ch, int start, int length)
		throws SAXException {
		if (current != null && text != null) {
			String value = new String(ch, start, length);
			text += value;
		}
	}
	private InfoQuestion list2Question(InfoQuestion infoQuestion) {
		Iterator it = listQuestion.iterator();
		List auxList = new ArrayList();
		while (it.hasNext()) {
			Element element = (Element) it.next();
			String tag = element.getQName();
			Attributes atts = element.getAttributes();
			if ((tag.equals("mattext"))) {
				auxList.add(element.getValue());
			} else if ((tag.equals("matimage"))) {
				if (atts.getIndex("label") != -1)
					auxList.add(atts.getValue("label"));
				auxList.add(
					(new String("Content-Type: ")).concat(
						atts.getValue("imagtype")));
			} else if ((tag.equals("flow"))) {
				auxList.add("<flow>");
			}
		}
		infoQuestion.setQuestion(auxList);
		it = listOptions.iterator();
		auxList = new ArrayList();
		while (it.hasNext()) {
			Element element = (Element) it.next();
			String tag = element.getQName();
			Attributes atts = element.getAttributes();
			if ((tag.equals("response_lid"))) {
				if (atts.getIndex("rcardinality") != -1) {
					infoQuestion.setQuestionCardinality(
						atts.getValue("rcardinality"));
				}
			} else if (tag.equals("response_label")) {
				auxList.add("<response_label>");
			} else if ((tag.equals("mattext"))) {
				auxList.add(element.getValue());
			} else if ((tag.equals("matimage"))) {
				if (atts.getIndex("label") != -1)
					auxList.add(atts.getValue("label"));
				auxList.add(
					(new String("Content-Type: ")).concat(
						atts.getValue("imagtype")));
			}
		}
		infoQuestion.setOptions(auxList);

		it = listResponse.iterator();
		auxList = new ArrayList();
		while (it.hasNext()) {
			Element element = (Element) it.next();
			String tag = element.getQName();
			Attributes atts = element.getAttributes();
			if (tag.equals("setvar"))
				infoQuestion.setQuestionValue(new Integer(element.getValue()));
		}
		return infoQuestion;
	}

	private String imageById(int imageId) {
		int imageIdAux = 1;
		Iterator it = listQuestion.iterator();
		while (it.hasNext()) {
			Element element = (Element) it.next();
			String tag = element.getQName();
			Attributes atts = element.getAttributes();
			if ((tag.equals("matimage")))
				if (imageIdAux == imageId)
					return element.getValue();
				else
					imageIdAux++;
		}
		it = listOptions.iterator();
		while (it.hasNext()) {
			Element element = (Element) it.next();
			String tag = element.getQName();
			Attributes atts = element.getAttributes();
			if ((tag.equals("matimage")))
				if (imageIdAux == imageId)
					return element.getValue();
				else
					imageIdAux++;
		}
		return null;
	}
}
