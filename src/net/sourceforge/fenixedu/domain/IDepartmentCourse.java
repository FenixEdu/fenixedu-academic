/*
 * IDepartmentCourse.java
 *
 * Created on 6 de Novembro de 2002, 16:03
 */

package net.sourceforge.fenixedu.domain;

/**
 * 
 * @author dcs-rjao
 */
public interface IDepartmentCourse extends IDomainObject {
    public java.lang.String getNome();

    public java.lang.String getSigla();

    public IDepartment getDepartamento();

    public void setNome(java.lang.String nome);

    public void setSigla(java.lang.String sigla);

    public void setDepartamento(IDepartment departamento);
}