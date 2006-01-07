package net.sourceforge.fenixedu.persistenceTier.OJB;

import net.sourceforge.fenixedu.domain.DocumentType;
import net.sourceforge.fenixedu.domain.GraduationType;
import net.sourceforge.fenixedu.domain.GuideEntry;
import net.sourceforge.fenixedu.domain.Guide;
import net.sourceforge.fenixedu.domain.GuideEntry;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGuideEntry;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class GuideEntryOJB extends PersistentObjectOJB implements IPersistentGuideEntry {

	public void delete(GuideEntry guideEntry) throws ExcepcaoPersistencia {
		super.delete(guideEntry);
	}

	public GuideEntry readByGuideAndGraduationTypeAndDocumentTypeAndDescription(Guide guide,
			GraduationType graduationType, DocumentType documentType, String description)
			throws ExcepcaoPersistencia {

		Criteria crit = new Criteria();
		crit.addEqualTo("guide.number", guide.getNumber());
		crit.addEqualTo("guide.year", guide.getYear());

		crit.addEqualTo("guide.version", guide.getVersion());
		crit.addEqualTo("graduationType", graduationType.name());
		crit.addEqualTo("documentType", documentType.name());
		crit.addEqualTo("description", description);
		return (GuideEntry) queryObject(GuideEntry.class, crit);

	}

}