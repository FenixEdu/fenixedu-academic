/*
 * Created on 28/Mai/2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier;

import net.sourceforge.fenixedu.domain.IStudentGroupAttend;

/**
 * @author asnr and scpo
 * 
 */
public interface IPersistentStudentGroupAttend extends IPersistentObject {

    public IStudentGroupAttend readByStudentGroupAndAttend(Integer studentGroupID, Integer attendID)
            throws ExcepcaoPersistencia;
}
