package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import pt.ist.fenixWebFramework.services.Service;

public class ReadOpenExecutionYears {

    @Service
    public static List<ExecutionYear> run() throws FenixServiceException {
        return ExecutionYear.readOpenExecutionYears();
    }
}