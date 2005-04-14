/*
 * Curriculum.java
 *
 * Created on 6 de Janeiro de 2003, 20:29
 */
package net.sourceforge.fenixedu.domain;

/**
 * 
 * @author EP 15 - fjgc
 * @author João Mota
 */
public class Curriculum extends Curriculum_Base {

    /** Creates a new instance of Curriculum */
    public Curriculum() {
    }

    public Curriculum(ICurricularCourse curricularCourse) {
        setCurricularCourse(curricularCourse);
    }

    public Curriculum(ICurricularCourse curricularCourse, String generalObjectives,
            String operacionalObjectives, String program) {
        setGeneralObjectives(generalObjectives);
        setOperacionalObjectives(operacionalObjectives);
        setProgram(program);
        setCurricularCourse(curricularCourse);
    }

    public Curriculum(ICurricularCourse curricularCourse, String generalObjectives,
            String operacionalObjectives, String generalObjectivesEn, String operacionalObjectivesEn) {
        setGeneralObjectives(generalObjectives);
        setOperacionalObjectives(operacionalObjectives);
        setGeneralObjectivesEn(generalObjectivesEn);
        setOperacionalObjectivesEn(operacionalObjectivesEn);
        setCurricularCourse(curricularCourse);
    }

    public Curriculum(ICurricularCourse curricularCourse, String program, String programEn) {
        setProgram(program);
        setProgramEn(programEn);
        setCurricularCourse(curricularCourse);
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof ICurriculum) {
            ICurriculum curriculum = (ICurriculum) obj;

            result = ((getCurricularCourse() == null && curriculum.getCurricularCourse() == null) || (getCurricularCourse() != null
                    && curriculum.getCurricularCourse() != null && getCurricularCourse().equals(
                    curriculum.getCurricularCourse())));
        }
        return result;
    }

    public String toString() {
        String result = "[CURRICULUM";
        result += "codigo interno" + getIdInternal();
        result += "Objectivos Operacionais" + getOperacionalObjectives();
        result += "Objectivos gerais" + getGeneralObjectives();
        result += "programa" + getProgram();
        result += "Objectivos Operacionais em Inglês" + getOperacionalObjectivesEn();
        result += "Objectivos gerais em Inglês" + getGeneralObjectivesEn();
        result += "programa em Inglês" + getProgramEn();
        result += "curricular Course" + getCurricularCourse();
        result += "]";
        return result;
    }
}