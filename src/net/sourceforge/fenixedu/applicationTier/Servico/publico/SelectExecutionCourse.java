package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;

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

            //CLONER
            //IExecutionDegree executionDegree = Cloner
            //        .copyInfoExecutionDegree2ExecutionDegree(infoExecutionDegree);
            //IExecutionPeriod executionPeriod = Cloner
            //        .copyInfoExecutionPeriod2IExecutionPeriod(infoExecutionPeriod);

            IExecutionDegree executionDegree = InfoExecutionDegree.newDomainFromInfo(infoExecutionDegree);
            if (executionDegree != null) {
                executionDegree.setCurricularPlan(InfoDegreeCurricularPlan
                        .newDomainFromInfo(infoExecutionDegree.getInfoDegreeCurricularPlan()));
                executionDegree.getCurricularPlan().setDegree(
                        InfoDegree.newDomainFromInfo(infoExecutionDegree.getInfoDegreeCurricularPlan()
                                .getInfoDegree()));
            }
            IExecutionPeriod executionPeriod = InfoExecutionPeriod
                    .newDomainFromInfo(infoExecutionPeriod);

            executionCourseList = executionCourseDAO
                    .readByCurricularYearAndExecutionPeriodAndExecutionDegree(curricularYear,
                            executionPeriod, executionDegree);

            for (int i = 0; i < executionCourseList.size(); i++) {
                IExecutionCourse executionCourse = (IExecutionCourse) executionCourseList.get(i);

                //CLONER
                //InfoExecutionCourse infoExecutionCourse =
                // (InfoExecutionCourse) Cloner.get(executionCourse);
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