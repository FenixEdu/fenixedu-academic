package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.Person;

public interface IPessoaPersistente extends IPersistentObject {

    public Integer countAllPersonByName(String name) throws ExcepcaoPersistencia;

    public List<Person> readPersonsBySubName(String subName) throws ExcepcaoPersistencia;
    
    public List<Person> findPersonByName(String name, Integer startIndex, Integer numberOfElementsInSpan) throws ExcepcaoPersistencia;
}