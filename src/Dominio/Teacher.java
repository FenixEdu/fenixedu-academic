/*
 * Docente.java
 *
 * Created on 26 de Novembro de 2002, 23:22
 */
package Dominio;
/**
 *
 * @author  EP15
 */
//import java.util.ArrayList;
//import java.util.Iterator;
import java.util.List;
public class Teacher extends Pessoa implements ITeacher {
    private Integer teacherNumber;
    private List professorShipsSites;
    private List sitesOwned;
    /** Creates a new instance of Teacher */
    public Teacher() {
    }
    public Teacher(String user, String password, Integer teacherNumber){
		super.setUsername(user);
		super.setPassword(password);
        setTeacherNumber(teacherNumber);
        setProfessorShipsSites(null);
        setSitesOwned(null);
    }
    public Integer getTeacherNumber() {
        return teacherNumber;
    }
    public List getSitesOwned() {
        return sitesOwned;
    }
    public List getProfessorShipsSites(){
        return professorShipsSites;
    }
//    public List getListaDeNomesDosSitiosResponsavel(){
//
//        List resultado = new ArrayList();
//        
//        if(sitesOwned!=null){
//
//            Iterator iterador = sitesOwned.iterator();
//
//            while (iterador.hasNext())
//                resultado.add(((ISitio)iterador.next()).getNome());
//            return resultado;
//        }
//        return resultado;
//    }
//    public List getListaDeNomesDosSitiosLecciona(){
//
//        List resultado = new ArrayList();
//
//        if(professorShipsSites!=null){
//
//            Iterator iterador = professorShipsSites.iterator();
//
//            while (iterador.hasNext())
//
//                resultado.add(((ISitio)iterador.next()).getNome());
//
//            return resultado;
//
//        }
//
//        return resultado;
//    }
    public void setTeacherNumber(Integer teacherNumber) {
        this.teacherNumber = teacherNumber;
    }
    public void setSitesOwned(List sites) {
        sitesOwned = sites;
    }
    public void setProfessorShipsSites(List sites){
        professorShipsSites = sites;
    }
    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof ITeacher) {
            resultado = getTeacherNumber().equals(((ITeacher) obj).getTeacherNumber());
        }
        return resultado;
    }
    public int hashCode() {
        return teacherNumber.hashCode();
    }
}
