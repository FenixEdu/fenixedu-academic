/*
 * Created on 30/Jul/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager.precedences;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.IPrecedence;
import net.sourceforge.fenixedu.domain.precedences.Precedence;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Tânia Pousão
 *  
 */
public class InsertSimplePrecedence implements IService {

    public InsertSimplePrecedence() {
    }

    public void run(String className, Integer curricularCourseToAddPrecedenceID,
            Integer precedentCurricularCourseID, Integer number) throws FenixServiceException {
        try {
            ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentCurricularCourse persistentCurricularCourse = persistentSuport.getIPersistentCurricularCourse();

			
            ICurricularCourse curricularCourseToAddPrecedence = (ICurricularCourse) persistentCurricularCourse
                    .readByOID(CurricularCourse.class, curricularCourseToAddPrecedenceID);
            if (curricularCourseToAddPrecedence == null) {
                throw new FenixServiceException("curricularCourseToAddPrecedence.NULL");
            }
			
			
            ICurricularCourse precedentCurricularCourse = null;
            if (precedentCurricularCourseID != null) {
                precedentCurricularCourse = (ICurricularCourse) persistentCurricularCourse.readByOID(
                        CurricularCourse.class, precedentCurricularCourseID);
                if (precedentCurricularCourse == null) {
                    throw new FenixServiceException("precedentCurricularCourse.NULL");
                }
            }
			
            IPrecedence precedence = new Precedence(curricularCourseToAddPrecedence, className, precedentCurricularCourse, number);

        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
        }
    }
}