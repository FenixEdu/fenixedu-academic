package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

/**
 * @author João Mota
 */
public class ReadExecutionCoursesByDegreeAndExecutionPeriod implements IServico {

    private static ReadExecutionCoursesByDegreeAndExecutionPeriod _servico = new ReadExecutionCoursesByDegreeAndExecutionPeriod();

    /**
     * The actor of this class.
     */

    private ReadExecutionCoursesByDegreeAndExecutionPeriod() {

    }

    /**
     * Returns Service Name
     */
    public String getNome() {
        return "ReadExecutionCoursesByDegreeAndExecutionPeriod";
    }

    /**
     * Returns the _servico.
     * 
     * @return ReadExecutionCoursesByDegreeAndExecutionPeriod
     */
    public static ReadExecutionCoursesByDegreeAndExecutionPeriod getService() {
        return _servico;
    }

    public Object run(InfoExecutionDegree infoExecutionDegree, InfoExecutionPeriod infoExecutionPeriod)
            throws FenixServiceException {

        List infoExecutionCourseList = new ArrayList();

        try {

            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            IPersistentExecutionCourse executionCourseDAO = sp.getIPersistentExecutionCourse();

            IExecutionDegree executionDegree = Cloner
                    .copyInfoExecutionDegree2ExecutionDegree(infoExecutionDegree);
            IExecutionPeriod executionPeriod = Cloner
                    .copyInfoExecutionPeriod2IExecutionPeriod(infoExecutionPeriod);
            List executionCourseList = new ArrayList();
            List temp = null;
            for (int i = 1; i < 6; i++) {
                temp = executionCourseDAO.readByCurricularYearAndExecutionPeriodAndExecutionDegree(
                        new Integer(i), executionPeriod, executionDegree);
                executionCourseList.addAll(temp);
            }

            for (int i = 0; i < executionCourseList.size(); i++) {
                IExecutionCourse aux = (IExecutionCourse) executionCourseList.get(i);
                InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) Cloner.get(aux);
                infoExecutionCourseList.add(infoExecutionCourse);
            }

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

        return infoExecutionCourseList;

    }

}