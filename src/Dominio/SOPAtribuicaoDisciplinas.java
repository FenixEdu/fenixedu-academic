/*
 * SOPAtribuicaoDisciplinas.java
 *
 * Created on 4 de Setembro de 2002, 13:44
 */

package Dominio;

/**
 * 
 * @author ars
 */
public class SOPAtribuicaoDisciplinas implements java.io.Serializable {

    private int codigoInterno;

    private int numeroEpoca;

    private String turma;

    private String disciplina;

    private String tipoAula;

    private int numeroDivisao;

    private int horasMaximas;

    /** Creates a new instance of SOPAtribuicaoDisciplinas */
    public SOPAtribuicaoDisciplinas() {
    }

    public int getCodigoInterno() {
        return codigoInterno;
    }

    public void setCodigoInterno(int codigo) {
        codigoInterno = codigo;
    }

    public int getNumeroEpoca() {
        return numeroEpoca;
    }

    public void setNumeroEpoca(int numero) {
        numeroEpoca = numero;
    }

    public String getTurma() {
        return turma;
    }

    public void setTurma(String tur) {
        turma = tur;
    }

    public String getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(String dis) {
        disciplina = dis;
    }

    public String getTipoAula() {
        return tipoAula;
    }

    public void setTipoAula(String tipo) {
        tipoAula = tipo;
    }

    public int getNumeroDivisao() {
        return numeroDivisao;
    }

    public void setNumeroDivisao(int numero) {
        numeroDivisao = numero;
    }

    public int getHorasMaximas() {
        return horasMaximas;
    }

    public void setHorasMaximas(int horas) {
        horasMaximas = horas;
    }

}