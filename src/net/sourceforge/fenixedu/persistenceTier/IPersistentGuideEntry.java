package net.sourceforge.fenixedu.persistenceTier;

import net.sourceforge.fenixedu.domain.DocumentType;
import net.sourceforge.fenixedu.domain.GraduationType;
import net.sourceforge.fenixedu.domain.IGuide;
import net.sourceforge.fenixedu.domain.IGuideEntry;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public interface IPersistentGuideEntry extends IPersistentObject {

	/**
	 * @param guideEntry
	 * @throws ExcepcaoPersistencia
	 */
	public void delete(IGuideEntry guideEntry) throws ExcepcaoPersistencia;

	/**
	 * @param guide
	 * @param graduationType
	 * @param documentType
	 * @param description
	 * @return IGuideEntry
	 * @throws ExcepcaoPersistencia
	 */
	public IGuideEntry readByGuideAndGraduationTypeAndDocumentTypeAndDescription(IGuide guide,
			GraduationType graduationType, DocumentType documentType, String description)
			throws ExcepcaoPersistencia;

}