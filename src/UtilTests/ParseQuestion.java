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
import java.util.Random;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.struts.util.LabelValueBean;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

import DataBeans.InfoQuestion;
import DataBeans.InfoStudentTestQuestion;

/**
 * @author Susana Fernandes
 */
public class ParseQuestion extends DefaultHandler
{
    private String text;
    private Element current = null;
    private List listQuestion, listOptions, listResponse;
    private Integer responseValue;
    private boolean question = false, option = false, response = false;

    public void MySAXParserBean()
    {
    }

    public InfoQuestion parseQuestion(String file, InfoQuestion infoQuestion, String path)
        throws Exception
    {
        try
        {
            parseFile(file, path);
        }
        catch (Exception e)
        {
            throw e;
        }
        return list2Question(infoQuestion);
    }

    public String parseQuestionImage(String file, int imageId, String path) throws Exception
    {
        try
        {
            parseFile(file, path);
        }
        catch (Exception e)
        {
            throw e;
        }

        return imageById(imageId);

    }

    public String shuffleQuestionOptions(String file, String path) throws Exception
    {
        try
        {
            parseFile(file, path);
        }
        catch (Exception e)
        {
            throw e;
        }
        return shuffleOptions();
    }

    public InfoStudentTestQuestion getOptionsShuffle(
        InfoStudentTestQuestion infoStudentTestQuestion,
        String path)
        throws Exception
    {
        try
        {
            parseFile(infoStudentTestQuestion.getQuestion().getXmlFile(), path);
        }
        catch (Exception e)
        {
            throw e;
        }
        infoStudentTestQuestion.getQuestion().setOptions(
            shuffleStudentTestQuestionOptions(
                infoStudentTestQuestion.getOptionShuffle(),
                infoStudentTestQuestion.getQuestion().getOptions()));
        infoStudentTestQuestion.getQuestion().setCorrectResponse(
            newResponseList(
                infoStudentTestQuestion.getQuestion().getCorrectResponse(),
                infoStudentTestQuestion.getQuestion().getOptions()));
        return infoStudentTestQuestion;
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

    public void parseFile(String file, String path)
        throws ParserConfigurationException, IOException, SAXException
    {
        listQuestion = new ArrayList();
        listOptions = new ArrayList();
        listResponse = new ArrayList();
        try
        {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            spf.setValidating(true);
            SAXParser saxParser = spf.newSAXParser();
            XMLReader reader = saxParser.getXMLReader();
            reader.setContentHandler(this);
            reader.setErrorHandler(this);
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
        catch (SAXParseException e)
        {
            throw e;
        }
        catch (SAXException e)
        {
            throw e;
        }
    }

    public void error(SAXParseException e) throws SAXParseException
    {
        System.out.println("-->ERROR:parseException" + e);
        throw e;
    }

    public void fatalError(SAXParseException e) throws SAXParseException
    {
        System.out.println("-->FATAL_ERROR:parseException" + e);
        throw e;
    }

    public void warning(SAXParseException e) throws SAXParseException
    {
        System.out.println("-->WARNING:parseException" + e);
        throw e;
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes)
        throws SAXException
    {
        current = new Element(uri, localName, qName, new AttributesImpl(attributes));
        if (qName.equals("presentation"))
        {
            question = true;
        }
        else if (qName.equals("response_lid"))
        {
            question = false;
            option = true;
        }
        else if (qName.equals("resprocessing"))
        {
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

    public void endElement(String uri, String localName, String qName) throws SAXException
    {
        if (current != null && text != null)
        {
            current.setValue(text.trim());
        }
        current = null;
        if (qName.equals("response_lid"))
        {
            option = false;
            question = true;
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException
    {
        if (current != null && text != null)
        {
            String value = new String(ch, start, length);
            text += value;
        }
    }
    private InfoQuestion list2Question(InfoQuestion infoQuestion)
    {
        Iterator it = listQuestion.iterator();
        List auxList = new ArrayList();
        while (it.hasNext())
        {
            Element element = (Element) it.next();
            String tag = element.getQName();
            Attributes atts = element.getAttributes();
            if ((tag.equals("mattext")))
            {
                auxList.add(new LabelValueBean("text", element.getValue()));
            }
            else if ((tag.equals("matimage")))
            {
                if (atts.getIndex("label") != -1)
                    auxList.add(new LabelValueBean("image_label", atts.getValue("label")));
                auxList.add(new LabelValueBean(atts.getValue("imagtype"), element.getValue()));
            }
            else if ((tag.equals("flow")))
            {
                auxList.add(new LabelValueBean("flow", ""));
            }
        }
        infoQuestion.setQuestion(auxList);

        it = listOptions.iterator();
        auxList = new ArrayList();
        int optionNumber = 0;
        while (it.hasNext())
        {
            Element element = (Element) it.next();
            String tag = element.getQName();
            Attributes atts = element.getAttributes();
            if ((tag.equals("response_lid")))
            {
                if (atts.getIndex("rcardinality") != -1)
                {
                    infoQuestion.setQuestionCardinality(atts.getValue("rcardinality"));
                }
            }
            else if (tag.equals("response_label"))
            {
                auxList.add(new LabelValueBean("response_label", atts.getValue("ident")));
                optionNumber++;
            }
            else if ((tag.equals("mattext")))
            {
                auxList.add(new LabelValueBean("text", element.getValue()));
            }
            else if ((tag.equals("matimage")))
            {
                if (atts.getIndex("label") != -1)
                    auxList.add(new LabelValueBean("image_label", atts.getValue("label")));
                auxList.add(new LabelValueBean(atts.getValue("imagtype"), element.getValue()));
            }
            else if ((tag.equals("flow")))
            {
                auxList.add(new LabelValueBean("flow", ""));
            }
        }
        infoQuestion.setOptions(auxList);
        infoQuestion.setOptionNumber(new Integer(optionNumber));
        it = listResponse.iterator();
        auxList = new ArrayList();
        while (it.hasNext())
        {
            Element element = (Element) it.next();
            String tag = element.getQName();
            Attributes atts = element.getAttributes();
            if (tag.equals("setvar"))
                infoQuestion.setQuestionValue(new Integer(element.getValue()));
            else if (tag.equals("varequal"))
                auxList.add(element.getValue());
        }
        infoQuestion.setCorrectResponse(auxList);
        return infoQuestion;
    }

    private String imageById(int imageId)
    {
        int imageIdAux = 1;
        Iterator it = listQuestion.iterator();
        while (it.hasNext())
        {
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
        while (it.hasNext())
        {
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

    private String shuffleOptions()
    {
        Iterator it = listOptions.iterator();
        Vector v = new Vector();
        Vector vRandom = new Vector();
        int optionNumber = 0;
        while (it.hasNext())
        {
            Element element = (Element) it.next();
            String tag = element.getQName();
            Attributes atts = element.getAttributes();

            if (tag.equals("response_label"))
            {
                optionNumber++;
                if (atts.getValue(atts.getIndex("rshuffle")).equals("Yes"))
                {
                    v.add("");
                    vRandom.add(new Integer(v.size()).toString());
                    continue;
                }
                else
                    v.add(new Integer(v.size() + 1).toString());
            }
        }

        Random r = new Random();
        boolean ready = false;
        it = vRandom.iterator();
        while (it.hasNext())
        {
            String id = (String) it.next();
            while (!ready)
            {
                int index = (r.nextInt(1000) % optionNumber);
                if (v.elementAt(index).equals(""))
                {
                    v.removeElementAt(index);
                    ready = true;
                    v.insertElementAt(id, index);
                }
                else
                    ready = false;
            }
            ready = false;
        }
        return v.toString();
    }

    private List shuffleStudentTestQuestionOptions(String shuffle, List oldList)
    {
        String[] aux = shuffle.substring(1, shuffle.length() - 1).split(", ");
        List newOptions = new ArrayList();
        for (int i = 0; i < aux.length; i++)
            newOptions = insert2NewList(oldList, newOptions, new Integer(aux[i]).intValue());
        return newOptions;
    }

    private List insert2NewList(List oldList, List newList, int newPosition)
    {
        Iterator it = oldList.iterator();
        int index = 0;
        while (it.hasNext())
        {
            LabelValueBean element = (LabelValueBean) it.next();
            if (element.getLabel().equals("response_label"))
                index++;
            if (index == newPosition)
            {
                newList.add(element);
            }
        }
        return newList;
    }

    private List newResponseList(List responseList, List optionList)
    {
        Iterator itResponse = responseList.iterator();
        List auxList = new ArrayList();
        while (itResponse.hasNext())
        {
            int index = 1;
            String response = (String) itResponse.next();
            Iterator itOption = optionList.iterator();
            while (itOption.hasNext())
            {
                LabelValueBean option = (LabelValueBean) itOption.next();
                if (option.getLabel().equals("response_label"))
                    if (option.getValue().equals(response))
                        auxList.add(new Integer(index));
                    else
                        index++;
            }
        }
        return auxList;
    }
}
