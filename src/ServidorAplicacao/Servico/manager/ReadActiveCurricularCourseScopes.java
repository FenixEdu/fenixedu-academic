/*
 * Created on 19/Ago/2003
 */
package ServidorAplicacao.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.util.Cloner;
import Dominio.CurricularCourse;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Fernanda Quitério 28/10/2003
 *  
 */
public class ReadActiveCurricularCourseScopes implements IServico {

    private static ReadActiveCurricularCourseScopes service = new ReadActiveCurricularCourseScopes();

    /**
     * The singleton access method of this class.
     */
    public static ReadActiveCurricularCourseScopes getService() {
        return service;
    }

    /**
     * The constructor of this class.
     */
    private ReadActiveCurricularCourseScopes() {
    }

    /**
     * Service name
     */
    public final String getNome() {
        return "ReadActiveCurricularCourseScopes";
    }

    /**
     * Executes the service. Returns the collection of active
     * infoCurricularCourseScopes.
     */
    public List run(Integer curricularCourseId) throws FenixServiceException {
        ISuportePersistente sp;
        List allCurricularCourseScopes = null;
        try {

            sp = SuportePersistenteOJB.getInstance();
            ICurricularCourse curricularCourse = (ICurricularCourse) sp
                    .getIPersistentCurricularCourse().readByOID(
                            CurricularCourse.class, curricularCourseId);
            allCurricularCourseScopes = sp
                    .getIPersistentCurricularCourseScope()
                    .readActiveCurricularCourseScopesByCurricularCourse(
                            curricularCourse);

        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }

        if (allCurricularCourseScopes == null
                || allCurricularCourseScopes.isEmpty())
            return allCurricularCourseScopes;

        Iterator iterator = allCurricularCourseScopes.iterator();
        List result = new ArrayList(allCurricularCourseScopes.size());

        while (iterator.hasNext())
            result
                    .add(Cloner
                            .copyICurricularCourseScope2InfoCurricularCourseScope((ICurricularCourseScope) iterator
                                    .next()));

        return result;
    }
}