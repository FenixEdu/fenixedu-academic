package ServidorPersistente;

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
    public IQualification readByYearAndSchoolAndPerson(
        Integer year,
        String school,
        IPessoa person)
        throws ExcepcaoPersistencia;
}
