package Util;

public class Data {
  private int _dia;
  private int _mes;
  private int _ano;

  /* Construtores */

  public Data(int dia, int mes, int ano) {
    dia(dia);
    mes(mes);
    ano(ano);
  }

  /* Selectores */

  public int dia() {
    return _dia;
  }

  public int mes() {
    return _mes;
  }

  public int ano() {
    return _ano;
  }

  /* Modificadores */

  public void dia(int dia) {
    _dia = dia;
  }

  public void mes(int mes) {
    _mes = mes;
  }

  public void ano(int ano) {
    _ano = ano;
  }

  /* Comparador */

  public boolean equals(Object o) {
    return o instanceof Data &&
           _dia == ((Data)o).dia() &&
           _mes == ((Data)o).mes() &&
           _ano == ((Data)o).ano();
  }
}