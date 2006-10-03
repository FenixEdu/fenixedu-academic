package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;

import net.sourceforge.fenixedu.domain.tests.NewQuestionGroup;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.tests.GroupElementBean;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class AssociableParentsForQuestion implements DataProvider {

	public Object provide(Object source, Object currentValue) {
		GroupElementBean groupElementBean = (GroupElementBean) source;
		
		List<NewQuestionGroup> possibleParents = groupElementBean.getChild().getAssociableParents();

		return possibleParents;
	}

	public Converter getConverter() {
		return null;
	}

}
