/*
 * ISitio.java
 *
 * Created on 24 de Agosto de 2002, 23:49
 */

package Dominio;

/**
 *
 * @author  ars
 */

import java.util.List;

public interface ISitio {
  String getNome();
  int getAnoCurricular();
  int getSemestre();
  String getCurso();
  String getDepartamento();
  List getSeccoes();
  ISeccao getSeccaoInicial();

  void setNome(String nome);
  void setAnoCurricular(int anoCurricular);
  void setSemestre(int semestre);
  void setCurso(String curso);
  void setDepartamento(String departamento);
  void setSeccoes(List seccoes);
  void setSeccaoInicial(ISeccao seccaoInicial);
}
