package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ICursoPersistente;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

public class CursoVO extends VersionedObjectsBase implements ICursoPersistente {

    public IDegree readBySigla(String sigla) throws ExcepcaoPersistencia {
        Iterator degreesIterator = ((List) readAll(Degree.class)).iterator();
        while (degreesIterator.hasNext()) {
            IDegree degree = (IDegree) degreesIterator.next();

            if (degree.getSigla().equals(sigla))
                return degree;
        }
        return null;
    }

    public List<IDegree> readAllFromOldDegreeStructure() throws ExcepcaoPersistencia {
        List<IDegree> degrees = (List<IDegree>) readAll(Degree.class);

        return (List<IDegree>) CollectionUtils.select(degrees, new Predicate() {
            public boolean evaluate(Object o) {
                return ((IDegree) o).getBolonhaDegreeType() == null;
            }
        });
    }

    public List<IDegree> readAllFromNewDegreeStructure() throws ExcepcaoPersistencia {
        List<IDegree> degrees = (List<IDegree>) readAll(Degree.class);

        return (List<IDegree>) CollectionUtils.select(degrees, new Predicate() {
            public boolean evaluate(Object o) {
                return ((IDegree) o).getBolonhaDegreeType() != null;
            }
        });
    }

    public List<IDegree> readAllByDegreeType(final DegreeType degreeType) throws ExcepcaoPersistencia {
        List<IDegree> degrees = (List<IDegree>) readAll(Degree.class);

        return (List<IDegree>) CollectionUtils.select(degrees, new Predicate() {
            public boolean evaluate(Object o) {
                return ((IDegree) o).getTipoCurso().equals(degreeType);
            }
        });
    }

}
