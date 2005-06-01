/*
 * Created on Apr 27, 2004
 *
 */
package net.sourceforge.fenixedu.persistenceTier.gratuity.masterDegree;

import java.util.List;

import net.sourceforge.fenixedu.domain.gratuity.SibsPaymentType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 */
public interface IPersistentSibsPaymentFileEntry extends IPersistentObject {

	public List readByYearAndStudentNumberAndPaymentType(Integer year, Integer studentNumber,
			SibsPaymentType paymentType) throws ExcepcaoPersistencia;

	public List readNonProcessed() throws ExcepcaoPersistencia;
}