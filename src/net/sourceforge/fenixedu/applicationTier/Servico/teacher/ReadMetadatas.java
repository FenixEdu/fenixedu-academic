/*
 * Created on 23/Jul/2003
 *  
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.ExecutionCourseSiteView;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoMetadata;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteMetadatas;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.dataTransferObject.comparators.QuestionDifficultyTypeComparatorByAscendingOrder;
import net.sourceforge.fenixedu.dataTransferObject.comparators.QuestionDifficultyTypeComparatorByDescendingOrder;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IMetadata;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentMetadata;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class ReadMetadatas implements IService {

    public ReadMetadatas() {
    }

    public SiteView run(Integer executionCourseId, String order, String asc, String path)
            throws FenixServiceException {

        try {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
            IPersistentExecutionCourse persistentExecutionCourse = persistentSuport
                    .getIPersistentExecutionCourse();
            IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                    ExecutionCourse.class, executionCourseId);
            if (executionCourse == null) {
                throw new InvalidArgumentsServiceException();
            }
            IPersistentMetadata persistentMetadata = persistentSuport.getIPersistentMetadata();
            List metadatas = new ArrayList();
            if (order == null
                    || !(order.equals("description") || order.equals("mainSubject")
                            || order.equals("difficulty") || order.equals("numberOfMembers")))
                order = new String("description");
            metadatas = persistentMetadata.readByExecutionCourseAndVisibilityAndOrder(executionCourse,
                    order, asc);
            List result = new ArrayList();
            Iterator iter = metadatas.iterator();
            while (iter.hasNext())
                result.add(InfoMetadata.newInfoFromDomain((IMetadata) iter.next()));
            if (order.equals("difficulty")) {
                if (asc != null && asc.equals("false"))
                    Collections.sort(result, new QuestionDifficultyTypeComparatorByDescendingOrder());
                else
                    Collections.sort(result, new QuestionDifficultyTypeComparatorByAscendingOrder());
            }
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