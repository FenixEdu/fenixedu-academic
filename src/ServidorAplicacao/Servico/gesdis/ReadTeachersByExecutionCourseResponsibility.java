/*
 * Created on 25/Mar/2003
 *
 * 
 */
package ServidorAplicacao.Servico.gesdis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoTeacher;
import DataBeans.util.Cloner;
import Dominio.IExecutionCourse;
import Dominio.IResponsibleFor;
import Dominio.ITeacher;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentResponsibleFor;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author João Mota
 * 
 *  
 */
public class ReadTeachersByExecutionCourseResponsibility implements IServico {
    private static ReadTeachersByExecutionCourseResponsibility service = new ReadTeachersByExecutionCourseResponsibility();

    /**
     * The singleton access method of this class.
     */
    public static ReadTeachersByExecutionCourseResponsibility getService() {
        return service;
    }

    /**
     *  
     */
    public ReadTeachersByExecutionCourseResponsibility() {

    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.IServico#getNome()
     */
    public String getNome() {

        return "ReadTeachersByExecutionCourseResponsibility";
    }

    public List run(InfoExecutionCourse infoExecutionCourse)
            throws FenixServiceException {
        try {
            List result = null;
            ISuportePersistente sp;
            sp = SuportePersistenteOJB.getInstance();
            IExecutionCourse executionCourse = Cloner
                    .copyInfoExecutionCourse2ExecutionCourse(infoExecutionCourse);
            IPersistentResponsibleFor persistentResponsibleFor = sp
                    .getIPersistentResponsibleFor();
            result = persistentResponsibleFor
                    .readByExecutionCourse(executionCourse);

            List infoResult = new ArrayList();
            if (result != null) {
                Iterator iter = result.iterator();
                while (iter.hasNext()) {
                    IResponsibleFor responsibleFor = (IResponsibleFor) iter
                            .next();
                    ITeacher teacher = responsibleFor.getTeacher();
                    InfoTeacher infoTeacher = Cloner
                            .copyITeacher2InfoTeacher(teacher);
                    infoResult.add(infoTeacher);
                }
                return infoResult;
            }

            return result;

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

    }
}