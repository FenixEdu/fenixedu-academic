/*
 * Created on 25/Jul/2003
 */
package UtilTests;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
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
import Util.tests.CardinalityType;
import Util.tests.QuestionType;
import Util.tests.RenderChoise;
import Util.tests.RenderFIB;
import Util.tests.Response;
import Util.tests.ResponseCondition;
import Util.tests.ResponseLID;
import Util.tests.ResponseNUM;
import Util.tests.ResponseProcessing;
import Util.tests.ResponseSTR;

/**
 * @author Susana Fernandes
 */
public class ParseQuestion extends DefaultHandler {

    private String text;

    private Element current = null;

    private List listQuestion, listOptions, listResponse, listFeedback;

    private String responseId = new String();

    private boolean question = false, option = false, response = false, feedback = false;

    public void MySAXParserBean() {
    }

    public InfoQuestion parseQuestion(String file, InfoQuestion infoQuestion, String path) throws Exception, ParseQuestionException {
        try {
            parseFile(file, path);
            infoQuestion = list2Question(infoQuestion);
        } catch (Exception e) {
            throw e;
        }
        return infoQuestion;
    }

    public InfoStudentTestQuestion parseStudentTestQuestion(InfoStudentTestQuestion infoStudentTestQuestion, String path) throws Exception,
            ParseQuestionException {
        try {
            parseFile(infoStudentTestQuestion.getQuestion().getXmlFile(), path);
        } catch (Exception e) {
            throw e;
        }
        infoStudentTestQuestion.setQuestion(list2Question(infoStudentTestQuestion.getQuestion()));

        Response response = null;
        if (infoStudentTestQuestion.getQuestion().getQuestionType().getType().intValue() == QuestionType.LID) {
            infoStudentTestQuestion.getQuestion()
                    .setOptions(
                            shuffleStudentTestQuestionOptions(infoStudentTestQuestion.getOptionShuffle(), infoStudentTestQuestion.getQuestion()
                                    .getOptions()));
            infoStudentTestQuestion.getQuestion().setResponseProcessingInstructions(
                    newResponseList(infoStudentTestQuestion.getQuestion().getResponseProcessingInstructions(), infoStudentTestQuestion.getQuestion()
                            .getOptions()));
            response = new ResponseLID();
        } else if (infoStudentTestQuestion.getQuestion().getQuestionType().getType().intValue() == QuestionType.NUM)
            response = new ResponseNUM();
        else
            response = new ResponseSTR();

        if (infoStudentTestQuestion.getResponse() == null) {
            infoStudentTestQuestion.setResponse(response);
        }
        return infoStudentTestQuestion;
    }

    public String parseQuestionImage(String file, int imageId, String path) throws Exception {
        try {
            parseFile(file, path);
        } catch (Exception e) {
            throw e;
        }

        return imageById(imageId);

    }

    public String shuffleQuestionOptions(String file, boolean shuffle, String path) throws Exception {
        try {
            parseFile(file, path);
        } catch (Exception e) {
            throw e;
        }
        return shuffleOptions(shuffle);
    }

    public void parseFile(String file, String path) throws ParserConfigurationException, IOException, SAXException {
        listQuestion = new ArrayList();
        listOptions = new ArrayList();
        listResponse = new ArrayList();
        listFeedback = new ArrayList();
        try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            spf.setValidating(true);
            SAXParser saxParser = spf.newSAXParser();
            XMLReader reader = saxParser.getXMLReader();
            reader.setContentHandler(this);
            reader.setErrorHandler(this);
            StringReader sr = new StringReader(file);
            InputSource input = new InputSource(sr);
            QuestionResolver resolver = new QuestionResolver(path);
            reader.setEntityResolver(resolver);
            reader.parse(input);
        } catch (MalformedURLException e) {
            throw e;
        } catch (FileNotFoundException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        } catch (SAXParseException e) {
            throw e;
        } catch (SAXException e) {
            throw e;
        }
    }

    public void error(SAXParseException e) throws SAXParseException {
        System.out.println("-->ERROR:parseException" + e);
        throw e;
    }

    public void fatalError(SAXParseException e) throws SAXParseException {
        System.out.println("-->FATAL_ERROR:parseException" + e);
        throw e;
    }

    public void warning(SAXParseException e) throws SAXParseException {
        System.out.println("-->WARNING:parseException" + e);
        throw e;
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        current = new Element(uri, localName, qName, new AttributesImpl(attributes));
        if (qName.equals("presentation")) {
            question = true;
        } else if (qName.equals("response_lid") || qName.equals("response_str") || qName.equals("response_num")) {
            question = false;
            option = true;
        } else if (qName.equals("resprocessing")) {
            response = true;
            question = false;
            option = false;
        } else if (qName.equals("itemfeedback")) {
            feedback = true;
            response = false;
            question = false;
            option = false;
        }

        if (question)
            listQuestion.add(current);
        else if (option)
            listOptions.add(current);
        else if (response)
            listResponse.add(current);
        else if (feedback)
            listFeedback.add(current);

        text = new String();
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (current != null && text != null) {
            current.setValue(text.trim());
        }
        current = null;

        if (qName.equals("response_lid")) {
            //  || qName.equals("response_str") || qName.equals("response_num"))
            option = false;
            question = true;
        } else if (qName.equals("not") || qName.equals("and") || qName.equals("or"))
            listResponse.add(new Element(uri, localName, "/" + qName, null));
        else if (qName.equals("itemfeedback"))
            listFeedback.add(new Element(uri, localName, "/" + qName, null));
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        if (current != null && text != null) {
            String value = new String(ch, start, length);
            text += value;
        }
    }

    private InfoQuestion list2Question(InfoQuestion infoQuestion) throws ParseQuestionException {
        infoQuestion = getQuestion(infoQuestion);
        infoQuestion = getOptions(infoQuestion);
        infoQuestion = getResponses(infoQuestion);
        infoQuestion = getFeedback(infoQuestion);

        if (infoQuestion.getQuestionType().getType().intValue() == QuestionType.LID)
            infoQuestion = getRidOfEmptyResponseConditions(infoQuestion);
        infoQuestion = setFenixCorrectResponse(infoQuestion);
        infoQuestion = removeRepeatedConditions(infoQuestion);
        return infoQuestion;
    }

    private InfoQuestion getQuestion(InfoQuestion infoQuestion) throws ParseQuestionException {
        Iterator it = listQuestion.iterator();
        List auxList = new ArrayList();
        while (it.hasNext()) {
            Element element = (Element) it.next();
            String tag = element.getQName();
            Attributes atts = element.getAttributes();
            if (tag.startsWith("render_")) {
                if (!(tag.equals("render_choice") || tag.equals("render_fib")))
                    throw new ParseQuestionException(tag, true);
            } else if (tag.startsWith("mat") && !tag.equals("material")) {

                if ((tag.equals("mattext"))) {
                    auxList.add(new LabelValueBean("text", element.getValue()));
                } else if ((tag.equals("matimage"))) {
                    if (atts.getIndex("label") != -1)
                        auxList.add(new LabelValueBean("image_label", atts.getValue("label")));
                    if (atts.getIndex("uri") != -1)
                        throw new ParseQuestionException(tag, "uri");
                    auxList.add(new LabelValueBean(atts.getValue("imagtype"), element.getValue()));
                } else
                    throw new ParseQuestionException(tag, true);
            } else if ((tag.equals("flow"))) {
                auxList.add(new LabelValueBean("flow", ""));
            }
        }
        infoQuestion.setQuestion(auxList);
        return infoQuestion;
    }

    private InfoQuestion getOptions(InfoQuestion infoQuestion) throws ParseQuestionException {
        Iterator it = listOptions.iterator();
        List auxList = new ArrayList();
        int optionNumber = 0;
        int questions = 0;
        while (it.hasNext()) {
            Element element = (Element) it.next();
            String tag = element.getQName();
            Attributes atts = element.getAttributes();
            if (tag.startsWith("render_")) {
                if (tag.equals("render_fib")) {
                    RenderFIB renderFIB = new RenderFIB();
                    renderFIB.setFibtype(atts.getValue("fibtype"));
                    if (atts.getIndex("rows") != -1)
                        renderFIB.setRows(new Integer(atts.getValue("rows")));
                    if (atts.getIndex("columns") != -1)
                        renderFIB.setColumns(new Integer(atts.getValue("columns")));
                    if (atts.getIndex("maxchars") != -1)
                        renderFIB.setMaxchars(new Integer(atts.getValue("maxchars")));
                    infoQuestion.getQuestionType().setRender(renderFIB);
                } else if (tag.equals("render_choice")) {
                    RenderChoise renderChoise = new RenderChoise();
                    renderChoise.setShuffle(atts.getValue("shuffle"));
                    infoQuestion.getQuestionType().setRender(renderChoise);
                } else
                    throw new ParseQuestionException(tag, true);
            } else if ((tag.startsWith("mat") && !tag.equals("material")) || tag.startsWith("response_")) {
                if ((tag.equals("response_lid")) || (tag.equals("response_str")) || (tag.equals("response_num"))) {
                    responseId = atts.getValue("ident");
                    questions++;
                    if (questions > 1)
                        throw new ParseQuestionException("O sistema ainda não suporta perguntas com alíneas.");

                    infoQuestion.setQuestionType(new QuestionType(tag));

                    if (atts.getIndex("rcardinality") != -1) {
                        if (atts.getValue("rcardinality").equals("Ordered"))
                            throw new ParseQuestionException(tag, "rcardinality=Ordered");
                        infoQuestion.getQuestionType().setCardinalityType(new CardinalityType(atts.getValue("rcardinality")));
                    }
                } else if (tag.equals("response_label")) {
                    auxList.add(new LabelValueBean("response_label", atts.getValue("ident")));
                    optionNumber++;
                } else if ((tag.equals("mattext"))) {
                    auxList.add(new LabelValueBean("text", element.getValue()));
                } else if ((tag.equals("matimage"))) {
                    if (atts.getIndex("label") != -1)
                        auxList.add(new LabelValueBean("image_label", atts.getValue("label")));
                    if (atts.getIndex("uri") != -1)
                        throw new ParseQuestionException(tag, "uri");
                    auxList.add(new LabelValueBean(atts.getValue("imagtype"), element.getValue()));
                } else
                    throw new ParseQuestionException(tag, true);
            } else if ((tag.equals("flow"))) {
                auxList.add(new LabelValueBean("flow", ""));
            }
        }
        infoQuestion.setOptionNumber(new Integer(optionNumber));
        infoQuestion.setOptions(auxList);
        return infoQuestion;
    }

    private InfoQuestion getResponses(InfoQuestion infoQuestion) throws ParseQuestionException {
        List newResponseList = getRidOfNot(listResponse);
        ListIterator it = newResponseList.listIterator();
        List auxList = new ArrayList();
        ResponseProcessing responseProcessing = null;
        int responseProcessingId = 0, and = 0, or = 0;
        boolean not = false;

        for (int i = 0; it.hasNext(); i++) {
            Element element = (Element) it.next();
            String tag = element.getQName();
            Attributes atts = element.getAttributes();

            if (tag.equals("setvar")) {
                if (atts.getIndex("action") != -1) {
                    responseProcessing.setAction(atts.getValue("action"));
                }
                Double value = new Double(element.getValue().replace(',', '.'));
                responseProcessing.setResponseValue(value);

                if (responseProcessing.getAction().intValue() == ResponseProcessing.SET
                        || responseProcessing.getAction().intValue() == ResponseProcessing.ADD) {
                    if (infoQuestion.getQuestionValue() == null || (infoQuestion.getQuestionValue().compareTo(value) < 0))
                        infoQuestion.setQuestionValue(value);
                } else if (responseProcessing.getAction().intValue() == ResponseProcessing.SUBTRACT) {
                    if (infoQuestion.getQuestionValue() == null)
                        infoQuestion.setQuestionValue(new Double("-" + value));
                    responseProcessing.setResponseValue(new Double("-" + value));
                }
                Iterator itAuxList = auxList.iterator();
                while (itAuxList.hasNext()) {
                    ResponseProcessing rp = (ResponseProcessing) itAuxList.next();
                    if (rp.getResponseProcessingId() == responseProcessing.getResponseProcessingId()) {
                        rp.setAction(atts.getValue("action"));
                        rp.setResponseValue(responseProcessing.getResponseValue());
                    } else if (rp.getAction() == null) {
                        rp.setAction(ResponseProcessing.SET_STRING);
                    } else if (rp.getResponseValue() == null) {
                        rp.setResponseValue(new Double(0));
                    }
                }
            } else if (tag.equals("respcondition")) {
                if (responseProcessing != null)
                    auxList.add(responseProcessing);
                responseProcessingId++;
                responseProcessing = new ResponseProcessing(responseProcessingId);
                responseProcessing.setResponseConditions(new ArrayList());
            } else if (tag.startsWith("var")) {
                if (tag.equals("varequal") || tag.equals("varlt") || tag.equals("varlte") || tag.equals("vargt") || tag.equals("vargte")
                        || tag.equals("varsubstring")) {
                    if (!atts.getValue("respident").equals(responseId))
                        throw new ParseQuestionException("Exercício Inválido (identificadores inválidos)");
                    if (or == 0 && and == 0) {
                        String tagName = tag;
                        if (not)
                            tagName = new String("not").concat(tagName);
                        if (atts.getIndex("case") != -1) {
                            if (atts.getValue("case").equals("Nocase"))
                                tagName = tagName.concat("ignorecase");
                        }
                        if (infoQuestion.getQuestionType().getType().intValue() == QuestionType.LID
                                && infoQuestion.getQuestionType().getCardinalityType().getType().intValue() == CardinalityType.SINGLE)
                            if (getNumberOfVarEquals(responseProcessing.getResponseConditions()) > 0)
                                throw new ParseQuestionException(
                                        "Uma das soluções indicadas no ficheiro tem mais do que uma resposta, e uma pergunta de escolha simples apenas admite uma resposta.");

                        responseProcessing.getResponseConditions()
                                .add(new ResponseCondition(tagName, element.getValue(), atts.getValue("respident")));
                    }
                } else
                    throw new ParseQuestionException(tag, true);
            } else if (tag.equals("not") || tag.equals("/not")) {
                if (not)
                    not = false;
                else
                    not = true;
            } else if (tag.equals("and")) {
                if (and == 0 && or == 0)
                    auxList.addAll(resolveAndCondition(newResponseList, i, new ArrayList(), responseProcessingId));
                and++;
            } else if (tag.equals("/and")) {
                and--;
            } else if (tag.equals("or")) {
                if (or == 0 && and == 0)
                    auxList.addAll(resolveOrCondition(newResponseList, i, new ArrayList(), responseProcessingId));
                or++;
            } else if (tag.equals("/or")) {
                or--;
            } else if (tag.equals("displayfeedback")) {
                List f = new ArrayList();
                f.add(new LabelValueBean("linkrefid", atts.getValue("linkrefid")));
                responseProcessing.setFeedback(f);
                Iterator itAuxList = auxList.iterator();
                while (itAuxList.hasNext()) {
                    ResponseProcessing rp = (ResponseProcessing) itAuxList.next();
                    if (rp.getResponseProcessingId() == responseProcessing.getResponseProcessingId()) {
                        rp.setFeedback(f);
                    }
                }

            }
        }
        if (responseProcessing != null && responseProcessing.getResponseConditions().size() != 0)
            auxList.add(responseProcessing);
        infoQuestion.setResponseProcessingInstructions(auxList);
        return infoQuestion;
    }

    private InfoQuestion getFeedback(InfoQuestion infoQuestion) throws ParseQuestionException {

        ListIterator it = listFeedback.listIterator();
        List auxList = new ArrayList();
        List responses = infoQuestion.getResponseProcessingInstructions();
        String ident = new String();
        while (it.hasNext()) {
            Element element = (Element) it.next();
            String tag = element.getQName();
            Attributes atts = element.getAttributes();

            if (tag.equals("itemfeedback")) {
                ident = atts.getValue("ident");
            } else if (tag.startsWith("mat") && !tag.equals("material")) {
                if ((tag.equals("mattext"))) {
                    auxList.add(new LabelValueBean("text", element.getValue()));
                } else if ((tag.equals("matimage"))) {
                    if (atts.getIndex("label") != -1)
                        auxList.add(new LabelValueBean("image_label", atts.getValue("label")));
                    if (atts.getIndex("uri") != -1)
                        throw new ParseQuestionException(tag, "uri");
                    auxList.add(new LabelValueBean(atts.getValue("imagtype"), element.getValue()));
                } else
                    throw new ParseQuestionException(tag, true);
            } else if ((tag.equals("flow"))) {
                auxList.add(new LabelValueBean("flow", ""));
            } else if (tag.equals("/itemfeedback")) {

                Iterator responsesIt = infoQuestion.getResponseProcessingInstructions().iterator();
                for (int i = 0; responsesIt.hasNext(); i++) {
                    ResponseProcessing rp = (ResponseProcessing) responsesIt.next();
                    if (rp.getFeedback() != null && rp.getFeedback().size() > 0
                            && ((LabelValueBean) rp.getFeedback().get(0)).getValue().equals(ident))
                        rp.setFeedback(auxList);

                    responses.set(i, rp);

                }
                auxList = new ArrayList();
            }
        }
        infoQuestion.setResponseProcessingInstructions(responses);
        return infoQuestion;
    }

    private String imageById(int imageId) {
        int imageIdAux = 1;
        Iterator it = listQuestion.iterator();
        while (it.hasNext()) {
            Element element = (Element) it.next();
            String tag = element.getQName();
            if ((tag.equals("matimage"))) {
                if (imageIdAux == imageId)
                    return element.getValue();
                imageIdAux++;
            }
        }
        it = listOptions.iterator();
        while (it.hasNext()) {
            Element element = (Element) it.next();
            String tag = element.getQName();
            if ((tag.equals("matimage"))) {
                if (imageIdAux == imageId)
                    return element.getValue();

                imageIdAux++;
            }
        }
        it = listFeedback.iterator();
        while (it.hasNext()) {
            Element element = (Element) it.next();
            String tag = element.getQName();
            if ((tag.equals("matimage"))) {
                if (imageIdAux == imageId)
                    return element.getValue();

                imageIdAux++;
            }
        }
        return null;
    }

    private String shuffleOptions(boolean shuffle) {
        Iterator it = listOptions.iterator();
        Vector v = new Vector();
        Vector vRandom = new Vector();
        int optionNumber = 0;
        while (it.hasNext()) {
            Element element = (Element) it.next();
            String tag = element.getQName();
            Attributes atts = element.getAttributes();

            if (tag.equals("response_label")) {
                optionNumber++;
                if (atts.getValue(atts.getIndex("rshuffle")).equals("Yes") && shuffle == true) {
                    v.add("");
                    vRandom.add(new Integer(v.size()).toString());
                    continue;
                }
                v.add(new Integer(v.size() + 1).toString());
            }
        }

        Random r = new Random();
        boolean ready = false;
        it = vRandom.iterator();
        while (it.hasNext()) {
            String id = (String) it.next();
            while (!ready) {
                int index = (r.nextInt(1000) % optionNumber);
                if (v.elementAt(index).equals("")) {
                    v.removeElementAt(index);
                    ready = true;
                    v.insertElementAt(id, index);
                } else
                    ready = false;
            }
            ready = false;
        }
        return v.toString();
    }

    private List shuffleStudentTestQuestionOptions(String shuffle, List oldList) {
        String[] aux = shuffle.substring(1, shuffle.length() - 1).split(", ");
        List newOptions = new ArrayList();
        for (int i = 0; i < aux.length; i++)
            newOptions = insert2NewList(oldList, newOptions, new Integer(aux[i]).intValue());
        return newOptions;
    }

    private List insert2NewList(List oldList, List newList, int newPosition) {
        Iterator it = oldList.iterator();
        int index = 0;
        while (it.hasNext()) {
            LabelValueBean element = (LabelValueBean) it.next();
            if (element.getLabel().equals("response_label")) {
                index++;
            }
            if (index == newPosition || index == 0) {
                newList.add(element);
            }
        }
        return newList;
    }

    public List newResponseList(List responseList, List optionList) {
        List newResponseProcessingList = new ArrayList();

        Iterator itResponseProcessing = responseList.iterator();
        while (itResponseProcessing.hasNext()) {
            List newResponseConditionList = new ArrayList();

            ResponseProcessing responseProcessing = (ResponseProcessing) itResponseProcessing.next();
            Iterator itResponseCondition = responseProcessing.getResponseConditions().iterator();
            while (itResponseCondition.hasNext()) {
                ResponseCondition responseCondition = (ResponseCondition) itResponseCondition.next();
                String response = responseCondition.getResponse();
                Iterator itOption = optionList.iterator();
                ResponseCondition newResponseCondition = null;
                int index = 1;
                while (itOption.hasNext()) {
                    LabelValueBean option = (LabelValueBean) itOption.next();
                    if (option.getLabel().equals("response_label"))
                        if (option.getValue().equals(response))
                            newResponseCondition = new ResponseCondition(ResponseCondition.getConditionString(responseCondition.getCondition()),
                                    new Integer(index).toString(), responseCondition.getResponseLabelId());
                        else
                            index++;
                }
                newResponseConditionList.add(newResponseCondition);
            }
            ResponseProcessing newResponseProcessing = new ResponseProcessing(newResponseConditionList, responseProcessing.getResponseValue(),
                    responseProcessing.getAction(), responseProcessing.getFeedback(), responseProcessing.isFenixCorrectResponse());
            newResponseProcessingList.add(newResponseProcessing);
        }
        return newResponseProcessingList;
    }

    public InfoQuestion setFenixCorrectResponse(InfoQuestion infoQuestion) {
        if (infoQuestion.getResponseProcessingInstructions().size() != 0) {
            Iterator itResponseProcessing = infoQuestion.getResponseProcessingInstructions().iterator();
            int fenixCorrectResponseIndex = -1;
            double maxValue = 0;
            int previewsAction = 0;
            for (int i = 0; itResponseProcessing.hasNext(); i++) {
                ResponseProcessing responseProcessing = (ResponseProcessing) itResponseProcessing.next();
                if (responseProcessing.getResponseValue() != null && responseProcessing.getAction() != null) {

                    if ((responseProcessing.getResponseValue().doubleValue() > maxValue)
                            || (responseProcessing.getResponseValue().doubleValue() == maxValue && previewsAction == 0)
                            || (responseProcessing.getResponseValue().doubleValue() == maxValue && previewsAction != ResponseProcessing.SET && responseProcessing
                                    .getAction().intValue() == ResponseProcessing.SET)) {
                        maxValue = responseProcessing.getResponseValue().doubleValue();
                        fenixCorrectResponseIndex = i;
                        previewsAction = responseProcessing.getAction().intValue();
                    }
                }
            }
            if (fenixCorrectResponseIndex != -1)
                ((ResponseProcessing) infoQuestion.getResponseProcessingInstructions().get(fenixCorrectResponseIndex)).setFenixCorrectResponse(true);
        }
        return infoQuestion;
    }

    public InfoQuestion removeRepeatedConditions(InfoQuestion infoQuestion) {
        List newRpList = new ArrayList();
        boolean isLID = false;
        if (infoQuestion.getQuestionType().getType().intValue() == QuestionType.LID)
            isLID = true;
        if (infoQuestion.getResponseProcessingInstructions().size() > 1) {
            newRpList.add(infoQuestion.getResponseProcessingInstructions().get(0));
            for (int i = 1; i < infoQuestion.getResponseProcessingInstructions().size(); i++) {
                ResponseProcessing responseProcessing = (ResponseProcessing) infoQuestion.getResponseProcessingInstructions().get(i);
                if (!responseProcessing.isThisConditionListInResponseProcessingList(newRpList, isLID))
                    newRpList.add(responseProcessing);
            }
            infoQuestion.setResponseProcessingInstructions(newRpList);
        }
        return infoQuestion;
    }

    private int getNumberOfVarEquals(List rcList) {
        Iterator rcIt = rcList.iterator();
        int result = 0;
        for (int i = 0; rcIt.hasNext(); i++)
            if (((ResponseCondition) rcIt.next()).getCondition().intValue() == ResponseCondition.VAREQUAL)
                result++;
        return result;
    }

    public InfoQuestion getRidOfEmptyResponseConditions(InfoQuestion infoQuestion) {
        Iterator rpIt = infoQuestion.getResponseProcessingInstructions().iterator();
        List newResponseProcessingInstructions = new ArrayList();

        for (int i = 0; rpIt.hasNext(); i++) {
            ResponseProcessing rp = (ResponseProcessing) rpIt.next();
            Iterator rcIt = rp.getResponseConditions().iterator();
            boolean empty = true;
            while (rcIt.hasNext()) {
                ResponseCondition rc = (ResponseCondition) rcIt.next();
                if (rc.getCondition().intValue() != ResponseCondition.NOTVAREQUAL)
                    empty = false;
            }
            if (!empty)
                newResponseProcessingInstructions.add(rp);
        }
        infoQuestion.setResponseProcessingInstructions(newResponseProcessingInstructions);
        return infoQuestion;
    }

    private List getRidOfNot(List responseList) {
        Iterator it = listResponse.iterator();
        List resultList = new ArrayList();
        //        ResponseProcessing responseProcessing = null;
        int not = 0;
        while (it.hasNext()) {
            Element element = (Element) it.next();
            String tag = element.getQName();
            if (tag.startsWith("var")) {
                if ((not % 2) == 0)
                    resultList.add(element);
                else {
                    resultList.add(new Element(null, "not", "not", null));
                    resultList.add(element);
                    resultList.add(new Element(null, "/not", "/not", null));
                }
            } else if (tag.equals("not")) {
                not++;
            } else if (tag.equals("/not")) {
                not--;
            } else if (tag.equals("and") || tag.equals("/and")) {
                if ((not % 2) == 0)
                    resultList.add(element);
                else {
                    String tagName = "or";
                    if (tag.equals("/and"))
                        tagName = "/" + tagName;
                    resultList.add(new Element(null, tagName, tagName, null));
                }

            } else if (tag.equals("or") || tag.equals("/or")) {
                if ((not % 2) == 0)
                    resultList.add(element);
                else {
                    String tagName = "and";
                    if (tag.equals("/or"))
                        tagName = "/" + tagName;
                    resultList.add(new Element(null, tagName, tagName, null));
                }
            } else
                resultList.add(element);
        }
        return resultList;
    }

    private List resolveAndCondition(List listResponse, int index, List oldResponseList, int id) throws ParseQuestionException {
        ListIterator it = listResponse.listIterator(index);
        boolean not = false;
        List newResponseList = new ArrayList();
        int or = 0;
        for (int i = 0; it.hasNext(); i++) {
            Element element = (Element) it.next();
            String tag = element.getQName();
            Attributes atts = element.getAttributes();

            if (tag.startsWith("var") && or == 0) {
                if (tag.equals("varequal") || tag.equals("varlt") || tag.equals("varlte") || tag.equals("vargt") || tag.equals("vargte")
                        || tag.equals("varsubstring")) {
                    if (!atts.getValue("respident").equals(responseId))
                        throw new ParseQuestionException("Exercício Inválido (identificadores inválidos)");
                    String tagName = tag;
                    if (not)
                        tagName = new String("not").concat(tagName);
                    if (atts.getIndex("case") != -1) {
                        if (atts.getValue("case").equals("Nocase"))
                            tagName = tagName.concat("ignorecase");
                    }
                    ResponseCondition rc = new ResponseCondition(tagName, element.getValue(), atts.getValue("respident"));

                    if (newResponseList.size() != 0) {
                        Iterator newResponseListIt = newResponseList.iterator();
                        while (newResponseListIt.hasNext()) {
                            ResponseProcessing rp = (ResponseProcessing) newResponseListIt.next();
                            rp.getResponseConditions().add(rc);

                        }
                    } else {
                        ResponseProcessing rp = new ResponseProcessing(id);
                        List rcList = new ArrayList();
                        rcList.add(rc);
                        rp.setResponseConditions(rcList);
                        newResponseList.add(rp);
                    }

                }
            } else if (tag.equals("or")) {
                if (or == 0)
                    newResponseList.addAll(resolveOrCondition(listResponse, index + i, newResponseList, id));
                or++;
            } else if ((tag.equals("not") || tag.equals("/not")) && or == 0) {
                if (not)
                    not = false;
                else
                    not = true;
            } else if (tag.equals("/and") && or == 0) {
                oldResponseList.addAll(newResponseList);
                return oldResponseList;
            } else if (tag.equals("/or")) {
                or--;
            }

        }
        oldResponseList.addAll(newResponseList);
        return oldResponseList;
    }

    private List resolveOrCondition(List listResponse, int index, List oldResponseList, int id) throws ParseQuestionException {
        ListIterator it = listResponse.listIterator(index);
        boolean not = false;
        List newResponseList = new ArrayList();
        int and = 0;
        for (int i = 0; it.hasNext(); i++) {
            Element element = (Element) it.next();
            String tag = element.getQName();
            Attributes atts = element.getAttributes();

            if (tag.startsWith("var") && and == 0) {
                if (tag.equals("varequal") || tag.equals("varlt") || tag.equals("varlte") || tag.equals("vargt") || tag.equals("vargte")
                        || tag.equals("varsubstring")) {
                    if (!atts.getValue("respident").equals(responseId))
                        throw new ParseQuestionException("Exercício Inválido (identificadores inválidos)");
                    String tagName = tag;
                    if (not)
                        tagName = new String("not").concat(tagName);
                    if (atts.getIndex("case") != -1) {
                        if (atts.getValue("case").equals("Nocase"))
                            tagName = tagName.concat("ignorecase");
                    }
                    if (oldResponseList.size() != 0) {
                        Iterator oldResponseListIt = oldResponseList.iterator();
                        while (oldResponseListIt.hasNext()) {
                            ResponseProcessing responseProcessing = (ResponseProcessing) oldResponseListIt.next();
                            responseProcessing.getResponseConditions().add(
                                    new ResponseCondition(tagName, element.getValue(), atts.getValue("respident")));
                            newResponseList.add(responseProcessing);
                        }
                    } else {
                        ResponseProcessing responseProcessing = new ResponseProcessing(id);
                        responseProcessing.setResponseConditions(new ArrayList());
                        responseProcessing.getResponseConditions()
                                .add(new ResponseCondition(tagName, element.getValue(), atts.getValue("respident")));
                        newResponseList.add(responseProcessing);
                    }
                }
            } else if (tag.equals("and")) {
                if (and == 0)
                    newResponseList = resolveAndCondition(listResponse, index + i, newResponseList, id);
                and++;
            } else if ((tag.equals("not") || tag.equals("/not")) && and == 0) {
                if (not)
                    not = false;
                else
                    not = true;
            } else if (tag.equals("/or") && and == 0) {
                return newResponseList;
            } else if (tag.equals("/and")) {
                and--;
            }

        }
        return newResponseList;
    }
}