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
    public IQualification readByYearAndSchoolAndDegreeAndPerson(
        Integer year,
        String school,
        String degree,
        IPessoa person)
        throws ExcepcaoPersistencia;
}
