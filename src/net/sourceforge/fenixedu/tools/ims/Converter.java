package net.sourceforge.fenixedu.tools.ims;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.tests.NewAtomicQuestion;
import net.sourceforge.fenixedu.domain.tests.NewChoice;
import net.sourceforge.fenixedu.domain.tests.NewCorrector;
import net.sourceforge.fenixedu.domain.tests.NewMultipleChoiceQuestion;
import net.sourceforge.fenixedu.domain.tests.NewNumericQuestion;
import net.sourceforge.fenixedu.domain.tests.NewPresentationMaterial;
import net.sourceforge.fenixedu.domain.tests.NewQuestionType;
import net.sourceforge.fenixedu.domain.tests.NewStringMaterial;
import net.sourceforge.fenixedu.domain.tests.NewTestElement;
import net.sourceforge.fenixedu.domain.tests.predicates.AndPredicate;
import net.sourceforge.fenixedu.domain.tests.predicates.MultipleChoiceAnswerPredicate;
import net.sourceforge.fenixedu.domain.tests.predicates.MultipleChoiceCountPredicate;
import net.sourceforge.fenixedu.domain.tests.predicates.NotPredicate;
import net.sourceforge.fenixedu.domain.tests.predicates.NumericEqualsPredicate;
import net.sourceforge.fenixedu.domain.tests.predicates.NumericGreaterThanOrEqualPredicate;
import net.sourceforge.fenixedu.domain.tests.predicates.NumericGreaterThanPredicate;
import net.sourceforge.fenixedu.domain.tests.predicates.NumericLessThanOrEqualPredicate;
import net.sourceforge.fenixedu.domain.tests.predicates.NumericLessThanPredicate;
import net.sourceforge.fenixedu.domain.tests.predicates.OrPredicate;
import net.sourceforge.fenixedu.domain.tests.predicates.Predicate;
import net.sourceforge.fenixedu.stm.Transaction;

import org.jaxen.JaxenException;
import org.jaxen.jdom.JDOMXPath;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class Converter {

	public static void convert(InputStream stream, int maximumValue) throws JDOMException, IOException,
			JaxenException {
		List<NewAtomicQuestion> atomicQuestions = new ArrayList<NewAtomicQuestion>();
		SAXBuilder builder = new SAXBuilder(false);
		builder.setEntityResolver(new EntityResolver() {

			public InputSource resolveEntity(String publicId, String systemId) throws SAXException,
					IOException {
				return new InputSource(new FileInputStream("metadata/ims/qtiasiv1p2.dtd"));
			}
		});

		Document document = builder.build(stream);
		JDOMXPath path = new JDOMXPath("//item");
		List<Element> items = path.selectNodes(document);
		for (Element item : items) {
			atomicQuestions.add(processItem(item, new HashMap<String, NewTestElement>(), maximumValue));
		}
	}

	private static NewAtomicQuestion processItem(Element item,
			HashMap<String, NewTestElement> transformationMap, int maximumValue) throws JaxenException {
		NewAtomicQuestion atomicQuestion;

		JDOMXPath path = new JDOMXPath("presentation/flow/flow/response_lid");
		Element response_lid = (Element) path.selectSingleNode(item);

		path = new JDOMXPath("presentation/flow/flow/response_num");
		Element response_num = (Element) path.selectSingleNode(item);

		if (response_lid != null) {
			atomicQuestion = processResponseLid(response_lid, transformationMap, maximumValue, item);
		} else if (response_num != null) {
			atomicQuestion = processResponseNum(response_num, transformationMap, maximumValue, item);
		} else {
			return null;
		}

		path = new JDOMXPath("presentation/flow/flow");
		List<Element> flows = path.selectNodes(item);

		for (Element flow : flows) {
			processFlows(flow, atomicQuestion, transformationMap);
		}

		return atomicQuestion;
	}

	private static NewMultipleChoiceQuestion processResponseLid(Element response_lid,
			HashMap<String, NewTestElement> transformationMap, int maximumValue, Element item)
			throws JaxenException {
		NewMultipleChoiceQuestion multipleChoiceQuestion = new NewMultipleChoiceQuestion();

		String rcardinality = response_lid.getAttributeValue("rcardinality");
		if (rcardinality == null) {
			rcardinality = "Single";
		}
		if (rcardinality.equalsIgnoreCase("Single")) {
			multipleChoiceQuestion.setValidator(new MultipleChoiceCountPredicate(1));
		}

		transformationMap.put(response_lid.getAttributeValue("ident"), multipleChoiceQuestion);

		Element render_choice = response_lid.getChild("render_choice");

		String shuffle = render_choice.getAttributeValue("shuffle");
		if (shuffle == null) {
			shuffle = "Yes";
		}

		multipleChoiceQuestion.setShuffle(shuffle.equalsIgnoreCase("Yes"));

		JDOMXPath path = new JDOMXPath("render_choice/response_label");
		List<Element> responseLabels = path.selectNodes(response_lid);

		for (Element responseLabel : responseLabels) {
			processResponseLabel(responseLabel, multipleChoiceQuestion, transformationMap);
		}

		Element responseNa = response_lid.getChild("render_choice").getChild("response_na");
		processResponseLabel(responseNa, multipleChoiceQuestion, transformationMap);

		if (rcardinality.equalsIgnoreCase("Single")) {
			multipleChoiceQuestion.setValidator(new MultipleChoiceCountPredicate(1));
		}

		path = new JDOMXPath("//respcondition");
		List<Element> respConditions = path.selectNodes(item);

		for (Element respCondition : respConditions) {
			processRespCondition(respCondition, multipleChoiceQuestion, transformationMap,
					maximumValue, NewQuestionType.MULTIPLE_CHOICE_QUESTION);
		}

		return multipleChoiceQuestion;
	}

	private static NewNumericQuestion processResponseNum(Element response_num,
			HashMap<String, NewTestElement> transformationMap, int maximumValue, Element item)
			throws JaxenException {
		NewNumericQuestion numericQuestion = new NewNumericQuestion();

		transformationMap.put(response_num.getAttributeValue("ident"), numericQuestion);

		JDOMXPath path = new JDOMXPath("//respcondition");
		List<Element> respConditions = path.selectNodes(item);

		for (Element respCondition : respConditions) {
			processRespCondition(respCondition, numericQuestion, transformationMap,
					maximumValue, NewQuestionType.NUMERIC_QUESTION);
		}

		return numericQuestion;
	}

	private static void processRespCondition(Element respCondition,
			NewAtomicQuestion atomicQuestion,
			HashMap<String, NewTestElement> transformationMap, int maximumValue,
			NewQuestionType questionType) {
		NewCorrector corrector = new NewCorrector();

		Element conditionVar = respCondition.getChild("conditionvar");

		List<Predicate> predicates = processPredicates(conditionVar, transformationMap, questionType);
		Predicate predicate;

		if (conditionVar.getChildren().size() == 1) {
			AndPredicate andPredicate = new AndPredicate();
			for (Predicate eachPredicate : predicates) {
				andPredicate.getPredicates().add(eachPredicate);
			}
			predicate = andPredicate;
		} else {
			predicate = predicates.get(0);
		}

		Element setVar = respCondition.getChild("setvar");

		corrector.setAtomicQuestion(atomicQuestion);
		corrector.setPredicate(predicate);
		corrector.setPercentage(Math.round((Integer.parseInt(setVar.getText()) * 100) / maximumValue));
	}

	private static final HashMap<String, PredicateElementProcessable> processMap = new HashMap<String, PredicateElementProcessable>();
	static {
		processMap.put("varequal", new ProcessVarEqual());
		processMap.put("varlt", new processVarLT());
		processMap.put("vargt", new processVarGT());
		processMap.put("varlte", new processVarLTE());
		processMap.put("vargte", new processVarGTE());
		processMap.put("and", new processAnd());
		processMap.put("or", new processOr());
		processMap.put("not", new processNot());
	}

	private static List<Predicate> processPredicates(Element element,
			HashMap<String, NewTestElement> transformationMap, NewQuestionType questionType) {
		List<Predicate> predicates = new ArrayList<Predicate>();
		for (Iterator<Element> i = element.getChildren().iterator(); i.hasNext();) {
			Element predicateElement = i.next();
			predicates.add(processMap.get(predicateElement.getName()).process(predicateElement,
					transformationMap, questionType));
		}
		return predicates;
	}

	private static class ProcessVarEqual implements PredicateElementProcessable {

		public Predicate process(Element element, HashMap<String, NewTestElement> transformationMap,
				NewQuestionType questionType) {
			switch (questionType) {
			case MULTIPLE_CHOICE_QUESTION:
				return new MultipleChoiceAnswerPredicate((NewChoice) transformationMap.get(element
						.getTextTrim()));
			case NUMERIC_QUESTION:
				return new NumericEqualsPredicate(Double.parseDouble(element.getTextTrim()));
			default:
				return null;
			}
		}

	}

	private static class processVarLT implements PredicateElementProcessable {

		public Predicate process(Element element, HashMap<String, NewTestElement> transformationMap,
				NewQuestionType questionType) {
			switch (questionType) {
			case NUMERIC_QUESTION:
				return new NumericLessThanPredicate(Integer.parseInt(element.getTextTrim()));
			default:
				return null;
			}
		}

	}

	private static class processVarGT implements PredicateElementProcessable {

		public Predicate process(Element element, HashMap<String, NewTestElement> transformationMap,
				NewQuestionType questionType) {
			switch (questionType) {
			case NUMERIC_QUESTION:
				return new NumericGreaterThanPredicate(Integer.parseInt(element.getTextTrim()));
			default:
				return null;
			}
		}

	}

	private static class processVarLTE implements PredicateElementProcessable {

		public Predicate process(Element element, HashMap<String, NewTestElement> transformationMap,
				NewQuestionType questionType) {
			switch (questionType) {
			case NUMERIC_QUESTION:
				return new NumericLessThanOrEqualPredicate(Integer.parseInt(element.getTextTrim()));
			default:
				return null;
			}
		}

	}

	private static class processVarGTE implements PredicateElementProcessable {

		public Predicate process(Element element, HashMap<String, NewTestElement> transformationMap,
				NewQuestionType questionType) {
			switch (questionType) {
			case NUMERIC_QUESTION:
				return new NumericGreaterThanOrEqualPredicate(Integer.parseInt(element.getTextTrim()));
			default:
				return null;
			}
		}

	}

	private static class processNot implements PredicateElementProcessable {

		public Predicate process(Element element, HashMap<String, NewTestElement> transformationMap,
				NewQuestionType questionType) {
			NotPredicate notPredicate = new NotPredicate();

			List<Predicate> predicates = processPredicates(element, transformationMap, questionType);
			for (Predicate predicate : predicates) {
				notPredicate.getPredicates().add(predicate);
			}

			return notPredicate;
		}

	}

	private static class processAnd implements PredicateElementProcessable {

		public Predicate process(Element element, HashMap<String, NewTestElement> transformationMap,
				NewQuestionType questionType) {
			AndPredicate andPredicate = new AndPredicate();

			List<Predicate> predicates = processPredicates(element, transformationMap, questionType);
			for (Predicate predicate : predicates) {
				andPredicate.getPredicates().add(predicate);
			}

			return andPredicate;
		}

	}

	private static class processOr implements PredicateElementProcessable {

		public Predicate process(Element element, HashMap<String, NewTestElement> transformationMap,
				NewQuestionType questionType) {
			OrPredicate orPredicate = new OrPredicate();

			List<Predicate> predicates = processPredicates(element, transformationMap, questionType);
			for (Predicate predicate : predicates) {
				orPredicate.getPredicates().add(predicate);
			}

			return orPredicate;
		}

	}

	private static void processResponseLabel(Element response_label,
			NewMultipleChoiceQuestion multipleChoiceQuestion,
			HashMap<String, NewTestElement> transformationMap) throws JaxenException {
		NewChoice choice = new NewChoice(multipleChoiceQuestion);

		transformationMap.put(response_label.getAttributeValue("ident"), choice);

		JDOMXPath path = new JDOMXPath("material");
		List<Element> materials = path.selectNodes(response_label);

		for (Element material : materials) {
			processMaterial(material, choice);
		}
	}

	private static void processFlows(Element flow, NewTestElement testElement,
			HashMap<String, NewTestElement> transformationMap) throws JaxenException {
		JDOMXPath path = new JDOMXPath("material");
		List<Element> materials = path.selectNodes(flow);

		for (Element material : materials) {
			processMaterial(material, testElement);
		}
	}

	private static NewPresentationMaterial processMaterial(Element material, NewTestElement testElement) {
		if (material.getChild("mattext") != null) {
			return processTextMaterial(material.getChild("mattext"), testElement);
		} else if (material.getChild("matimage") != null) {
			// presentationMaterials.add(processImageMaterial(material.element("matimage")));
		}

		return null;
	}

	private static NewPresentationMaterial processTextMaterial(Element element,
			NewTestElement testElement) {
		return new NewStringMaterial(testElement, true, element.getText());
	}

	public static void main(String[] args) throws Exception {
		try {
			Transaction.abort();
			Transaction.commit();
		} catch (Exception e) {

		}
		Transaction.begin();
		DomainObject.turnOffLockMode();
		RootDomainObject.initTests();

		String filePath = "/home/jpmsi/Desktop/ims/novos/caixaNumerica/caixaNumerica1.xml";
		FileInputStream stream = new FileInputStream(filePath);
		convert(stream, 1);

		Transaction.abort();
	}

}
