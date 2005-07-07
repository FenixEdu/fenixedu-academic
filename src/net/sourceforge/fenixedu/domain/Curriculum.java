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

    public String toString() {
        String result = "[CURRICULUM";
        result += "codigo interno" + this.getIdInternal();
        result += "Objectivos Operacionais" + this.getOperacionalObjectives();
        result += "Objectivos gerais" + this.getGeneralObjectives();
        result += "programa" + this.getProgram();
        result += "Objectivos Operacionais em Inglês" + this.getOperacionalObjectivesEn();
        result += "Objectivos gerais em Inglês" + this.getGeneralObjectivesEn();
        result += "programa em Inglês" + this.getProgramEn();
        result += "curricular Course" + this.getCurricularCourse();
        result += "]";
        return result;
    }

    public boolean equals(Object obj) {
        if (obj instanceof ICurriculum) {
            final ICurriculum curriculum = (ICurriculum) obj;
            return this.getIdInternal().equals(curriculum.getIdInternal());
        }
        return false;
    }

}
