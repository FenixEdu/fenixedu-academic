/*
 * Created on 23/Jul/2003
 *
 */

package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.ExecutionCourseSiteView;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoMetadata;
import DataBeans.InfoSiteMetadatas;
import DataBeans.SiteView;
import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.IMetadata;
import Dominio.ITest;
import Dominio.Test;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Susana Fernandes
 */

public class ReadMetadatasByTest implements IService {

    public ReadMetadatasByTest() {
    }

    public SiteView run(Integer executionCourseId, Integer testId, String order, String asc)
            throws FenixServiceException {
        List result = new ArrayList();
        try {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
            IPersistentExecutionCourse persistentExecutionCourse = persistentSuport
                    .getIPersistentExecutionCourse();

            IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                    ExecutionCourse.class, executionCourseId);
            if (executionCourse == null)
                throw new InvalidArgumentsServiceException();

            ITest test = (ITest) persistentSuport.getIPersistentTest().readByOID(Test.class, testId);

            if (test == null) {
                throw new InvalidArgumentsServiceException();
            }

            if (order == null
                    || !(order.equals("description") || order.equals("mainSubject")
                            || order.equals("difficulty") || order.equals("numberOfMembers")))
                order = new String("description");

            List metadatasList = persistentSuport.getIPersistentMetadata()
                    .readByExecutionCourseAndNotTest(executionCourse, test, order, asc);
            Iterator iter = metadatasList.iterator();
            while (iter.hasNext())
                result.add(InfoMetadata.newInfoFromDomain((IMetadata) iter.next()));

            InfoSiteMetadatas bodyComponent = new InfoSiteMetadatas();
            bodyComponent.setInfoMetadatas(result);
            bodyComponent.setExecutionCourse(InfoExecutionCourse.newInfoFromDomain(executionCourse));
            SiteView siteView = new ExecutionCourseSiteView(bodyComponent, bodyComponent);
            return siteView;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }
}