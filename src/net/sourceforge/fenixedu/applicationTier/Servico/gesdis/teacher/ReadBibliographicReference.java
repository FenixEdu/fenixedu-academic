/*
 * Created on 17/Mar/2003
 *
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.gesdis.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoBibliographicReference;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IBibliographicReference;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentBibliographicReference;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.exceptions.ExistingPersistentException;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author PTRLV
 * 
 *  
 */
public class ReadBibliographicReference implements IService {

    public List run(InfoExecutionCourse infoExecutionCourse, Boolean optional)
            throws FenixServiceException {
        List references = null;
        List infoBibRefs = null;
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentExecutionYear persistentExecutionYear = sp.getIPersistentExecutionYear();
            IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();
            IPersistentBibliographicReference persistentBibliographicReference = persistentBibliographicReference = sp
                    .getIPersistentBibliographicReference();
            IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();

            IExecutionYear executionYear = persistentExecutionYear
                    .readExecutionYearByName(infoExecutionCourse.getInfoExecutionPeriod()
                            .getInfoExecutionYear().getYear());
            IExecutionPeriod executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear(
                    infoExecutionCourse.getInfoExecutionPeriod().getName(), executionYear);
            IExecutionCourse executionCourse = persistentExecutionCourse
                    .readByExecutionCourseInitialsAndExecutionPeriod(infoExecutionCourse.getSigla(),
                            executionPeriod);
            references = persistentBibliographicReference.readBibliographicReference(executionCourse);

            Iterator iterator = references.iterator();
            infoBibRefs = new ArrayList();
            while (iterator.hasNext()) {
                IBibliographicReference bibRef = (IBibliographicReference) iterator.next();

                if (optional != null) {
                    if (bibRef.getOptional().equals(optional)) {
                        InfoBibliographicReference infoBibRef = Cloner
                                .copyIBibliographicReference2InfoBibliographicReference(bibRef);
                        infoBibRefs.add(infoBibRef);
                    }
                } else {
                    InfoBibliographicReference infoBibRef = Cloner
                            .copyIBibliographicReference2InfoBibliographicReference(bibRef);
                    infoBibRefs.add(infoBibRef);
                }
            }

        } catch (ExistingPersistentException e) {
            throw new ExistingServiceException(e);
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
        return infoBibRefs;
    }
}