/*
 * Curriculum.java
 *
 * Created on 6 de Janeiro de 2003, 20:29
 */
package net.sourceforge.fenixedu.domain;

import java.util.Calendar;

import net.sourceforge.fenixedu.util.MultiLanguageString;

/**
 * 
 * @author EP 15 - fjgc
 * @author Jo�o Mota
 */
public class Curriculum extends Curriculum_Base {

    public Curriculum() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

    public void edit(String generalObjectives, String operacionalObjectives, String program,
            String generalObjectivesEn, String operacionalObjectivesEn, String programEn,
            String language, Person person) {

        if (language == null) {

            this.setGeneralObjectives(generalObjectives);
            this.setOperacionalObjectives(operacionalObjectives);
            this.setProgram(program);

        } else {

            this.setGeneralObjectivesEn(generalObjectivesEn);
            this.setOperacionalObjectivesEn(operacionalObjectivesEn);
            this.setProgramEn(programEn);
        }

        this.setPersonWhoAltered(person);
        Calendar today = Calendar.getInstance();
        this.setLastModificationDate(today.getTime());
    }

    public void delete() {
        removeCurricularCourse();
        removePersonWhoAltered();
        removeRootDomainObject();
        
        deleteDomainObject();
    }

    public MultiLanguageString getGeneralObjectivesI18N() {
        final MultiLanguageString multiLanguageString = new MultiLanguageString();
        if (getGeneralObjectives() != null && getGeneralObjectives().length() > 0) {
            multiLanguageString.setContent(Language.pt, getGeneralObjectives());
        }
        if (getGeneralObjectivesEn() != null && getGeneralObjectivesEn().length() > 0) {
            multiLanguageString.setContent(Language.en, getGeneralObjectivesEn());
        }
        return multiLanguageString;
    }

    public MultiLanguageString getOperacionalObjectivesI18N() {
        final MultiLanguageString multiLanguageString = new MultiLanguageString();
        if (getOperacionalObjectives() != null && getOperacionalObjectives().length() > 0) {
            multiLanguageString.setContent(Language.pt, getOperacionalObjectives());
        }
        if (getOperacionalObjectivesEn() != null && getOperacionalObjectivesEn().length() > 0) {
            multiLanguageString.setContent(Language.en, getOperacionalObjectivesEn());
        }
        return multiLanguageString;
    }

    public MultiLanguageString getProgramI18N() {
        final MultiLanguageString multiLanguageString = new MultiLanguageString();
        if (getProgram() != null && getProgram().length() > 0) {
            multiLanguageString.setContent(Language.pt, getProgram());
        }
        if (getProgramEn() != null && getProgramEn().length() > 0) {
            multiLanguageString.setContent(Language.en, getProgramEn());
        }
        return multiLanguageString;
    }
    
}
