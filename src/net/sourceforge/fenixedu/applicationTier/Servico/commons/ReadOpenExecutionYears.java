package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadOpenExecutionYears extends Service {

    public List<ExecutionYear> run() throws FenixServiceException{
        return ExecutionYear.readOpenExecutionYears();
    }
}