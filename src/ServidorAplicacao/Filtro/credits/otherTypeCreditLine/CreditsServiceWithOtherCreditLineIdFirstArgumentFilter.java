/*
 * Created on 1/Mar/2004
 */
package ServidorAplicacao.Filtro.credits.otherTypeCreditLine;

import Dominio.credits.IOtherTypeCreditLine;
import Dominio.credits.OtherTypeCreditLine;
import ServidorAplicacao.Filtro.credits.AbstractTeacherDepartmentAuthorization;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.credits.IPersistentOtherTypeCreditLine;

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
    protected Integer getTeacherId(Object[] arguments, ISuportePersistente sp)
            throws FenixServiceException {
        Integer id = (Integer) arguments[0];

        IPersistentOtherTypeCreditLine otherTypeCreditLineDAO = sp
                .getIPersistentOtherTypeCreditLine();

        IOtherTypeCreditLine otherTypeCreditLine;
        try {
            otherTypeCreditLine = (IOtherTypeCreditLine) otherTypeCreditLineDAO
                    .readByOID(OtherTypeCreditLine.class, id);
        } catch (ExcepcaoPersistencia e) {
            return null;
        }
        return otherTypeCreditLine == null ? null : otherTypeCreditLine
                .getTeacher().getIdInternal();
    }

}