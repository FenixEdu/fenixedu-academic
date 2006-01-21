/*
 * ApagarTurma.java
 *
 * Created on 27 de Outubro de 2002, 18:13
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * Serviço ApagarTurma.
 * 
 * @author tfc130
 * @author Pedro Santos e Rita Carvalho
 */
import java.util.Iterator;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ApagarTurma extends Service {

    public Object run(InfoClass infoClass) throws ExcepcaoPersistencia {
        final SchoolClass schoolClass = (SchoolClass) persistentSupport.getITurmaPersistente().readByOID(
                SchoolClass.class, infoClass.getIdInternal());

        // Shift
        Iterator iter = schoolClass.getAssociatedShiftsIterator();
        while(iter.hasNext()){
            Shift shift = (Shift) iter.next();
            iter.remove();
            shift.getAssociatedClasses().remove(schoolClass);
        }
        schoolClass.getAssociatedShifts().clear();

        // ExecutionDegree
        schoolClass.getExecutionDegree().getSchoolClasses().remove(schoolClass);
        schoolClass.setExecutionDegree(null);

        // ExecutionPeriod
        schoolClass.getExecutionPeriod().getSchoolClasses().remove(schoolClass);
        schoolClass.setExecutionPeriod(null);

        persistentSupport.getITurmaPersistente().deleteByOID(SchoolClass.class, schoolClass.getIdInternal());

        return new Boolean(true);
    }

}