package Dominio;

public class DepartamentoLicenciatura {
  private final int _chaveDepartamento;
  private final int _chaveLicenciatura;

  /* Construtores */

  public DepartamentoLicenciatura(int chaveDepartamento, int chaveLicenciatura) {
    _chaveDepartamento = chaveDepartamento;
    _chaveLicenciatura = chaveLicenciatura;
  }

  /* Selectores */

  public int chaveDepartamento() {
    return _chaveDepartamento;
  }

  public int chaveLicenciatura() {
    return _chaveLicenciatura;
  }
}