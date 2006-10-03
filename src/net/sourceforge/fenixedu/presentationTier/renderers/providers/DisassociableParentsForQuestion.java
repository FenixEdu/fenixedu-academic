package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.tests.NewQuestionGroup;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.tests.GroupElementBean;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

import org.apache.commons.beanutils.BeanComparator;

public class DisassociableParentsForQuestion implements DataProvider {

	public Object provide(Object source, Object currentValue) {
		GroupElementBean groupElementBean = (GroupElementBean) source;

		List<NewQuestionGroup> allParents;

		if (groupElementBean.getChild().getParentQuestionGroups().size() == 1) {
			allParents = new ArrayList<NewQuestionGroup>();
		} else {
			allParents = groupElementBean.getChild().getParentQuestionGroups();
		}

		return allParents;
	}

	public Converter getConverter() {
		return null;
	}

}
