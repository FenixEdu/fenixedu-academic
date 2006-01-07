package net.sourceforge.fenixedu.persistenceTier;

import net.sourceforge.fenixedu.domain.DocumentType;
import net.sourceforge.fenixedu.domain.GraduationType;
import net.sourceforge.fenixedu.domain.Guide;
import net.sourceforge.fenixedu.domain.GuideEntry;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public interface IPersistentGuideEntry extends IPersistentObject {

	/**
	 * @param guideEntry
	 * @throws ExcepcaoPersistencia
	 */
	public void delete(GuideEntry guideEntry) throws ExcepcaoPersistencia;

	/**
	 * @param guide
	 * @param graduationType
	 * @param documentType
	 * @param description
	 * @return IGuideEntry
	 * @throws ExcepcaoPersistencia
	 */
	public GuideEntry readByGuideAndGraduationTypeAndDocumentTypeAndDescription(Guide guide,
			GraduationType graduationType, DocumentType documentType, String description)
			throws ExcepcaoPersistencia;

}