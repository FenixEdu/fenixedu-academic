package ServidorAplicacao.Servico.publico;

import java.util.ArrayList;
import java.util.List;

import DataBeans.InfoDegree;
import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import Dominio.IExecutionDegree;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

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