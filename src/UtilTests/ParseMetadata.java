/*
 * Created on 24/Jul/2003
 *  
 */

package UtilTests;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

import Dominio.IMetadata;

/**
 * @author Susana Fernandes
 */

public class ParseMetadata extends DefaultHandler
{
	private String text;
	private Vector vector = new Vector();
	private Element current = null;

	public List parseMetadata(String file, String path) throws Exception
	{
		SAXParserFactory spf = SAXParserFactory.newInstance();
		spf.setValidating(true);
		SAXParser saxParser = spf.newSAXParser();
		XMLReader reader = saxParser.getXMLReader();
		reader.setContentHandler(this);
		reader.setErrorHandler(this);
		try
		{
			StringReader sr = new StringReader(file);
			InputSource input = new InputSource(sr);
			MetadataResolver resolver = new MetadataResolver(path);
			reader.setEntityResolver(resolver);
			reader.parse(input);
		}
		catch (MalformedURLException e)
		{
			throw e;
		}
		catch (FileNotFoundException e)
		{
			throw e;
		}
		catch (IOException e)
		{
			throw e;
		}
		return getMembers();
	}

	public IMetadata parseMetadata(String file, IMetadata metadata, String path)
		throws IOException, ParserConfigurationException, SAXException
	{
		SAXParserFactory spf = SAXParserFactory.newInstance();
		spf.setValidating(true);
		SAXParser saxParser = spf.newSAXParser();
		XMLReader reader = saxParser.getXMLReader();
		reader.setContentHandler(this);
		reader.setErrorHandler(this);
		try
		{
			StringReader sr = new StringReader(file);
			InputSource input = new InputSource(sr);
			MetadataResolver resolver = new MetadataResolver(path);
			reader.setEntityResolver(resolver);
			reader.parse(input);
		}
		catch (MalformedURLException e)
		{
			throw e;
		}
		catch (FileNotFoundException e)
		{
			throw e;
		}
		catch (IOException e)
		{
			throw e;
		}

		return vector2Metadata(vector, metadata);
	}

	public void error(SAXParseException e) throws SAXParseException
	{
		System.out.println("--->ERROR: " + e);
		throw e;
	}

	public void fatalError(SAXParseException e) throws SAXParseException
	{
		System.out.println("--->FATALERROR: " + e);
		throw e;
	}

	public void warning(SAXParseException e) throws SAXParseException
	{
		System.out.println("--->WARNING: " + e);
		throw e;
	}

	public void startElement(String uri, String localName, String qName, Attributes attributes)
	{
		current = new Element(uri, localName, qName, new AttributesImpl(attributes));
		vector.addElement(current);
		text = new String();
	}

	public void endElement(String uri, String localName, String qName)
	{
		if (current != null && text != null)
		{
			current.setValue(text.trim());
		}
		current = null;
	}

	public void characters(char[] ch, int start, int length)
	{
		if (current != null && text != null)
		{
			String value = new String(ch, start, length);
			text += value;
		}
	}

	private IMetadata vector2Metadata(Vector vector, IMetadata metadata)
	{

		boolean difficulty = false,
			description = false,
			mainsubject = false,
			secondarysubject = false,
			level = false,
			author = false,
			members = false,
			value = false,
			typicallearningtime = false;
		String secondarySubjectString = new String(), authorString = new String();
		int numberOfMembers = 0;
		Iterator it = vector.iterator();
		while (it.hasNext())
		{
			Element element = (Element) it.next();
			String tag = element.getQName();
			Attributes atts = element.getAttributes();
			if ((tag.equals("difficulty")))
			{
				difficulty = true;
				mainsubject = false;
				author = false;
				members = false;
				typicallearningtime = false;
			}
			if ((tag.equals("description")))
			{
				difficulty = false;
				description = true;
				mainsubject = false;
				author = false;
				members = false;
				typicallearningtime = false;
			}
			else if ((tag.equals("mainsubject")))
			{
				mainsubject = true;
				secondarysubject = false;
				author = false;
				members = false;
				typicallearningtime = false;
			}
			else if ((tag.equals("secondarysubject")))
			{
				secondarysubject = true;
				author = false;
				members = false;
				typicallearningtime = false;
			}
			else if ((tag.equals("author")))
			{
				author = true;
				secondarysubject = false;
				members = false;
				typicallearningtime = false;
			}
			else if ((tag.equals("level")))
			{
				level = true;
				secondarysubject = false;
				author = false;
				members = false;
				typicallearningtime = false;
			}
			else if ((tag.equals("members")))
			{
				members = true;
				author = false;
				secondarysubject = false;
				typicallearningtime = false;
			}
			else if ((tag.equals("typicallearningtime")))
			{
				members = false;
				author = false;
				secondarysubject = false;
				typicallearningtime = true;
			}
			else if (
				(tag.equals("value"))
					&& (difficulty == true || mainsubject == true || secondarysubject == true))
			{
				value = true;
			}
			else if ((tag.equals("langstring")) && difficulty == true && value == true)
			{
				metadata.setDifficulty(element.getValue());
				difficulty = false;
				value = false;
			}
			else if ((tag.equals("langstring")) && mainsubject == true && value == true)
			{
				metadata.setMainSubject(element.getValue());
				mainsubject = false;
				value = false;
			}
			else if ((tag.equals("langstring")) && secondarysubject == true && value == true)
			{
				if (!secondarySubjectString.equals(""))
					secondarySubjectString = secondarySubjectString.concat(" / ");
				secondarySubjectString = secondarySubjectString.concat(element.getValue());
				value = false;
			}
			else if ((tag.equals("langstring")) && description == true)
			{
				metadata.setDescription(element.getValue());
				description = false;
			}
			else if ((tag.equals("langstring")) && level == true)
			{
				metadata.setLevel(element.getValue());
				level = false;
			}
			else if ((tag.equals("langstring")) && author == true)
			{
				if (!authorString.equals(""))
					authorString = authorString.concat(" / ");
				authorString = authorString.concat(element.getValue());
			}
			else if ((tag.equals("location")) && members == true)
			{
				numberOfMembers++;
			}

			else if ((tag.equals("datetime")) && typicallearningtime == true)
			{
				String[] hourTokens = element.getValue().split(":");
				Calendar result = Calendar.getInstance();
				result.set(Calendar.HOUR_OF_DAY, (new Integer(hourTokens[0])).intValue());
				result.set(Calendar.MINUTE, (new Integer(hourTokens[1])).intValue());
				result.set(Calendar.SECOND, new Integer(0).intValue());
				metadata.setLearningTime(result);
				typicallearningtime = false;
			}
		}
		metadata.setSecondarySubject(secondarySubjectString);
		metadata.setAuthor(authorString);
		metadata.setNumberOfMembers(new Integer(numberOfMembers));
		return metadata;
	}

	private List getMembers()
	{
		List result = new ArrayList();
		Iterator it = vector.iterator();
		while (it.hasNext())
		{
			Element element = (Element) it.next();
			String tag = element.getQName();
			Attributes atts = element.getAttributes();
			if ((tag.equals("location")))
				result.add(element.getValue());
		}
		return result;
	}
}
