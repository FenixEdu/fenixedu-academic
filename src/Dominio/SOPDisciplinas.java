/*
 * SOPDisciplinas.java
 *
 * Created on 1 de Setembro de 2002, 20:43
 */

package Dominio;

/**
 * 
 * @author ars
 */
public class SOPDisciplinas implements java.io.Serializable {

    private int codigoInterno;

    private String sigla;

    private String nome;

    /** Creates a new instance of SOPDisciplinas */
    public SOPDisciplinas() {
    }

    public int getCodigoInterno() {
        return codigoInterno;
    }

    public void setCodigoInterno(int codigo) {
        codigoInterno = codigo;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String s) {
        sigla = s;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String n) {
        nome = n;
    }

}