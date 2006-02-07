/*
 * Created on 1/Mar/2004
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.credits.otherTypeCreditLine;

import net.sourceforge.fenixedu.applicationTier.Filtro.credits.AbstractTeacherDepartmentAuthorization;
import net.sourceforge.fenixedu.domain.credits.OtherTypeCreditLine;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author jpvl
 */
public class CreditsServiceWithOtherCreditLineIdFirstArgumentFilter extends
        AbstractTeacherDepartmentAuthorization {

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Filtro.credits.AbstractTeacherDepartmentAuthorization#getTeacherId(java.lang.Object[],
     *      ServidorPersistente.ISuportePersistente)
     */
    protected Integer getTeacherId(Object[] arguments) {
        Integer id = (Integer) arguments[0];

        OtherTypeCreditLine otherTypeCreditLine;
        try {
            otherTypeCreditLine = (OtherTypeCreditLine) persistentObject.readByOID(
                    OtherTypeCreditLine.class, id);
        } catch (ExcepcaoPersistencia e) {
            return null;
        }
        return otherTypeCreditLine == null ? null : otherTypeCreditLine.getTeacher().getIdInternal();
    }

}