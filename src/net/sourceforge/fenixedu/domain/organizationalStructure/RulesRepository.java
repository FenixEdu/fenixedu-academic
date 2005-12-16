/*
 * Created on Oct 4, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.domain.organizationalStructure;

import net.sourceforge.fenixedu.domain.IPerson;

public class RulesRepository {

// NOTA: ALGUNS NOMES NESTE FICHEIRO ESTÃO EM PORTUGUES E SÃO COMO TAL
// DEPENDENTES DO IST E SUA ESTRUTURA. MAS UMA VEZ QUE ESTA CLASSE É APENAS
// REPRESENTATIVA DA ESTRUTURA DO IST FAZ SENTIDO QUE ASSIM SEJA.
// É DE NOTAR QUE NUM FUTURO PROXIMO (?) ESTA CLASSE DEIXARÁ DE EXISTIR PARA
// DAR ORIGEM A UM MODELO DE REGRAS E EXCEPÇÕES APLICADO ÀS FUNÇÕES.
    
    public static Boolean isElegible(IPerson person, IUnit unit, String functionName) {
        if (unit.getName().equals("Conselho Directivo")) {
            return isElegibleConselhoDirectivo(person, functionName);
        }
        if (unit.getName().equals("Assembleia de Representantes")) {
            return isElegibleAssembleiaRepresentantes(person, functionName);
        }
//        if (unit.getName().equals("Comissão Executiva")) {
//            String parentUnitName = unit.getParentUnit().getName();
//            if (parentUnitName.equals("Conselho Científico")) {
//                return isElegibleConselhoCientifico(person, functionName);
//            }
//            if (parentUnitName.equals("Conselho Pedagógico")) {
//                return isElegibleConselhoPedagogico(person, functionName);
//            }
//        }
        if (unit.getName().equals("Conselho Administrativo")) {
            return isElegibleConselhoAdministrativo(person, functionName);
        }
        if (unit.getName().equals("Conselho Consultivo")) {
            return isElegibleConselhoConsultivo(person, functionName);
        }
        if (unit.getName().startsWith("Centro de Informática do IST (CIIST)")) {
            return isElegibleCIIST(person, functionName);
        }
        if (unit.getName().startsWith("Presidência do Departamento")) {
            return isElegibleDefaultDepartment(person, functionName);
        }
        if (unit.getName().startsWith("Conselho do Departamento")) {
            return isElegibleDefaultDepartment(person, functionName);
        }
        if (unit.getName().startsWith("Secção")) {
            return isElegibleDefaultSection(person, functionName);
        }
        if (unit.getName().equals("Associação de Estudantes (AEIST)")) {
            return isElegibleAEIST(person, functionName);
        }
        return true;
    }
    
    private static Boolean isElegibleConselhoDirectivo (IPerson person, String functionName) {
        // presidente
        if ( functionName.equals("Presidente do IST")) {
            return (isCatedraticTeacher(person) || isAssociatedTeacher(person));
        }
        return true;
    }
  
    private static Boolean isElegibleAssembleiaRepresentantes (IPerson person, String functionName) {
        if ( functionName.equals("Presidente") ) {
            return (isCatedraticTeacher(person) || isAssociatedTeacher(person));
        }
        if ( functionName.equals("Vice-Presidente") ) {
            //1 ESTUDANTE e 1 PROFESSOR
        }
        if ( functionName.equals("Secretário(a)") ) {
            return isEmployee(person);
        }
        if ( functionName.equals("Membro (Docente)")){ 
            return isTeacher(person);
        }
        if ( functionName.equals("Membro (Aluno)")){ 
            return isStudent(person);
        }
        if ( functionName.equals("Membro (Não Docente)")){ 
            return (isEmployee(person) && !isTeacher(person));
        }
        return true;
    }

//    private static Boolean isElegibleConselhoCientifico (IPerson person, String functionName) {
//        if (functionName.equals("Presidente-Adjunto para os assuntos científicos")) {
//            return (isCatedraticTeacher(person) || isAssociatedTeacher(person));
//        }
//        if (functionName.equals("Vice-Presidente")) {
//            return (isTeacher(person));
//        }
//        if (functionName.equals("Secretário(a)")) {
//            return (isEmployee(person));
//        }
//        return true;
//    }
//    
//    private static Boolean isElegibleConselhoPedagogico (IPerson person, String functionName) {
//        if (functionName.equals("Presidente-Adjunto para os assuntos pedagógicos")) {
//            return (isCatedraticTeacher(person) || isAssociatedTeacher(person));
//        }
//        if (functionName.equals("Vice-Presidente Aluno")) {
//            return isStudent(person);
//        }
//        if (functionName.equals("Vogal (Docente)")) {
//            return isTeacher(person);        
//        }
//        if (functionName.equals("Vogal (Aluno)")) {
//            return isStudent(person);
//        }
//        if (functionName.equals("Vogal (Assistente)")) {
//            return isAssistent(person);
//        }
//        return true;
//    }
 
    private static Boolean isElegibleConselhoAdministrativo (IPerson person, String functionName){
        if (functionName.equals("Presidente-Adjunto para os assuntos administrativos")) {
            return (isCatedraticTeacher(person) || isAssociatedTeacher(person));
        }
        if (functionName.equals("Director De Serviços De Recursos")) {
            return isEmployee(person);
        }
        if (functionName.equals("Chefe De Repartição De Recursos")) {
            return isEmployee(person);
        }
        if (functionName.equals("Vogal Docente")) {
            return isTeacher(person);
        }
        return true;
    }
 
    private static Boolean isElegibleConselhoConsultivo (IPerson person, String functionName){
        if (functionName.equals("Membro Não Docente")) {
            return !isTeacher(person);
        }
        return true;
    }
    
    private static Boolean isElegibleCIIST (IPerson person, String functionName) {
        if (functionName.equals("Presidente")) {
            return isTeacher(person);
        }
        return true;
    }
    
    private static Boolean isElegibleDefaultDepartment (IPerson person, String functionName) {
        if (functionName.equals("Presidente")) {
            return (isCatedraticTeacher(person) || isAssociatedTeacher(person));
        }
        if (functionName.equals("Docente")) {
            return isTeacher(person);
        }
        return true;
    }
    
    private static Boolean isElegibleDefaultSection (IPerson person, String functionName) {
        if (functionName.equals("Coordenador") || functionName.equals("Docente")) {
            return  isTeacher(person);
        }
        return true;
    }
    
    private static Boolean isElegibleAEIST (IPerson person, String functionName) {
            if (functionName.equals("Presidente")) {
                return isStudent(person);
            }
        return true;
    }
    
/************************************************************
 *                AUXILIAR METHODS
 ************************************************************/
    
    private static Boolean isCatedraticTeacher (IPerson person) {
        if (person.getTeacher() != null 
            && 
            person.getTeacher().getCategory().getLongName().equals("PROFESSOR CATEDRATICO")) {
                return true;
            }
        else {
            return false;
        }
    }
    
    private static Boolean isAssociatedTeacher (IPerson person) {
        if (person.getTeacher() != null 
            && 
            person.getTeacher().getCategory().getLongName().equals("PROFESSOR ASSOCIADO")) {
                return true;
            }
        else {
            return false;
        }
    }
    
    private static Boolean isStudent (IPerson person) {
        if(person.getStudentsCount() != 0) {
            return true;
        } 
        else {
            return false;
        }
    }
    
    private static Boolean isTeacher (IPerson person) {
        if(person.getTeacher() != null
                &&
           person.getTeacher().getCategory().getLongName().startsWith("PROFESSOR")) {
            return true;
        } 
        else {
            return false;
        }
    }
    
    private static Boolean isEmployee (IPerson person) {
        if(person.getEmployee() != null) {
            return true;
        } 
        else {
            return false;
        }
    }
    
//    private static Boolean isAssistent (IPerson person) {
//        if(person.getTeacher() != null 
//                &&
//           person.getTeacher().getCategory().getLongName().contains("ASSISTENTE")) {
//            return true;
//        } 
//        else {
//            return false;
//        }
//    }
}

