/*
 * Created on 2003/07/17
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * @author Luis Crus & Sara Ribeiro
 */

public class CreateExecutionPeriod implements IService {

    //SERVICO PARA SER REMOVIDO
    public Boolean run(InfoExecutionPeriod infoExecutionPeriodOfWorkingArea,
            InfoExecutionPeriod infoExecutionPeriodToExportDataFrom) {
        return null;
    }

    public class InvalidExecutionPeriod extends FenixServiceException {

        /**
         *  
         */
        InvalidExecutionPeriod() {
            super();
        }

    }

    public class ExistingExecutionPeriod extends FenixServiceException {

        /**
         *  
         */
        ExistingExecutionPeriod() {
            super();
        }

    }

}