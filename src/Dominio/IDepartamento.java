/*
 * IDepartamento.java
 *
 * Created on 6 de Novembro de 2002, 16:03
 */

package Dominio;

import java.util.Set;

/**
 *
 * @author  dcs-rjao
 */
public interface IDepartamento {
    public java.lang.String getNome();
    public java.lang.String getSigla();
    public Set getDisciplinasAssociadas();
    
    
    public void setSigla(java.lang.String sigla);
    public void setNome(java.lang.String nome);
    public void setDisciplinasAssociadas(java.util.Set disciplinasAssociadas);
}
