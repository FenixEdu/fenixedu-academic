/*
 * Created on Oct 7, 2004
 */
package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;

/**
 * @author nmgo
 * @author lmre
 */
public class ReadOpenExecutionYears extends Service {

    public List run() throws FenixServiceException, ExcepcaoPersistencia {
        
        IPersistentExecutionYear persistentExecutionYear = persistentSupport.getIPersistentExecutionYear();
        
        return persistentExecutionYear.readOpenExecutionYears();

    }
}