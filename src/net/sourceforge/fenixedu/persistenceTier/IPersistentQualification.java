package net.sourceforge.fenixedu.persistenceTier;

import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IQualification;

/**
 * @author João Simas
 * @author Nuno Barbosa
 */
public interface IPersistentQualification extends IPersistentObject {
    public List readQualificationsByPerson(IPerson person) throws ExcepcaoPersistencia;

    public IQualification readByDateAndSchoolAndPerson(Date date, String school, IPerson person)
            throws ExcepcaoPersistencia;
}