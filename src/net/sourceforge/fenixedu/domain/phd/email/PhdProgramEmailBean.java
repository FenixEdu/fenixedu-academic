package net.sourceforge.fenixedu.domain.phd.email;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdProgram;

public class PhdProgramEmailBean extends PhdEmailBean {

    private static final long serialVersionUID = 1L;

    private PhdProgram phdProgram;
    private List<PhdIndividualProgramProcess> selectedElements;

    public PhdProgramEmailBean(PhdProgram phdProgram) {
	this.phdProgram = phdProgram;
    }

    public PhdProgram getPhdProgram() {
	return phdProgram;
    }

    public void setPhdProgram(PhdProgram phdProgram) {
	this.phdProgram = phdProgram;
    }

    public List<PhdIndividualProgramProcess> getSelectedElements() {
	return selectedElements;
    }

    public void setSelectedElements(List<PhdIndividualProgramProcess> selectedElements) {
	this.selectedElements = selectedElements;
    }

    public List<PhdIndividualProgramProcess> getIndividualProcesses() {
	List<PhdIndividualProgramProcess> list = new ArrayList<PhdIndividualProgramProcess>();

	for (DomainObject element : getSelectedElements()) {
	    list.add((PhdIndividualProgramProcess) element);
	}

	return list;
    }

}
