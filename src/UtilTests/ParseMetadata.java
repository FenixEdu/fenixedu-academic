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

import DataBeans.InfoMetadata;

/**
 * @author Susana Fernandes
 */

public class ParseMetadata extends DefaultHandler
{
    private String text;
    private Vector vector = new Vector();
    private Element current = null;

    public void MySAXParserBean()
    {
    }

    public XMLReader getXMLReader(String path) throws Exception
    {
        try
        {

            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser saxParser = spf.newSAXParser();
            XMLReader reader = saxParser.getXMLReader();
            reader.setContentHandler(this);
            reader.setErrorHandler(this);
            Resolver resolver = new Resolver(path);
            reader.setEntityResolver(resolver);
            return reader;
        }
        catch (SAXParseException e)
        {
            throw e;
        }
        catch (SAXException e)
        {
            throw e;
        }
    }

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
            Resolver resolver = new Resolver(path);
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

    public InfoMetadata parseMetadata(String file, InfoMetadata infoMetadata, String path)
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
            Resolver resolver = new Resolver(path);
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
       
        return vector2Metadata(vector, infoMetadata);
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
        throws SAXException
    {
        current = new Element(uri, localName, qName, new AttributesImpl(attributes));
        vector.addElement(current);
        text = new String();
    }

    public void endElement(String uri, String localName, String qName) throws SAXException
    {
        if (current != null && text != null)
        {
            current.setValue(text.trim());
        }
        current = null;
    }

    public void characters(char[] ch, int start, int length) throws SAXException
    {
        if (current != null && text != null)
        {
            String value = new String(ch, start, length);
            text += value;
        }
    }
    private InfoMetadata vector2Metadata(Vector vector, InfoMetadata infoMetadata)
    {

        boolean difficulty = false,
            mainsubject = false,
            secondarysubject = false,
            level = false,
            author = false,
            members = false,
            value = false;
        List secondarySubjectList = new ArrayList(),
            authorList = new ArrayList(),
            membersList = new ArrayList();
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
            }
            else if ((tag.equals("mainsubject")))
            {
                mainsubject = true;
                secondarysubject = false;
                author = false;
                members = false;
            }
            else if ((tag.equals("secondarysubject")))
            {
                secondarysubject = true;
                author = false;
                members = false;
            }
            else if ((tag.equals("author")))
            {
                author = true;
                secondarysubject = false;
                members = false;
            }
            else if ((tag.equals("level")))
            {
                level = true;
                secondarysubject = false;
                author = false;
                members = false;
            }
            else if ((tag.equals("members")))
            {
                members = true;
                author = false;
                secondarysubject = false;
            }
            else if (
                (tag.equals("value"))
                    && (difficulty == true || mainsubject == true || secondarysubject == true))
            {
                value = true;
            }
            else if ((tag.equals("langstring")) && difficulty == true && value == true)
            {
                infoMetadata.setDifficulty(element.getValue());
                difficulty = false;
                value = false;
            }
            else if ((tag.equals("langstring")) && mainsubject == true && value == true)
            {
                infoMetadata.setMainSubject(element.getValue());
                mainsubject = false;
                value = false;
            }
            else if ((tag.equals("langstring")) && secondarysubject == true && value == true)
            {
                secondarySubjectList.add(element.getValue());
                value = false;
            }
            else if ((tag.equals("langstring")) && level == true)
            {
                infoMetadata.setLevel(element.getValue());
                level = false;
            }
            else if ((tag.equals("langstring")) && author == true)
            {
                authorList.add(element.getValue());
            }
            else if ((tag.equals("location")) && members == true)
            {
                membersList.add(element.getValue());
            }
        }
        infoMetadata.setSecondarySubject(secondarySubjectList);
        infoMetadata.setAuthor(authorList);
        infoMetadata.setMembers(membersList);
        return infoMetadata;
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
