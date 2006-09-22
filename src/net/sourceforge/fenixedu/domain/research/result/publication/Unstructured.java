package net.sourceforge.fenixedu.domain.research.result.publication;

import bibtex.dom.BibtexEntry;

/**
 * This publication is used to maintain old Unstructured Publications
 * Is used only to migrate to new publications
 * Field used: title and year
 */
public class Unstructured extends Unstructured_Base {

    public Unstructured() {
        super();
    }

    @Override
    public BibtexEntry exportToBibtexEntry() {
        return null;
    }

    @Override
    public String getResume() {
        String resume = getParticipationsAndTitleString();
        if ((getYear() != null) && (getYear() > 0))
            resume = resume + getYear();

        resume = finishResume(resume);
        return resume;
    }

}
