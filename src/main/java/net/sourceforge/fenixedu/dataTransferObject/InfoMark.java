/*
 * InfoStudent.java
 *
 * Created on 13 de Dezembro de 2002, 16:04
 */

package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.Mark;

/**
 * 
 * @author tfc130
 */

public class InfoMark extends InfoObject {

    private final Mark markDomainReference;

    public InfoMark(final Mark mark) {
        markDomainReference = mark;
    }

    public static InfoMark newInfoFromDomain(Mark mark) {
        return mark == null ? null : new InfoMark(mark);
    }

    private Mark getMarkObject() {
        return markDomainReference;
    }

    @Override
    public String getExternalId() {
        return getMarkObject().getExternalId();
    }

    @Override
    public void setExternalId(String integer) {
        throw new Error("Method should not be called!");
    }

    /**
     * @return
     */
    public String getMark() {
        return getMarkObject().getMark();
    }

    /**
     * @return
     */
    public String getPublishedMark() {
        return getMarkObject().getPublishedMark();
    }

    /**
     * @return
     */
    public InfoEvaluation getInfoEvaluation() {
        return InfoEvaluation.newInfoFromDomain(getMarkObject().getEvaluation());
    }

    /**
     * @return
     */
    public InfoFrequenta getInfoFrequenta() {
        return InfoFrequenta.newInfoFromDomain(getMarkObject().getAttend());
    }

    @Override
    public String toString() {
        return getMarkObject().toString();
    }

}
