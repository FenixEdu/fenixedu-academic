/*
 * Created on 17/Jul/2003 To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.IEnrollment;
import net.sourceforge.fenixedu.domain.IEnrolmentEquivalence;
import net.sourceforge.fenixedu.domain.IEquivalentEnrolmentForEnrolmentEquivalence;

/**
 * @author dcs-rjao 17/Jul/2003
 */
public interface IPersistentEquivalentEnrolmentForEnrolmentEquivalence extends IPersistentObject {

    public void delete(IEquivalentEnrolmentForEnrolmentEquivalence enrolmentEquivalenceRestriction)
            throws ExcepcaoPersistencia;

    public IEquivalentEnrolmentForEnrolmentEquivalence readByEnrolmentEquivalenceAndEquivalentEnrolment(
            IEnrolmentEquivalence enrolmentEquivalence, IEnrollment equivalentEnrolment)
            throws ExcepcaoPersistencia;

    public List readAll() throws ExcepcaoPersistencia;

    public List readByEquivalentEnrolment(IEnrollment equivalentEnrolment) throws ExcepcaoPersistencia;

    public List readByEnrolmentEquivalence(IEnrolmentEquivalence enrolmentEquivalence)
            throws ExcepcaoPersistencia;
}