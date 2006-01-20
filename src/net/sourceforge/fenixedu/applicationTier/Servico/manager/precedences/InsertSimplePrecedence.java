/*
 * Created on 30/Jul/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager.precedences;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourse;

/**
 * @author Tânia Pousão
 *  
 */
public class InsertSimplePrecedence extends Service {

    public void run(String className, Integer curricularCourseToAddPrecedenceID,
            Integer precedentCurricularCourseID, Integer number) throws FenixServiceException, ExcepcaoPersistencia {

        IPersistentCurricularCourse persistentCurricularCourse = persistentSupport
                .getIPersistentCurricularCourse();

        CurricularCourse curricularCourseToAddPrecedence = (CurricularCourse) persistentCurricularCourse
                .readByOID(CurricularCourse.class, curricularCourseToAddPrecedenceID);
        if (curricularCourseToAddPrecedence == null) {
            throw new FenixServiceException("curricularCourseToAddPrecedence.NULL");
        }

        CurricularCourse precedentCurricularCourse = null;
        if (precedentCurricularCourseID != null) {
            precedentCurricularCourse = (CurricularCourse) persistentCurricularCourse.readByOID(
                    CurricularCourse.class, precedentCurricularCourseID);
            if (precedentCurricularCourse == null) {
                throw new FenixServiceException("precedentCurricularCourse.NULL");
            }
        }

        DomainFactory.makePrecedence(curricularCourseToAddPrecedence, className,
                precedentCurricularCourse, number);
    }

}