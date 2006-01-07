package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.degree.DegreeType;

public interface ICursoPersistente extends IPersistentObject {

    public Degree readBySigla(String sigla) throws ExcepcaoPersistencia;

    public List<Degree> readAllFromOldDegreeStructure() throws ExcepcaoPersistencia;
    
    public List<Degree> readAllFromNewDegreeStructure() throws ExcepcaoPersistencia;

    public List<Degree> readAllByDegreeType(DegreeType degreeType) throws ExcepcaoPersistencia;

}
