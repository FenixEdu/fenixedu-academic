/*
 * IDocente.java
 *
 * Created on 26 de Novembro de 2002, 23:02
 */
package Dominio;
/**
 *
 * @author  EP15
 */
import java.util.List;
public interface ITeacher extends IPessoa{
    public Integer getTeacherNumber();
    public List getSitesOwned();
    public List getProfessorShipsSites();
//    public List getListaDeNomesDosSitiosResponsavel();
//    public List getListaDeNomesDosSitiosLecciona();
    public void setTeacherNumber(Integer number);
    public void setSitesOwned(List sites);
    public void setProfessorShipsSites(List sites);
}
