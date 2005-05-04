package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.IPrice;
import net.sourceforge.fenixedu.domain.DocumentType;
import net.sourceforge.fenixedu.util.GraduationType;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public interface IPersistentPrice extends IPersistentObject {

    /**
     * 
     * @return List
     * @throws ExcepcaoPersistencia
     */
    public List readAll() throws ExcepcaoPersistencia;

    /**
     * 
     * @param graduationType
     * @return List
     * @throws ExcepcaoPersistencia
     */
    public List readByGraduationType(GraduationType graduationType) throws ExcepcaoPersistencia;

    /**
     * 
     * @param graduationType
     * @param documentType
     * @param description
     * @return IPrice
     * @throws ExcepcaoPersistencia
     */
    public IPrice readByGraduationTypeAndDocumentTypeAndDescription(GraduationType graduationType,
            DocumentType documentType, String description) throws ExcepcaoPersistencia;

    /**
     * 
     * @param graduationType
     * @param documentType
     * @return List
     * @throws ExcepcaoPersistencia
     */
    public List readByGraduationTypeAndDocumentType(GraduationType graduationType,
            DocumentType documentType) throws ExcepcaoPersistencia;

    /**
     * 
     * @param graduationType
     * @param types
     * @return List
     * @throws ExcepcaoPersistencia
     */
    public List readByGraduationTypeAndDocumentType(GraduationType graduationType, List types)
            throws ExcepcaoPersistencia;
}