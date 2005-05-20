package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.IGuide;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.GuideState;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public interface IPersistentGuide extends IPersistentObject {

    /**
     * @param number
     * @param year
     * @return A List of the Guide Versions
     * @throws ExcepcaoPersistencia
     */
    public List readByNumberAndYear(Integer number, Integer year) throws ExcepcaoPersistencia;

    /**
     * @param number
     * @param year
     * @param version
     * @return IGuide
     * @throws ExcepcaoPersistencia
     */
    public IGuide readByNumberAndYearAndVersion(Integer number, Integer year, Integer version)
            throws ExcepcaoPersistencia;

    /**
     * @param year
     * @return Guide Number
     * @throws ExcepcaoPersistencia
     */
    public Integer generateGuideNumber(Integer year) throws ExcepcaoPersistencia;

    /**
     * @param year
     * @return A list with diferente Guides for this Year
     * @throws ExcepcaoPersistencia
     */
    public List readByYear(Integer year) throws ExcepcaoPersistencia;

    /**
     * @param identificationDocumentNumber
     * @param identificationDocumentType
     * @return A list with the diferent Guides for This Person
     * @throws ExcepcaoPersistencia
     */
    public List readByPerson(String identificationDocumentNumber,
            IDDocumentType identificationDocumentType) throws ExcepcaoPersistencia;

    /**
     * @param guideYear
     * @param situationOfGuide
     * @return
     */
    public List readByYearAndState(Integer guideYear, GuideState situationOfGuide)
            throws ExcepcaoPersistencia;

}