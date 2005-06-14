package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

/**
 * @author João Mota
 */
public class SelectExecutionCourse implements IServico {

    private static SelectExecutionCourse _servico = new SelectExecutionCourse();

    /**
     * The actor of this class.
     */

    private SelectExecutionCourse() {

    }

    /**
     * Returns Service Name
     */
    public String getNome() {
        return "SelectExecutionCourse";
    }

    /**
     * Returns the _servico.
     * 
     * @return SelectExecutionCourse
     */
    public static SelectExecutionCourse getService() {
        return _servico;
    }

    public Object run(InfoExecutionDegree infoExecutionDegree, InfoExecutionPeriod infoExecutionPeriod,
            Integer curricularYear) {

        List infoExecutionCourseList = new ArrayList();

        try {
            List executionCourseList = null;
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            IPersistentExecutionCourse executionCourseDAO = sp.getIPersistentExecutionCourse();

           // IExecutionDegree executionDegree = InfoExecutionDegree.newDomainFromInfo(infoExecutionDegree);
            
            if (infoExecutionDegree != null) {
                infoExecutionDegree.setInfoDegreeCurricularPlan(infoExecutionDegree.getInfoDegreeCurricularPlan());
                infoExecutionDegree.getInfoDegreeCurricularPlan().setInfoDegree(
                        infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree());
            }
           
//            IExecutionPeriod executionPeriod = InfoExecutionPeriod
//                    .newDomainFromInfo(infoExecutionPeriod);

            executionCourseList = executionCourseDAO
                    .readByCurricularYearAndExecutionPeriodAndExecutionDegree(curricularYear,
                            infoExecutionPeriod.getSemester(),
                            infoExecutionDegree.getInfoDegreeCurricularPlan().getName(),
                            infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getSigla(),
                            infoExecutionPeriod.getIdInternal());

            for (int i = 0; i < executionCourseList.size(); i++) {
                IExecutionCourse executionCourse = (IExecutionCourse) executionCourseList.get(i);

                InfoExecutionCourse infoExecutionCourse = InfoExecutionCourse
                        .newInfoFromDomain(executionCourse);
                infoExecutionCourseList.add(infoExecutionCourse);
            }

        } catch (ExcepcaoPersistencia e) {

            e.printStackTrace();
        }

        return infoExecutionCourseList;

    }

}