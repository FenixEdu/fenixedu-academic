/*
 * Created on 13/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoSiteOldPublications;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.teacher.IOldPublication;
import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentOldPublication;
import net.sourceforge.fenixedu.util.OldPublicationType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class ReadOldPublications implements IServico {
    private static ReadOldPublications service = new ReadOldPublications();

    /**
     *  
     */
    private ReadOldPublications() {

    }

    public static ReadOldPublications getService() {

        return service;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.IServico#getNome()
     */
    public String getNome() {
        return "ReadOldPublications";
    }

    public SiteView run(OldPublicationType oldPublicationType, String user) throws FenixServiceException {
        try {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();

            IPersistentTeacher persistentTeacher = persistentSuport.getIPersistentTeacher();
            ITeacher teacher = persistentTeacher.readTeacherByUsername(user);
            InfoTeacher infoTeacher = Cloner.copyITeacher2InfoTeacher(teacher);

            IPersistentOldPublication persistentOldPublication = persistentSuport
                    .getIPersistentOldPublication();
            List publications = persistentOldPublication.readAllByTeacherAndOldPublicationType(teacher,
                    oldPublicationType);

            List result = (List) CollectionUtils.collect(publications, new Transformer() {
                public Object transform(Object o) {
                    IOldPublication oldPublication = (IOldPublication) o;
                    return Cloner.copyIOldPublication2InfoOldPublication(oldPublication);
                }
            });

            InfoSiteOldPublications bodyComponent = new InfoSiteOldPublications();
            bodyComponent.setInfoOldPublications(result);
            bodyComponent.setOldPublicationType(oldPublicationType);
            bodyComponent.setInfoTeacher(infoTeacher);

            SiteView siteView = new SiteView(bodyComponent);
            return siteView;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }
}