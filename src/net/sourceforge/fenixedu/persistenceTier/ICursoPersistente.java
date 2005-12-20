package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.degree.DegreeType;

public interface ICursoPersistente extends IPersistentObject {

    public IDegree readBySigla(String sigla) throws ExcepcaoPersistencia;

    public List<IDegree> readAllFromOldDegreeStructure() throws ExcepcaoPersistencia;
    
    public List<IDegree> readAllFromNewDegreeStructure() throws ExcepcaoPersistencia;

    public List<IDegree> readAllByDegreeType(DegreeType degreeType) throws ExcepcaoPersistencia;

}
