package net.sourceforge.fenixedu.presentationTier.renderers.providers.student;

import net.sourceforge.fenixedu.dataTransferObject.student.elections.StudentVoteBean;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class YearCandidateDelegateManagementProvider implements DataProvider {

	@Override
	public Object provide(Object source, Object currentValue) {
		final StudentVoteBean studentVoteBean = (StudentVoteBean) source;
		return studentVoteBean.getSelectedStudentVote("candidate");
	}

	@Override
	public Converter getConverter() {
		return new DomainObjectKeyConverter();
	}

}
