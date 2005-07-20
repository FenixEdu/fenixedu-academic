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
import net.sourceforge.fenixedu.domain.ICurricularYear;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
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

            ICurricularYear curricularYearFromDB = (ICurricularYear) sp.getIPersistentCurricularYear()
                    .readByOID(CurricularYear.class, curricularYear);

            listDCDE = executionCourseDAO
                    .readByExecutionPeriodAndExecutionDegreeAndCurricularYearAndName(infoExecutionPeriod.getIdInternal(),
                            infoExecutionDegree.getInfoDegreeCurricularPlan().getIdInternal(),
                            curricularYearFromDB.getIdInternal(), "");

            Iterator iterator = listDCDE.iterator();
            listInfoDE = new ArrayList();
            while (iterator.hasNext()) {
                IExecutionCourse elem = (IExecutionCourse) iterator.next();

                listInfoDE.add(InfoExecutionCourse.newInfoFromDomain(elem));

            }
       
        return listInfoDE;
    }

}