/*
 * ExecutionCourseKeyAndLessonType.java
 *
 * Created on 04 de Novembro de 2002, 18:36
 */

package net.sourceforge.fenixedu.dataTransferObject;

/**
 * 
 * @author tfc130
 */
import net.sourceforge.fenixedu.domain.ShiftType;

public class ExecutionCourseKeyAndLessonType extends InfoObject {
    protected ShiftType _tipoAula;

    protected String _sigla;

    //FIXME: Add all uniques from ExecutionCourse or add'em to the services
    // that use this bean

    public ExecutionCourseKeyAndLessonType() {
    }

    public ExecutionCourseKeyAndLessonType(ShiftType tipoAula, String sigla) {
        setTipoAula(tipoAula);
        setSigla(sigla);
    }

    public ShiftType getTipoAula() {
        return _tipoAula;
    }

    public void setTipoAula(ShiftType tipoAula) {
        _tipoAula = tipoAula;
    }

    public String getSigla() {
        return _sigla;
    }

    public void setSigla(String sigla) {
        _sigla = sigla;
    }

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof ExecutionCourseKeyAndLessonType) {
            ExecutionCourseKeyAndLessonType tipoAulaAndKeyDisciplinaExecucao = (ExecutionCourseKeyAndLessonType) obj;

            resultado = (getTipoAula().equals(tipoAulaAndKeyDisciplinaExecucao.getTipoAula()))
                    && (getSigla().equals(tipoAulaAndKeyDisciplinaExecucao.getSigla()));
        }

        return resultado;
    }

    public String toString() {
        String result = "[TIPOAULAANDKEYDISCIPLINAEXECUCAO";
        result += ", tipo aula=" + _tipoAula;
        result += ", sigla =" + _sigla;
        result += "]";
        return result;
    }

}