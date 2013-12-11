package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionYear;

import org.apache.struts.util.LabelValueBean;

import pt.ist.bennu.core.domain.Bennu;
import pt.ist.fenixframework.Atomic;

public class ReadExecutionYears {
    @Atomic
    public static List run() throws FenixServiceException {
        final List<LabelValueBean> result = new ArrayList<LabelValueBean>();
        for (final ExecutionYear executionYear : Bennu.getInstance().getExecutionYearsSet()) {
            result.add(new LabelValueBean(executionYear.getYear(), executionYear.getYear()));
        }
        return result;
    }
}