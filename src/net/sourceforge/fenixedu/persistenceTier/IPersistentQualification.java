package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

/**
 * @author João Simas
 * @author Nuno Barbosa
 */
public interface IPersistentQualification extends IPersistentObject {
    public List readQualificationsByPersonId(Integer personId) throws ExcepcaoPersistencia;

}