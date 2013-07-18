package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import pt.ist.fenixWebFramework.services.Service;

public class ReadNotClosedExecutionYears {

    @Service
    public static List<InfoExecutionYear> run() {
        final List<InfoExecutionYear> result = new ArrayList<InfoExecutionYear>();
        for (final ExecutionYear executionYear : ExecutionYear.readNotClosedExecutionYears()) {
            result.add(InfoExecutionYear.newInfoFromDomain(executionYear));
        }
        return result;
    }
}