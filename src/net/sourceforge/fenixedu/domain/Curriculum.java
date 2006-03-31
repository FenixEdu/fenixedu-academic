/*
 * Curriculum.java
 *
 * Created on 6 de Janeiro de 2003, 20:29
 */
package net.sourceforge.fenixedu.domain;

import java.util.Calendar;

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
}
