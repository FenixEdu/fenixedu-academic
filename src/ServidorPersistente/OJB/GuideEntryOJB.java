package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.GuideEntry;
import Dominio.IGuide;
import Dominio.IGuideEntry;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentGuideEntry;
import Util.DocumentType;
import Util.GraduationType;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class GuideEntryOJB extends ObjectFenixOJB implements IPersistentGuideEntry
{

    public GuideEntryOJB()
    {
    }

    public void delete(IGuideEntry guideEntry) throws ExcepcaoPersistencia
    {
        super.delete(guideEntry);
    }

    public List readByGuide(IGuide guide) throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        crit.addEqualTo("guide.number", guide.getNumber());
        crit.addEqualTo("guide.year", guide.getYear());
        return queryList(GuideEntry.class, crit);

    }

    public IGuideEntry readByGuideAndGraduationTypeAndDocumentTypeAndDescription(IGuide guide,
            GraduationType graduationType, DocumentType documentType, String description)
            throws ExcepcaoPersistencia
    {

        Criteria crit = new Criteria();
        crit.addEqualTo("guide.number", guide.getNumber());
        crit.addEqualTo("guide.year", guide.getYear());

        crit.addEqualTo("guide.version", guide.getVersion());
        crit.addEqualTo("graduationType", graduationType.getType());
        crit.addEqualTo("documentType", documentType.getType());
        crit.addEqualTo("description", description);
        return (IGuideEntry) queryObject(GuideEntry.class, crit);

    }

}