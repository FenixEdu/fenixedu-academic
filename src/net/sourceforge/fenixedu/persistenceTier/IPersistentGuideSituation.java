package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.IGuide;
import net.sourceforge.fenixedu.domain.IGuideSituation;
import net.sourceforge.fenixedu.util.SituationOfGuide;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public interface IPersistentGuideSituation extends IPersistentObject {

    /**
     * @param guide
     * @return List
     * @throws ExcepcaoPersistencia
     */
    public List readByGuide(IGuide guide) throws ExcepcaoPersistencia;

    /**
     * @param guide
     * @return IGuideSituation
     * @throws ExcepcaoPersistencia
     */
    public IGuideSituation readGuideActiveSituation(IGuide guide) throws ExcepcaoPersistencia;

    /**
     * @param guide
     * @param guideSituation
     * @return IGuideSituation
     * @throws ExcepcaoPersistencia
     */
    public IGuideSituation readByGuideAndSituation(IGuide guide, SituationOfGuide guideSituation)
            throws ExcepcaoPersistencia;

}