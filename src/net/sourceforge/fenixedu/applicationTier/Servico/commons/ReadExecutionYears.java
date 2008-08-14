package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.struts.util.LabelValueBean;

public class ReadExecutionYears extends Service {
    public List run() throws FenixServiceException {
	final List<LabelValueBean> result = new ArrayList<LabelValueBean>();
	for (final ExecutionYear executionYear : rootDomainObject.getExecutionYears()) {
	    result.add(new LabelValueBean(executionYear.getYear(), executionYear.getYear()));
	}
	return result;
    }
}