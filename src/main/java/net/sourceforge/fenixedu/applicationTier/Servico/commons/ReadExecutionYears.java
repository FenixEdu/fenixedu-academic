package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.apache.struts.util.LabelValueBean;

import pt.ist.fenixWebFramework.services.Service;

public class ReadExecutionYears {
    @Service
    public static List run() throws FenixServiceException {
        final List<LabelValueBean> result = new ArrayList<LabelValueBean>();
        for (final ExecutionYear executionYear : RootDomainObject.getInstance().getExecutionYears()) {
            result.add(new LabelValueBean(executionYear.getYear(), executionYear.getYear()));
        }
        return result;
    }
}