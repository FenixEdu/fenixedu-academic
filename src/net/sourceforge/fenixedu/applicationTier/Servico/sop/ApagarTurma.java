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

import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.domain.ISchoolClass;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ApagarTurma implements IService {

    public Object run(InfoClass infoClass) throws ExcepcaoPersistencia {

        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        final ISchoolClass schoolClass = (ISchoolClass) sp.getITurmaPersistente().readByOID(
                SchoolClass.class, infoClass.getIdInternal());

        // Shift
        Iterator iter = schoolClass.getAssociatedShiftsIterator();
        while(iter.hasNext()){
            IShift shift = (IShift) iter.next();
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

        sp.getITurmaPersistente().deleteByOID(SchoolClass.class, schoolClass.getIdInternal());

        return new Boolean(true);
    }

}