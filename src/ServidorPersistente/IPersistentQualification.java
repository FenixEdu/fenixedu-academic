package ServidorPersistente;

import java.util.Date;
import java.util.List;

import Dominio.IPerson;
import Dominio.IQualification;

/**
 * @author João Simas
 * @author Nuno Barbosa
 */
public interface IPersistentQualification extends IPersistentObject {
    public List readQualificationsByPerson(IPerson person) throws ExcepcaoPersistencia;

    public IQualification readByDateAndSchoolAndPerson(Date date, String school, IPerson person)
            throws ExcepcaoPersistencia;
}