package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.GuideEntry;
import Dominio.IGuide;
import Dominio.IGuideEntry;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentGuideEntry;
import ServidorPersistente.exceptions.ExistingPersistentException;
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

    public void write(IGuideEntry guideEntryToWrite)
        throws ExcepcaoPersistencia, ExistingPersistentException
    {
        IGuideEntry guideEntryBD = null;
        if (guideEntryToWrite == null)
            // Should we throw an exception saying nothing to write or
            // something of the sort?
            // By default, if OJB received a null object it would complain.
            return;

        // read Guide Entry

        guideEntryBD =
            this.readByGuideAndGraduationTypeAndDocumentTypeAndDescription(
                guideEntryToWrite.getGuide(),
                guideEntryToWrite.getGraduationType(),
                guideEntryToWrite.getDocumentType(),
                guideEntryToWrite.getDescription());

        // if (guideEntry not in database) then write it
        if (guideEntryBD == null)
        {
            super.lockWrite(guideEntryToWrite);
            return;

        }
        // else if (guideEntry is mapped to the database then write any
		// existing changes)
        else if (
            (guideEntryToWrite != null)
                && ((GuideEntry) guideEntryBD).getIdInternal().equals(
                    ((GuideEntry) guideEntryToWrite).getIdInternal()))
        {

            guideEntryBD.setDescription(guideEntryToWrite.getDescription());
            guideEntryBD.setDocumentType(guideEntryToWrite.getDocumentType());
            guideEntryBD.setGraduationType(guideEntryToWrite.getGraduationType());
            guideEntryBD.setPrice(guideEntryToWrite.getPrice());
            guideEntryBD.setQuantity(guideEntryToWrite.getQuantity());

            // No need to werite it because it is already mapped.
            //super.lockWrite(lessonToWrite);
            // else throw an AlreadyExists exception.
        }
        else
        {
            throw new ExistingPersistentException();
        }

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

    public IGuideEntry readByGuideAndGraduationTypeAndDocumentTypeAndDescription(
        IGuide guide,
        GraduationType graduationType,
        DocumentType documentType,
        String description)
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
