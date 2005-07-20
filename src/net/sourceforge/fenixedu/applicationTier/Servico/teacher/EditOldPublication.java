/*
 * Created on 13/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.EditDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoOldPublication;
import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.teacher.IOldPublication;
import net.sourceforge.fenixedu.domain.teacher.OldPublication;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class EditOldPublication extends EditDomainObjectService {

    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) {
        return sp.getIPersistentOldPublication();
    }

    @Override
    protected void copyInformationFromInfoToDomain(ISuportePersistente sp, InfoObject infoObject,
            IDomainObject domainObject) throws ExcepcaoPersistencia {
        InfoOldPublication infoOldPublication = (InfoOldPublication) infoObject;
        IOldPublication oldPublication = (OldPublication) domainObject;

        oldPublication.setLastModificationDate(infoOldPublication.getLastModificationDate());
        oldPublication.setOldPublicationType(infoOldPublication.getOldPublicationType());
        oldPublication.setPublication(infoOldPublication.getPublication());

        IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
        ITeacher teacher = persistentTeacher.readByNumber(infoOldPublication.getInfoTeacher()
                .getTeacherNumber());
        oldPublication.setTeacher(teacher);
        oldPublication.setKeyTeacher(teacher.getIdInternal());
    }

    @Override
    protected IDomainObject createNewDomainObject(InfoObject infoObject) {
        return null;
    }

    @Override
    protected Class getDomainObjectClass() {
        return null;
    }

}
