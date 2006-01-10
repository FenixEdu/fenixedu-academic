package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ICursoPersistente;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

public class CursoVO extends VersionedObjectsBase implements ICursoPersistente {

    public Degree readBySigla(String sigla) throws ExcepcaoPersistencia {
        Iterator degreesIterator = ((List) readAll(Degree.class)).iterator();
        while (degreesIterator.hasNext()) {
            Degree degree = (Degree) degreesIterator.next();

            if (degree.getSigla().equals(sigla))
                return degree;
        }
        return null;
    }

    public List<Degree> readAllFromOldDegreeStructure() throws ExcepcaoPersistencia {
        List<Degree> degrees = (List<Degree>) readAll(Degree.class);

        return (List<Degree>) CollectionUtils.select(degrees, new Predicate() {
            public boolean evaluate(Object o) {
                return ((Degree) o).getBolonhaDegreeType() == null;
            }
        });
    }

    public List<Degree> readAllFromNewDegreeStructure() throws ExcepcaoPersistencia {
        List<Degree> degrees = (List<Degree>) readAll(Degree.class);

        return (List<Degree>) CollectionUtils.select(degrees, new Predicate() {
            public boolean evaluate(Object o) {
                return ((Degree) o).getBolonhaDegreeType() != null;
            }
        });
    }

    public List<Degree> readAllByDegreeType(final DegreeType degreeType) throws ExcepcaoPersistencia {
        List<Degree> degrees = (List<Degree>) readAll(Degree.class);

        return (List<Degree>) CollectionUtils.select(degrees, new Predicate() {
            public boolean evaluate(Object o) {
                return ((Degree) o).getTipoCurso().equals(degreeType);
            }
        });
    }

}
