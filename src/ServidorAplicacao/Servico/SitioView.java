package ServidorAplicacao.Servico;


import java.util.List;

/**
 * This is the view class that contains information about the sitio
 * domain objects.
 *
 * @author Joao Pereira 
 **/

public class SitioView {
  private String _nome;
  private int _anoCurricular;
  private int _semestre;
  private String _departamento;
  private String _curso;
  private List _seccoes;
  private String _seccaoInicial;

  /**
   * @param seccoes is a list with the names of the seccoes of the
   * sitio. This list can be null if the site has no seccoes.
   **/
  public SitioView(String nome, int anoCurricular, int semestre,
		   String curso, String departamento, List seccoes) {
    this(nome, anoCurricular, semestre, departamento, curso, seccoes, null);
  }

  /**
   * @param seccoes is a list with the names of the seccoes of the
   * sitio. This list can be null if the site has no seccoes.
   **/
  public SitioView(String nome, int anoCurricular, int semestre,
		   String curso, String departamento, List seccoes,
                   String seccaoInicial) {
    _nome = nome;
    _anoCurricular = anoCurricular;
    _semestre = semestre;
    _departamento = departamento;
    _curso = curso;
    _seccoes = seccoes;
    _seccaoInicial = seccaoInicial;
  }

  public String getNome() {
    return _nome;
  }
  
  public int getAnoCurricular() {
    return _anoCurricular;
  }
  
  public int getSemestre() {
    return _semestre;
  }
  
  public String getCurso() {
    return _curso;
  }
  
  public String getDepartamento() {
    return _departamento;
  }
  
  /**
   * @returns the list of seccoes of the sitio. This list can be null
   * if the sitio has no seccoes.
   **/
  public List getSeccoes() {
    return _seccoes;
  }

  public String getSeccaoInicial() {
    return _seccaoInicial;
  }
}
