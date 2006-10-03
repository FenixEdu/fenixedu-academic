package net.sourceforge.fenixedu.tools.ims;

import java.util.HashMap;

import net.sourceforge.fenixedu.domain.tests.NewQuestionType;
import net.sourceforge.fenixedu.domain.tests.NewTestElement;
import net.sourceforge.fenixedu.domain.tests.predicates.Predicate;

import org.jdom.Element;

public interface PredicateElementProcessable {
	public Predicate process(Element element, HashMap<String, NewTestElement> transformationMap, NewQuestionType questionType);
}
