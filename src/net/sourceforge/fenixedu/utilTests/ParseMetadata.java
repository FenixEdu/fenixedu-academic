/*
 * Created on 24/Jul/2003
 *  
 */

package net.sourceforge.fenixedu.utilTests;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import net.sourceforge.fenixedu.domain.onlineTests.Metadata;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

import com.sun.faces.el.impl.parser.ParseException;

/**
 * @author Susana Fernandes
 */

public class ParseMetadata extends DefaultHandler {
    private String text;

    private Vector<Element> vector = new Vector<Element>();

    private Element current = null;

    private List<String> members = new ArrayList<String>();

    public Metadata parseMetadata(Vector<Element> vector, Metadata metadata) {
        this.vector = vector;
        return vector2Metadata(vector, metadata);
    }

    public Vector<Element> parseMetadata(String metadataFile, String path) throws ParseException {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setValidating(true);
        try {
            SAXParser saxParser = spf.newSAXParser();
            XMLReader reader = saxParser.getXMLReader();
            reader.setContentHandler(this);
            reader.setErrorHandler(this);
            StringReader sr = new StringReader(metadataFile);
            InputSource input = new InputSource(sr);
            MetadataResolver resolver = new MetadataResolver(path);
            reader.setEntityResolver(resolver);
            reader.parse(input);
        } catch (Exception e) {
            throw new ParseException();
        }

        setMembers(vector);
        return vector;
    }

    public void error(SAXParseException e) throws SAXParseException {
        throw e;
    }

    public void fatalError(SAXParseException e) throws SAXParseException {
        throw e;
    }

    public void warning(SAXParseException e) throws SAXParseException {
        throw e;
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        current = new Element(uri, localName, qName, new AttributesImpl(attributes));
        vector.addElement(current);
        text = new String();
    }

    public void endElement(String uri, String localName, String qName) {
        if (current != null && text != null) {
            current.setValue(text.trim());
        } else if (qName.equals("members")) {
            current = new Element(uri, localName, "not" + qName, null);
            vector.addElement(current);
        }
        current = null;
    }

    public void characters(char[] ch, int start, int length) {
        if (current != null && text != null) {
            String value = new String(ch, start, length);
            text += value;
        }
    }

    private Metadata vector2Metadata(Vector<Element> vector, Metadata metadata) {
        boolean difficulty = false, description = false, mainsubject = false, secondarysubject = false, level = false, author = false, value = false, typicallearningtime = false;
        String secondarySubjectString = new String(), authorString = new String();
        for (Element element : vector) {
            String tag = element.getQName();
            if ((tag.equals("difficulty"))) {
                difficulty = true;
                mainsubject = false;
                author = false;
                typicallearningtime = false;
            }
            if ((tag.equals("description"))) {
                difficulty = false;
                description = true;
                mainsubject = false;
                author = false;
                typicallearningtime = false;
            } else if ((tag.equals("mainsubject"))) {
                mainsubject = true;
                secondarysubject = false;
                author = false;
                typicallearningtime = false;
            } else if ((tag.equals("secondarysubject"))) {
                secondarysubject = true;
                author = false;
                typicallearningtime = false;
            } else if ((tag.equals("author"))) {
                author = true;
                secondarysubject = false;
                typicallearningtime = false;
            } else if ((tag.equals("level"))) {
                level = true;
                secondarysubject = false;
                author = false;
                typicallearningtime = false;
            } else if ((tag.equals("typicallearningtime"))) {
                author = false;
                secondarysubject = false;
                typicallearningtime = true;
            } else if ((tag.equals("value"))
                    && (difficulty == true || mainsubject == true || secondarysubject == true)) {
                value = true;
            } else if ((tag.equals("langstring")) && difficulty == true && value == true) {
                metadata.setDifficulty(element.getValue());
                difficulty = false;
                value = false;
            } else if ((tag.equals("langstring")) && mainsubject == true && value == true) {
                metadata.setMainSubject(element.getValue());
                mainsubject = false;
                value = false;
            } else if ((tag.equals("langstring")) && secondarysubject == true && value == true) {
                if (!secondarySubjectString.equals(""))
                    secondarySubjectString = secondarySubjectString.concat(" / ");
                secondarySubjectString = secondarySubjectString.concat(element.getValue());
                value = false;
            } else if ((tag.equals("langstring")) && description == true) {
                metadata.setDescription(element.getValue());
                description = false;
            } else if ((tag.equals("langstring")) && level == true) {
                metadata.setLevel(element.getValue());
                level = false;
            } else if ((tag.equals("langstring")) && author == true) {
                if (!authorString.equals(""))
                    authorString = authorString.concat(" / ");
                authorString = authorString.concat(element.getValue());
            } else if ((tag.equals("datetime")) && typicallearningtime == true) {
                if (!org.apache.commons.lang.StringUtils.isEmpty(element.getValue())) {
                    String[] hourTokens = element.getValue().split(":");
                    Calendar result = Calendar.getInstance();
                    result.set(Calendar.HOUR_OF_DAY, (new Integer(hourTokens[0])).intValue());
                    result.set(Calendar.MINUTE, (new Integer(hourTokens[1])).intValue());
                    result.set(Calendar.SECOND, new Integer(0).intValue());
                    metadata.setLearningTime(result);
                    typicallearningtime = false;
                }
            }
        }
        metadata.setSecondarySubject(secondarySubjectString);
        metadata.setAuthor(authorString);
        return metadata;
    }

    private void setMembers(Vector<Element> vector2) {
        boolean members = false;
        for (Element element : vector) {
            String tag = element.getQName();
            if ((tag.equals("members"))) {
                members = true;
            } else if ((tag.equals("notmembers"))) {
                members = false;
            } else if ((tag.equals("location")) && members == true) {
                try {
                    addMembers(new String(element.getValue().getBytes(), "utf8"));
                } catch (UnsupportedEncodingException e) {
                    addMembers(element.getValue());
                }

            }
        }
    }

    public List<String> getMembers() {
        return members;
    }

    public void addMembers(String member) {
        this.members.add(member);
    }
}