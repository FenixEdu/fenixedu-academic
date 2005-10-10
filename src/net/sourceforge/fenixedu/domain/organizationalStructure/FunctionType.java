/*
 * Created on Oct 3, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.domain.organizationalStructure;

public enum FunctionType {
    
    PRESIDENTE,
    PRESIDENTE_ADJUNTO,
    VICE_PRESIDENTE,
    SECRETARIO,
    CHEFE,
    PROFESSOR_VOGAL,
    DIRECTOR,
    COORDENADOR,
    FUNCIONARIO,
    FUNCIONARIO_NAO_DOCENTE,
    DOCENTE,
    ALUNO,
    REPRESENTANTE,
    VOGAL;
    
    public String getName() {
        return name();
    }    
}
