package ServidorPersistente.OJB;
import java.util.List;
import org.apache.ojb.broker.query.Criteria;
import Dominio.BibliographicReference;
import Dominio.IBibliographicReference;
import Dominio.IExecutionCourse;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentBibliographicReference;
/**
 * @author EP 15
 */
public class BibliographicReferenceOJB
    extends ObjectFenixOJB
    implements IPersistentBibliographicReference
{
    public void lockWrite(IBibliographicReference bibliographicReference) throws ExcepcaoPersistencia
    {
        super.lockWrite(bibliographicReference);
    }
    public void delete(IBibliographicReference bibliographicReference) throws ExcepcaoPersistencia
    {
        super.delete(bibliographicReference);
    }
   
    public IBibliographicReference readBibliographicReference(
        IExecutionCourse executionCourse,
        String title,
        String authors,
        String reference,
        String year)
        throws ExcepcaoPersistencia
    {

        Criteria crit = new Criteria();
        crit.addEqualTo("executionCourse.sigla", executionCourse.getSigla());
        crit.addEqualTo(
            "executionCourse.executionPeriod.name",
            executionCourse.getExecutionPeriod().getName());
        crit.addEqualTo(
            "executionCourse.executionPeriod.executionYear.year",
            executionCourse.getExecutionPeriod().getExecutionYear().getYear());
        crit.addEqualTo("title", title);
        crit.addEqualTo("authors", authors);
        crit.addEqualTo("reference", reference);
        crit.addEqualTo("year", year);
        return (IBibliographicReference) queryObject(BibliographicReference.class, crit);

    }
    public List readBibliographicReference(IExecutionCourse executionCourse) throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        crit.addEqualTo("executionCourse.idInternal", executionCourse.getIdInternal());
        List result = queryList(BibliographicReference.class, crit);
        return result;

    }
}
