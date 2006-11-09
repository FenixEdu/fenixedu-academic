/*
 * Curriculum.java
 *
 * Created on 6 de Janeiro de 2003, 20:29
 */
package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.joda.time.DateTime;

/**
 * 
 * @author EP 15 - fjgc
 * @author Jo�o Mota
 */
public class Curriculum extends Curriculum_Base {

    public Curriculum() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
    	final IUserView userView = AccessControl.getUserView();
        this.setPersonWhoAltered(userView.getPerson());
	}

    public void edit(String generalObjectives, String operacionalObjectives, String program,
            String generalObjectivesEn, String operacionalObjectivesEn, String programEn) {

    	this.setGeneralObjectives(generalObjectives);
    	this.setOperacionalObjectives(operacionalObjectives);
    	this.setProgram(program);
    	this.setGeneralObjectivesEn(generalObjectivesEn);
    	this.setOperacionalObjectivesEn(operacionalObjectivesEn);
    	this.setProgramEn(programEn);

    	final IUserView userView = AccessControl.getUserView();
        this.setPersonWhoAltered(userView.getPerson());
        this.setLastModificationDateDateTime(new DateTime());
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
