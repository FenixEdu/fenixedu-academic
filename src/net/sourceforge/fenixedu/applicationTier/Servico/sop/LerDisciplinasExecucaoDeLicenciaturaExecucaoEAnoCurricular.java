package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * Serviço LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular
 * 
 * @author tfc130
 * @version
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular implements IService {

    public List run(InfoExecutionDegree infoExecutionDegree, InfoExecutionPeriod infoExecutionPeriod,
            Integer curricularYear) throws ExcepcaoPersistencia {

        List listDCDE = null;
        List listInfoDE = null;

            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentExecutionCourse executionCourseDAO = sp.getIPersistentExecutionCourse();

            CurricularYear curricularYearFromDB = (CurricularYear) sp.getIPersistentCurricularYear()
                    .readByOID(CurricularYear.class, curricularYear);

            listDCDE = executionCourseDAO
                    .readByExecutionPeriodAndExecutionDegreeAndCurricularYearAndName(infoExecutionPeriod.getIdInternal(),
                            infoExecutionDegree.getInfoDegreeCurricularPlan().getIdInternal(),
                            curricularYearFromDB.getIdInternal(), "", infoExecutionPeriod.getSemester());

            Iterator iterator = listDCDE.iterator();
            listInfoDE = new ArrayList();
            while (iterator.hasNext()) {
                ExecutionCourse elem = (ExecutionCourse) iterator.next();

                listInfoDE.add(InfoExecutionCourse.newInfoFromDomain(elem));

            }
       
        return listInfoDE;
    }

}