/*
 * Created on 25/Jul/2003
 */
package net.sourceforge.fenixedu.domain.onlineTests.utils;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import net.sourceforge.fenixedu.domain.onlineTests.Question;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.SubQuestion;
import net.sourceforge.fenixedu.util.tests.CardinalityType;
import net.sourceforge.fenixedu.util.tests.QuestionOption;
import net.sourceforge.fenixedu.util.tests.QuestionType;
import net.sourceforge.fenixedu.util.tests.RenderChoise;
import net.sourceforge.fenixedu.util.tests.RenderFIB;
import net.sourceforge.fenixedu.util.tests.ResponseCondition;
import net.sourceforge.fenixedu.util.tests.ResponseProcessing;
import net.sourceforge.fenixedu.util.tests.TestType;
import net.sourceforge.fenixedu.utilTests.Element;
import net.sourceforge.fenixedu.utilTests.ParseQuestionException;
import net.sourceforge.fenixedu.utilTests.QuestionResolver;

import org.apache.struts.util.LabelValueBean;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

import com.sun.faces.el.impl.parser.ParseException;

/**
 * @author Susana Fernandes
 */
public class ParseSubQuestion extends DefaultHandler {

    private String text;

    private Element current = null;

    private List<QuestionElement> questionElementList;

    private boolean questionPresentation = false, question = false, option = false, response = false,
            feedback = false;

    private static final Element NOT_ELEMENT = new Element(null, "not", "not", null);

    private static final Element SLASH_NOT_ELEMENT = new Element(null, "/not", "/not", null);

    public Question parseSubQuestion(Question question, String path) throws ParseQuestionException,
            ParseException {
        if (question.getSubQuestions() == null || question.getSubQuestions().size() == 0) {
            try {
                parseFile(question.getXmlFile(), path);
            } catch (Exception e) {
                throw new ParseException();
            }
            for (QuestionElement questionElement : questionElementList) {
                question.addSubQuestion(createSubQuestion(questionElement));
            }
        }
        return question;
    }

    // para o preview - só tem 1 item
    public SubQuestion parseSubQuestion(String fileString, String path) throws ParseQuestionException,
            ParseException {
        try {
            parseFile(fileString, path);
        } catch (Exception e) {
            throw new ParseException();
        }
        return createSubQuestion(questionElementList.get(0));
    }

    public StudentTestQuestion parseStudentTestQuestion(StudentTestQuestion studentTestQuestion,
            String path) throws Exception, ParseQuestionException {
        if (studentTestQuestion.getStudentSubQuestions() == null
                || studentTestQuestion.getStudentSubQuestions().size() == 0) {
            try {
                parseFile(studentTestQuestion.getQuestion().getXmlFile(), path);
            } catch (Exception e) {
                throw new ParseException();
            }
            for (QuestionElement questionElement : questionElementList) {
                studentTestQuestion.addStudentSubQuestion(createSubQuestion(questionElement));
            }
            SubQuestion subQuestion = studentTestQuestion.getSubQuestionByItem();
            if (!studentTestQuestion.getDistributedTest().getTestType().equals(new TestType(3))
                    && subQuestion.getQuestionType().getType().intValue() == QuestionType.LID) {
                String optionShuffle = studentTestQuestion.getOptionShuffle();
                if (optionShuffle == null || optionShuffle.length() == 0) {
                    if (subQuestion.getShuffle() == null) {
                        subQuestion.setShuffle(shuffleOptions(getQuestionElement(studentTestQuestion
                                .getItemId())));
                    }
                } else {
                    subQuestion.setShuffle(optionShuffle.substring(1, optionShuffle.length() - 1).split(
                            ","));
                }
                subQuestion.setOptions(shuffleStudentTestQuestionOptions(subQuestion.getShuffle(),
                        subQuestion.getOptions()));
                subQuestion.setResponseProcessingInstructions(shuffleStudentTestQuestionResponses(
                        subQuestion.getShuffle(), subQuestion.getResponseProcessingInstructions()));
            }
        }
        return studentTestQuestion;
    }

    private QuestionElement getQuestionElement(String questionItem) {
        if (!questionElementList.isEmpty() && questionElementList.size() == 1 && questionItem == null) {
            return questionElementList.iterator().next();
        }
        for (QuestionElement questionElement : questionElementList) {
            if (questionElement.getItemId().equals(questionItem)) {
                return questionElement;
            }
        }
        return null;
    }

    public void parseFile(String file, String path) throws ParserConfigurationException, IOException,
            SAXException {
        questionElementList = new ArrayList<QuestionElement>();
        questionPresentation = false;
        question = false;
        option = false;
        response = false;
        feedback = false;
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

        // if (itemId == null && !doneItem) {
        // item = true;
        // }
        // if (itemId != null && qName.equals("item") &&
        // attributes.getValue("ident").equals(itemId)) {
        // item = true;
        // }
        // if (item) {

        if (qName.equals("section")) {
            questionPresentation = true;
            question = false;
            if (questionElementList.size() == 0
                    || questionElementList.get(questionElementList.size() - 1).getListQuestion().size() != 0) {
                questionElementList.add(new QuestionElement());
            }
        } else if (qName.equals("item")) {
            questionPresentation = false;
            question = true;
            if (questionElementList.size() == 0
                    || questionElementList.get(questionElementList.size() - 1).getItemId() != null) {
                questionElementList.add(new QuestionElement());
            }
            questionElementList.get(questionElementList.size() - 1).setItemId(
                    attributes.getValue("ident"));
            if (attributes.getIndex("title") != -1) {
                questionElementList.get(questionElementList.size() - 1).setTitle(
                        attributes.getValue("title"));
            }

        } else if (qName.equals("response_lid") || qName.equals("response_str")
                || qName.equals("response_num")) {
            question = false;
            option = true;
        } else if (qName.equals("resprocessing")) {
            response = true;
            question = false;
            option = false;
            questionPresentation = false;
        } else if (qName.equals("itemfeedback")) {
            feedback = true;
            response = false;
            question = false;
            option = false;
            questionPresentation = false;
        }
        if (questionPresentation)
            questionElementList.get(questionElementList.size() - 1).addListQuestionPresentation(current);
        else if (question)
            questionElementList.get(questionElementList.size() - 1).addListQuestion(current);
        else if (option)
            questionElementList.get(questionElementList.size() - 1).addListOptions(current);
        else if (response)
            questionElementList.get(questionElementList.size() - 1).addListResponse(current);
        else if (feedback)
            questionElementList.get(questionElementList.size() - 1).addListFeedback(current);
        // }
        text = "";
    }

    public void endElement(String uri, String localName, String qName) {
        if (current != null && text != null) {
            current.setValue(text.trim());
        }
        current = null;
        // if (item) {
        if (qName.equals("response_lid")) {
            option = false;
            question = true;
        } else if (qName.equals("not") || qName.equals("and") || qName.equals("or")) {
            questionElementList.get(questionElementList.size() - 1).addListResponse(
                    new Element(uri, localName, "/" + qName, null));
        } else if (qName.equals("itemfeedback")) {
            questionElementList.get(questionElementList.size() - 1).addListFeedback(
                    new Element(uri, localName, "/" + qName, null));
            // } else if (qName.equals("item")) {
            // item = false;
            // doneItem = true;
            // }
        }
    }

    public void characters(char[] ch, int start, int length) {
        if (current != null && text != null) {
            String value = new String(ch, start, length);
            text += value;
        }
    }

    private SubQuestion createSubQuestion(QuestionElement questionElement) throws ParseQuestionException {
        SubQuestion subQuestion = new SubQuestion();
        subQuestion.setItemId(questionElement.getItemId());
        subQuestion.setTitle(questionElement.getTitle());
        subQuestion.setPrePresentation(getPresentation(questionElement.getListQuestionPresentation(),
                subQuestion));
        subQuestion.setPresentation(getPresentation(questionElement.getListQuestion(), subQuestion));
        subQuestion = getOptions(questionElement, subQuestion);
        subQuestion = getResponseProcessingInstructions(questionElement, subQuestion);
        subQuestion = getFeedback(questionElement, subQuestion);

        if (subQuestion.getQuestionType() != null
                && subQuestion.getQuestionType().getType().intValue() == QuestionType.LID) {
            subQuestion = getRidOfEmptyResponseConditions(subQuestion);
        }
        subQuestion = setFenixCorrectResponse(subQuestion);
        subQuestion = removeRepeatedConditions(subQuestion);
        if (subQuestion.getQuestionType() != null
                && subQuestion.getQuestionType().getType().intValue() == QuestionType.LID) {

            subQuestion.setResponseProcessingInstructions(newResponseList(subQuestion
                    .getResponseProcessingInstructions(), subQuestion.getOptions()));
            // subQuestion.setResponseProcessingInstructions(newResponseList(subQuestion.getResponseProcessingInstructions(),
            // subQuestion.getOptions()));
        }

        return subQuestion;
    }

    private List<LabelValueBean> getPresentation(List<Element> questionList, SubQuestion subQuestion)
            throws ParseQuestionException {
        List<LabelValueBean> presentationList = new ArrayList<LabelValueBean>();
        for (Element element : questionList) {
            String tag = element.getQName();
            Attributes atts = element.getAttributes();
            if (tag.startsWith("render_")) {
                if (!(tag.equals("render_choice") || tag.equals("render_fib")))
                    throw new ParseQuestionException(tag, true);
            } else if (tag.startsWith("mat") && !tag.equals("material")) {
                if ((tag.equals("mattext"))) {
                    String textType = "text/plain";
                    if (atts.getIndex("texttype") != -1) {
                        textType = atts.getValue("texttype");
                    }
                    presentationList.add(new LabelValueBean(textType, element.getValue()));
                } else if ((tag.equals("matimage"))) {
                    if (atts.getIndex("label") != -1)
                        presentationList.add(new LabelValueBean("image_label", atts.getValue("label")));
                    if (atts.getIndex("uri") != -1)
                        throw new ParseQuestionException(tag, "uri");
                    presentationList.add(new LabelValueBean(atts.getValue("imagtype"), element
                            .getValue()));
                } else
                    throw new ParseQuestionException(tag, true);
            } else if ((tag.startsWith("flow"))) {
                presentationList.add(new LabelValueBean("flow", ""));
            }
        }
        return presentationList;
    }

    private SubQuestion getOptions(QuestionElement questionElement, SubQuestion subQuestion)
            throws ParseQuestionException {
        List<LabelValueBean> optionsAuxList = new ArrayList<LabelValueBean>();
        int optionNumber = 0;
        // int questions = 0;
        QuestionOption questionOption = new QuestionOption();
        List<QuestionOption> optionList = new ArrayList<QuestionOption>();
        for (Element element : questionElement.getListOptions()) {
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
                    subQuestion.getQuestionType().setRender(renderFIB);
                } else if (tag.equals("render_choice")) {
                    RenderChoise renderChoise = new RenderChoise();
                    renderChoise.setShuffle(atts.getValue("shuffle"));
                    subQuestion.getQuestionType().setRender(renderChoise);
                } else
                    throw new ParseQuestionException(tag, true);
            } else if ((tag.startsWith("mat") && !tag.equals("material")) || tag.startsWith("response_")) {
                if ((tag.equals("response_lid")) || (tag.equals("response_str"))
                        || (tag.equals("response_num"))) {
                    questionElement.setResponseId(atts.getValue("ident"));
                    // questions++;
                    // if (questions > 1)
                    // throw new ParseQuestionException("O sistema ainda não
                    // suporta perguntas com alíneas.");

                    subQuestion.setQuestionType(new QuestionType(tag));

                    if (atts.getIndex("rcardinality") != -1) {
                        if (atts.getValue("rcardinality").equals("Ordered"))
                            throw new ParseQuestionException(tag, "rcardinality=Ordered");
                        subQuestion.getQuestionType().setCardinalityType(
                                new CardinalityType(atts.getValue("rcardinality")));
                    }
                } else if ((tag.equals("response_label")) || (tag.equals("response_na"))) {

                    if (questionOption.getOptionId() == null) {
                        questionOption.setOptionId(atts.getValue("ident"));
                    } else {
                        if (optionsAuxList.size() != 0) {
                            questionOption.setOptionContent(optionsAuxList);
                            optionList.add(questionOption);
                        }
                        questionOption = new QuestionOption(atts.getValue("ident"));
                        optionsAuxList = new ArrayList<LabelValueBean>();
                    }
                    if (tag.equals("response_na"))
                        questionOption.setEmptyResponse(true);

                    optionsAuxList.add(new LabelValueBean("response_label", atts.getValue("ident")));
                    optionNumber++;
                } else if ((tag.equals("mattext"))) {
                    String textType = "text/plain";
                    if (atts.getIndex("texttype") != -1) {
                        textType = atts.getValue("texttype");
                    }
                    optionsAuxList.add(new LabelValueBean(textType, element.getValue()));
                } else if ((tag.equals("matimage"))) {
                    if (atts.getIndex("label") != -1)
                        optionsAuxList.add(new LabelValueBean("image_label", atts.getValue("label")));
                    if (atts.getIndex("uri") != -1)
                        throw new ParseQuestionException(tag, "uri");
                    optionsAuxList
                            .add(new LabelValueBean(atts.getValue("imagtype"), element.getValue()));
                } else
                    throw new ParseQuestionException(tag, true);
            } else if ((tag.equals("flow"))) {
                optionsAuxList.add(new LabelValueBean("flow", ""));
            }
        }
        if (questionOption != null && optionsAuxList.size() != 0) {
            questionOption.setOptionContent(optionsAuxList);
            optionList.add(questionOption);
        }
        // subQuestion.setOptionNumber(new Integer(optionNumber));
        subQuestion.setOptions(optionList);

        return subQuestion;
    }

    private SubQuestion getResponseProcessingInstructions(QuestionElement questionElement,
            SubQuestion subQuestion) throws ParseQuestionException {
        List<Element> newResponseList = getRidOfNot(questionElement);
        ListIterator it = newResponseList.listIterator();
        List<ResponseProcessing> auxList = new ArrayList<ResponseProcessing>();
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
                    if (subQuestion.getQuestionValue() == null
                            || (subQuestion.getQuestionValue().compareTo(value) < 0))
                        subQuestion.setQuestionValue(value);
                } else if (responseProcessing.getAction().intValue() == ResponseProcessing.SUBTRACT) {
                    if (subQuestion.getQuestionValue() == null)
                        subQuestion.setQuestionValue(new Double("-" + value));
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
                if (responseProcessing != null && responseProcessing.getResponseConditions().size() != 0) {
                    auxList.add(responseProcessing);
                }
                responseProcessingId++;
                responseProcessing = new ResponseProcessing(responseProcessingId);
                responseProcessing.setResponseConditions(new ArrayList<ResponseCondition>());
            } else if (tag.startsWith("var")) {
                if (tag.equals("varequal") || tag.equals("varlt") || tag.equals("varlte")
                        || tag.equals("vargt") || tag.equals("vargte") || tag.equals("varsubstring")) {
                    if (!atts.getValue("respident").equals(questionElement.getResponseId()))
                        throw new ParseQuestionException(
                                "Exercício Inválido (identificadores inválidos)");
                    if (or == 0 && and == 0) {
                        String tagName = tag;
                        if (not)
                            tagName = new String("not").concat(tagName);
                        if (atts.getIndex("case") != -1) {
                            if (atts.getValue("case").equals("Nocase"))
                                tagName = tagName.concat("ignorecase");
                        }
                        if (subQuestion.getQuestionType().getType().intValue() == QuestionType.LID
                                && subQuestion.getQuestionType().getCardinalityType().getType()
                                        .intValue() == CardinalityType.SINGLE)
                            if (getNumberOfVarEquals(responseProcessing.getResponseConditions()) > 0)
                                throw new ParseQuestionException(
                                        "Uma das soluções indicadas no ficheiro tem mais do que uma resposta, e uma pergunta de escolha simples apenas admite uma resposta.");

                        responseProcessing.getResponseConditions().add(
                                new ResponseCondition(tagName, element.getValue(), atts
                                        .getValue("respident")));
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
                    auxList.addAll(resolveAndCondition(questionElement, newResponseList, i,
                            new ArrayList<ResponseProcessing>(), responseProcessingId));
                and++;
            } else if (tag.equals("/and")) {
                and--;
            } else if (tag.equals("or")) {
                if (or == 0 && and == 0)
                    auxList.addAll(resolveOrCondition(questionElement, newResponseList, i,
                            new ArrayList<ResponseProcessing>(), responseProcessingId));
                or++;
            } else if (tag.equals("/or")) {
                or--;
            } else if (tag.equals("displayfeedback")) {
                List<LabelValueBean> f = new ArrayList<LabelValueBean>();
                f.add(new LabelValueBean("linkrefid", atts.getValue("linkrefid")));
                responseProcessing.setFeedback(f);
                Iterator itAuxList = auxList.iterator();
                while (itAuxList.hasNext()) {
                    ResponseProcessing rp = (ResponseProcessing) itAuxList.next();
                    if (rp.getResponseProcessingId() == responseProcessing.getResponseProcessingId()) {
                        rp.setFeedback(f);
                    }
                }
            } else if (tag.equals("respcond_extension")) {
                boolean setNextItem = false;
                for (ResponseProcessing rp : auxList) {
                    if (rp.getResponseProcessingId() == responseProcessing.getResponseProcessingId()) {
                        rp.setNextItem(atts.getValue("itemident"));
                        setNextItem = true;
                    }
                }
                if (!setNextItem) {
                    responseProcessing.setNextItem(atts.getValue("itemident"));
                }
            }
        }
        if (responseProcessing != null && responseProcessing.getResponseConditions().size() != 0) {
            auxList.add(responseProcessing);
        }
        subQuestion.setResponseProcessingInstructions(auxList);
        return subQuestion;
    }

    private SubQuestion getFeedback(QuestionElement questionElement, SubQuestion subQuestion)
            throws ParseQuestionException {
        List<LabelValueBean> feedbackAuxList = new ArrayList<LabelValueBean>();
        List<ResponseProcessing> responses = subQuestion.getResponseProcessingInstructions();
        String ident = "";
        for (Element element : questionElement.getListFeedback()) {
            String tag = element.getQName();
            Attributes atts = element.getAttributes();

            if (tag.equals("itemfeedback")) {
                ident = atts.getValue("ident");
            } else if (tag.startsWith("mat") && !tag.equals("material")) {
                if ((tag.equals("mattext"))) {
                    String textType = "text/plain";
                    if (atts.getIndex("texttype") != -1) {
                        textType = atts.getValue("texttype");
                    }
                    feedbackAuxList.add(new LabelValueBean(textType, element.getValue()));
                } else if ((tag.equals("matimage"))) {
                    if (atts.getIndex("label") != -1)
                        feedbackAuxList.add(new LabelValueBean("image_label", atts.getValue("label")));
                    if (atts.getIndex("uri") != -1)
                        throw new ParseQuestionException(tag, "uri");
                    feedbackAuxList
                            .add(new LabelValueBean(atts.getValue("imagtype"), element.getValue()));
                } else
                    throw new ParseQuestionException(tag, true);
            } else if ((tag.startsWith("flow"))) {
                feedbackAuxList.add(new LabelValueBean("flow", ""));
            } else if (tag.equals("/itemfeedback")) {

                Iterator responsesIt = subQuestion.getResponseProcessingInstructions().iterator();
                for (int i = 0; responsesIt.hasNext(); i++) {
                    ResponseProcessing rp = (ResponseProcessing) responsesIt.next();
                    if (rp.getFeedback() != null && rp.getFeedback().size() > 0
                            && ((LabelValueBean) rp.getFeedback().get(0)).getValue().equals(ident))
                        rp.setFeedback(feedbackAuxList);
                    responses.set(i, rp);
                }
                feedbackAuxList = new ArrayList<LabelValueBean>();
            }
        }
        subQuestion.setResponseProcessingInstructions(responses);
        return subQuestion;
    }

    // private String imageById(QuestionElement questionElement, int imageId) {
    // int imageIdAux = 1;
    // for (Element element : questionElement.getListQuestion()) {
    // String tag = element.getQName();
    // if ((tag.equals("matimage"))) {
    // if (imageIdAux == imageId)
    // return element.getValue();
    // imageIdAux++;
    // }
    // }
    // for (Element element : questionElement.getListOptions()) {
    // String tag = element.getQName();
    // if ((tag.equals("matimage"))) {
    // if (imageIdAux == imageId)
    // return element.getValue();
    // imageIdAux++;
    // }
    // }
    // for (Element element : questionElement.getListFeedback()) {
    // String tag = element.getQName();
    // if ((tag.equals("matimage"))) {
    // if (imageIdAux == imageId)
    // return element.getValue();
    // imageIdAux++;
    // }
    // }
    // return null;
    // }

    private String[] shuffleOptions(QuestionElement questionElement) {
        Vector<String> v = new Vector<String>();
        Vector<String> vRandom = new Vector<String>();
        int optionNumber = 0;
        for (Element element : questionElement.getListOptions()) {
            String tag = element.getQName();
            Attributes atts = element.getAttributes();

            if (tag.equals("response_label") || tag.equals("response_na")) {
                optionNumber++;
                if (atts.getValue(atts.getIndex("rshuffle")).equals("Yes")) {
                    v.add("");
                    vRandom.add(new Integer(v.size()).toString());
                    continue;
                }
                v.add(new Integer(v.size() + 1).toString());
            }
        }

        Random r = new Random();
        boolean ready = false;
        for (String id : vRandom) {
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
        return v.toArray(new String[v.size()]);
    }

    private List<QuestionOption> shuffleStudentTestQuestionOptions(String[] shuffle,
            List<QuestionOption> oldList) {
        if (shuffle == null) {
            return oldList;
        }
        List<QuestionOption> newList = new ArrayList<QuestionOption>();
        // String[] aux = shuffle.substring(1, shuffle.length() - 1).split(",
        // ");
        for (int i = 0; i < shuffle.length; i++)
            newList.add(i, oldList.get(new Integer(shuffle[i].trim()).intValue() - 1));

        return newList;
    }

    private List<ResponseProcessing> shuffleStudentTestQuestionResponses(String[] shuffle,
            List<ResponseProcessing> oldResponseProcessingList) {
        if (shuffle == null) {
            return oldResponseProcessingList;
        }

        List<ResponseProcessing> newResponseProcessingList = new ArrayList<ResponseProcessing>();
        for (ResponseProcessing oldResponseProcessing : oldResponseProcessingList) {
            List<ResponseCondition> newResponseConditionList = new ArrayList<ResponseCondition>();
            for (ResponseCondition oldResponseCondition : oldResponseProcessing.getResponseConditions()) {
                ResponseCondition newResponseCondition = oldResponseCondition;
                newResponseCondition.setResponse(new Integer(getPosition(shuffle, oldResponseCondition
                        .getResponse())).toString());
                newResponseConditionList.add(newResponseCondition);
            }
            ResponseProcessing newReponseProcessing = oldResponseProcessing;
            newReponseProcessing.setResponseConditions(newResponseConditionList);
            newResponseProcessingList.add(newReponseProcessing);
        }

        return newResponseProcessingList;
    }

    private int getPosition(String[] shuffle, String value) {
        for (int i = 0; i < shuffle.length; i++) {
            if (shuffle[i].equals(value)) {
                return i + 1;
            }
        }
        return 0;
    }

    public List<ResponseProcessing> newResponseList(List<ResponseProcessing> responseList,
            List<QuestionOption> optionList) {
        List<ResponseProcessing> newResponseProcessingList = new ArrayList<ResponseProcessing>();

        for (ResponseProcessing responseProcessing : responseList) {
            List<ResponseCondition> newResponseConditionList = new ArrayList<ResponseCondition>();
            for (ResponseCondition responseCondition : (List<ResponseCondition>) responseProcessing
                    .getResponseConditions()) {
                String response = responseCondition.getResponse();
                ResponseCondition newResponseCondition = null;
                int index = 1;
                for (QuestionOption option : optionList) {
                    if (option.getOptionId().equals(response)) {
                        newResponseCondition = new ResponseCondition(ResponseCondition
                                .getConditionString(responseCondition.getCondition()),
                                new Integer(index).toString(), responseCondition.getResponseLabelId());
                    } else {
                        index++;
                    }
                }
                newResponseConditionList.add(newResponseCondition);
            }
            ResponseProcessing newResponseProcessing = new ResponseProcessing(newResponseConditionList,
                    responseProcessing.getResponseValue(), responseProcessing.getAction(),
                    responseProcessing.getFeedback(), responseProcessing.isFenixCorrectResponse());
            newResponseProcessing.setNextItem(responseProcessing.getNextItem());
            newResponseProcessingList.add(newResponseProcessing);
        }
        return newResponseProcessingList;
    }

    public SubQuestion setFenixCorrectResponse(SubQuestion subQuestion) {
        if (subQuestion.getResponseProcessingInstructions().size() != 0) {
            Iterator itResponseProcessing = subQuestion.getResponseProcessingInstructions().iterator();
            int fenixCorrectResponseIndex = -1;
            double maxValue = 0;
            int previewsAction = 0;
            for (int i = 0; itResponseProcessing.hasNext(); i++) {
                ResponseProcessing responseProcessing = (ResponseProcessing) itResponseProcessing.next();
                if (responseProcessing.getResponseValue() != null
                        && responseProcessing.getAction() != null) {

                    if ((responseProcessing.getResponseValue().doubleValue() > maxValue)
                            || (responseProcessing.getResponseValue().doubleValue() == maxValue && previewsAction == 0)
                            || (responseProcessing.getResponseValue().doubleValue() == maxValue
                                    && previewsAction != ResponseProcessing.SET && responseProcessing
                                    .getAction().intValue() == ResponseProcessing.SET)) {
                        maxValue = responseProcessing.getResponseValue().doubleValue();
                        fenixCorrectResponseIndex = i;
                        previewsAction = responseProcessing.getAction().intValue();
                    }
                }
            }
            if (fenixCorrectResponseIndex != -1)
                ((ResponseProcessing) subQuestion.getResponseProcessingInstructions().get(
                        fenixCorrectResponseIndex)).setFenixCorrectResponse(true);
        }
        return subQuestion;
    }

    public SubQuestion removeRepeatedConditions(SubQuestion subQuestion) {
        List<ResponseProcessing> newRpList = new ArrayList<ResponseProcessing>();
        if (subQuestion.getResponseProcessingInstructions().size() > 1) {
            boolean isLID = false;
            if (subQuestion.getQuestionType().getType().intValue() == QuestionType.LID) {
                isLID = true;
            }

            newRpList.add(subQuestion.getResponseProcessingInstructions().get(0));
            for (ResponseProcessing responseProcessing : subQuestion.getResponseProcessingInstructions()) {
                if (!responseProcessing.isThisConditionListInResponseProcessingList(newRpList, isLID)) {
                    newRpList.add(responseProcessing);
                }
            }
            subQuestion.setResponseProcessingInstructions(newRpList);
        }
        return subQuestion;
    }

    private int getNumberOfVarEquals(List<ResponseCondition> rcList) {
        int result = 0;
        for (ResponseCondition responseCondition : rcList) {
            if (responseCondition.getCondition().intValue() == ResponseCondition.VAREQUAL) {
                result++;
            }
        }
        return result;
    }

    public SubQuestion getRidOfEmptyResponseConditions(SubQuestion subQuestion) {
        List<ResponseProcessing> newResponseProcessingInstructions = new ArrayList<ResponseProcessing>();
        for (ResponseProcessing rp : subQuestion.getResponseProcessingInstructions()) {
            boolean empty = true;
            if (rp.getNextItem() == null || rp.getNextItem().length() == 0) {
                for (ResponseCondition rc : rp.getResponseConditions()) {
                    if (rc.getCondition().intValue() != ResponseCondition.NOTVAREQUAL) {
                        empty = false;
                    }
                }
            } else {
                empty = false;
            }
            if (!empty) {
                newResponseProcessingInstructions.add(rp);
            }
        }
        subQuestion.setResponseProcessingInstructions(newResponseProcessingInstructions);
        return subQuestion;
    }

    private List<Element> getRidOfNot(QuestionElement questionElement) {
        List<Element> resultList = new ArrayList<Element>();
        int not = 0;
        for (Element element : questionElement.getListResponse()) {
            String tag = element.getQName();
            if (tag.startsWith("var")) {
                if ((not % 2) == 0)
                    resultList.add(element);
                else {
                    resultList.add(NOT_ELEMENT);
                    resultList.add(element);
                    resultList.add(SLASH_NOT_ELEMENT);
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

    private List<ResponseProcessing> resolveAndCondition(QuestionElement questionElement,
            List<Element> listResponse, int index, List<ResponseProcessing> oldResponseList, int id)
            throws ParseQuestionException {
        ListIterator it = listResponse.listIterator(index);
        boolean not = false;
        List<ResponseProcessing> newResponseList = new ArrayList<ResponseProcessing>();
        int or = 0;
        for (int i = 0; it.hasNext(); i++) {
            Element element = (Element) it.next();
            String tag = element.getQName();
            Attributes atts = element.getAttributes();

            if (tag.startsWith("var") && or == 0) {
                if (tag.equals("varequal") || tag.equals("varlt") || tag.equals("varlte")
                        || tag.equals("vargt") || tag.equals("vargte") || tag.equals("varsubstring")) {
                    if (!atts.getValue("respident").equals(questionElement.getResponseId()))
                        throw new ParseQuestionException(
                                "Exercício Inválido (identificadores inválidos)");
                    String tagName = tag;
                    if (not)
                        tagName = new String("not").concat(tagName);
                    if (atts.getIndex("case") != -1) {
                        if (atts.getValue("case").equals("Nocase"))
                            tagName = tagName.concat("ignorecase");
                    }
                    ResponseCondition rc = new ResponseCondition(tagName, element.getValue(), atts
                            .getValue("respident"));

                    if (newResponseList.size() != 0) {
                        Iterator newResponseListIt = newResponseList.iterator();
                        while (newResponseListIt.hasNext()) {
                            ResponseProcessing rp = (ResponseProcessing) newResponseListIt.next();
                            rp.getResponseConditions().add(rc);

                        }
                    } else {
                        ResponseProcessing rp = new ResponseProcessing(id);
                        List<ResponseCondition> rcList = new ArrayList<ResponseCondition>();
                        rcList.add(rc);
                        rp.setResponseConditions(rcList);
                        newResponseList.add(rp);
                    }

                }
            } else if (tag.equals("or")) {
                if (or == 0)
                    newResponseList.addAll(resolveOrCondition(questionElement, listResponse, index + i,
                            newResponseList, id));
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

    private List<ResponseProcessing> resolveOrCondition(QuestionElement questionElement,
            List<Element> listResponse, int index, List<ResponseProcessing> oldResponseList, int id)
            throws ParseQuestionException {
        ListIterator it = listResponse.listIterator(index);
        boolean not = false;
        List<ResponseProcessing> newResponseList = new ArrayList<ResponseProcessing>();
        int and = 0;
        for (int i = 0; it.hasNext(); i++) {
            Element element = (Element) it.next();
            String tag = element.getQName();
            Attributes atts = element.getAttributes();

            if (tag.startsWith("var") && and == 0) {
                if (tag.equals("varequal") || tag.equals("varlt") || tag.equals("varlte")
                        || tag.equals("vargt") || tag.equals("vargte") || tag.equals("varsubstring")) {
                    if (!atts.getValue("respident").equals(questionElement.getResponseId()))
                        throw new ParseQuestionException(
                                "Exercício Inválido (identificadores inválidos)");
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
                            ResponseProcessing responseProcessing = (ResponseProcessing) oldResponseListIt
                                    .next();
                            responseProcessing.getResponseConditions().add(
                                    new ResponseCondition(tagName, element.getValue(), atts
                                            .getValue("respident")));
                            newResponseList.add(responseProcessing);
                        }
                    } else {
                        ResponseProcessing responseProcessing = new ResponseProcessing(id);
                        responseProcessing.setResponseConditions(new ArrayList<ResponseCondition>());
                        responseProcessing.getResponseConditions().add(
                                new ResponseCondition(tagName, element.getValue(), atts
                                        .getValue("respident")));
                        newResponseList.add(responseProcessing);
                    }
                }
            } else if (tag.equals("and")) {
                if (and == 0)
                    newResponseList = resolveAndCondition(questionElement, listResponse, index + i,
                            newResponseList, id);
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