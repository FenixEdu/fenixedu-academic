package ServidorPersistente;

import java.util.Date;
import java.util.List;

import Dominio.IPessoa;
import Dominio.IQualification;

/**
 * @author João Simas
 * @author Nuno Barbosa
 */
public interface IPersistentQualification extends IPersistentObject
{
    public List readQualificationsByPerson(IPessoa person) throws ExcepcaoPersistencia;
    public IQualification readByDateAndSchoolAndPerson(
        Date date,
        String school,
        IPessoa person)
        throws ExcepcaoPersistencia;
}
