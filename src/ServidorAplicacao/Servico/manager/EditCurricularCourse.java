/*
 * Created on 18/Ago/2003
 */
package ServidorAplicacao.Servico.manager;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoCurricularCourse;
import Dominio.CurricularCourse;
import Dominio.ICurricularCourse;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author lmac1
 */
public class EditCurricularCourse implements IService {

    public EditCurricularCourse() {
    }

    public void run(InfoCurricularCourse newInfoCurricularCourse) throws FenixServiceException {

        IPersistentCurricularCourse persistentCurricularCourse = null;
        ICurricularCourse oldCurricularCourse = null;
        String newName = null;
        String newCode = null;

        try {

            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
            persistentCurricularCourse = persistentSuport.getIPersistentCurricularCourse();
            oldCurricularCourse = (ICurricularCourse) persistentCurricularCourse.readByOID(
                    CurricularCourse.class, newInfoCurricularCourse.getIdInternal(), true);

            newName = newInfoCurricularCourse.getName();
            newCode = newInfoCurricularCourse.getCode();

            if (oldCurricularCourse == null) {
                throw new NonExistingServiceException();
            }
            oldCurricularCourse.setName(newName);
            oldCurricularCourse.setCode(newCode);
            oldCurricularCourse.setAcronym(newInfoCurricularCourse.getAcronym());
            oldCurricularCourse.setType(newInfoCurricularCourse.getType());
            oldCurricularCourse.setMandatory(newInfoCurricularCourse.getMandatory());
            oldCurricularCourse.setBasic(newInfoCurricularCourse.getBasic());
            oldCurricularCourse.setTheoreticalHours(newInfoCurricularCourse.getTheoreticalHours());
            oldCurricularCourse.setTheoPratHours(newInfoCurricularCourse.getTheoPratHours());
            oldCurricularCourse.setPraticalHours(newInfoCurricularCourse.getPraticalHours());
            oldCurricularCourse.setLabHours(newInfoCurricularCourse.getLabHours());
            oldCurricularCourse.setMaximumValueForAcumulatedEnrollments(newInfoCurricularCourse
                    .getMaximumValueForAcumulatedEnrollments());
            oldCurricularCourse.setMinimumValueForAcumulatedEnrollments(newInfoCurricularCourse
                    .getMinimumValueForAcumulatedEnrollments());
            oldCurricularCourse.setCredits(newInfoCurricularCourse.getCredits());
            oldCurricularCourse.setEctsCredits(newInfoCurricularCourse.getEctsCredits());
            oldCurricularCourse.setWeigth(newInfoCurricularCourse.getWeigth());
            oldCurricularCourse.setEnrollmentWeigth(newInfoCurricularCourse.getEnrollmentWeigth());
            oldCurricularCourse.setMandatoryEnrollment(newInfoCurricularCourse.getMandatoryEnrollment());
            oldCurricularCourse.setEnrollmentAllowed(newInfoCurricularCourse.getEnrollmentAllowed());

        } catch (ExistingPersistentException ex) {
            throw new ExistingServiceException(ex);
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }
    }
}