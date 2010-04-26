package net.sourceforge.fenixedu.presentationTier.Action.phd;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.presentationTier.renderers.providers.AbstractDomainObjectProvider;

import org.apache.commons.collections.comparators.ReverseComparator;

public class ManageEnrolmentsBean implements Serializable {

    static private final long serialVersionUID = 1L;

    private PhdIndividualProgramProcess process;

    private ExecutionSemester semester;

    private Collection<Enrolment> enrolments;

    public PhdIndividualProgramProcess getProcess() {
	return process;
    }

    public void setProcess(PhdIndividualProgramProcess process) {
	this.process = process;
    }

    public ExecutionSemester getSemester() {
	return semester;
    }

    public void setSemester(ExecutionSemester semester) {
	this.semester = semester;
    }

    public Collection<Enrolment> getEnrolments() {
	return enrolments;
    }

    public void setEnrolments(Collection<Enrolment> enrolments) {
	this.enrolments = enrolments;
    }

    static public class PhdManageEnrolmentsExecutionSemestersProviders extends AbstractDomainObjectProvider {

	@Override
	public Object provide(Object source, Object obj) {
	    final ManageEnrolmentsBean bean = (ManageEnrolmentsBean) source;

	    final List<ExecutionSemester> result = new ArrayList<ExecutionSemester>();

	    ExecutionSemester each = bean.getProcess().getExecutionYear().getFirstExecutionPeriod();
	    while (each != null) {
		result.add(each);
		each = each.getNextExecutionPeriod();
	    }

	    Collections.sort(result, new ReverseComparator(ExecutionSemester.COMPARATOR_BY_SEMESTER_AND_YEAR));

	    return result;
	}

    }
}
