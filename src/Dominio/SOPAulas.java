/*
 * SOPAulas.java
 *
 * Created on 1 de Setembro de 2002, 11:34
 */

package Dominio;

/**
 * 
 * @author ars
 */

import java.io.Serializable;

public class SOPAulas implements Serializable, Comparable {

    private int codigoInterno;

    private int numeroEpoca;

    private String turma;

    private int dia;

    private int hora;

    private String disciplina;

    private String tipoAula;

    private int numeroTurno;

    private String sala;

    private int fixa;

    /** Creates a new instance of SOPAulas */
    public SOPAulas() {
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

    public String getTurma() {
        return turma;
    }

    public int getDia() {
        return dia;
    }

    public int getHora() {
        return hora;
    }

    public String getDisciplina() {
        return disciplina;
    }

    public String getTipoAula() {
        return tipoAula;
    }

    public int getNumeroTurno() {
        return numeroTurno;
    }

    public String getSala() {
        return sala;
    }

    public int getFixa() {
        return fixa;
    }

    public void setNumeroEpoca(int numero) {
        numeroEpoca = numero;
    }

    public void setTurma(String tur) {
        turma = tur;
    }

    public void setDia(int d) {
        dia = d;
    }

    public void setHora(int h) {
        hora = h;
    }

    public void setDisciplina(String dis) {
        disciplina = dis;
    }

    public void setTipoAula(String tipo) {
        tipoAula = tipo;
    }

    public void setNumeroTurno(int numero) {
        numeroTurno = numero;
    }

    public void setSala(String sal) {
        sala = sal;
    }

    public void setFixa(int fix) {
        fixa = fix;
    }

    public int compareTo(Object o) {
        SOPAulas sa = (SOPAulas) o;
        if (dia < sa.getDia())
            return -1;
        else if (dia > sa.getDia())
            return 1;
        else if ((dia == sa.getDia()) && (hora < sa.getHora()))
            return -1;
        else
            return 0;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("");

        stringBuffer.append("[SOPLESSON=").append("hora=").append(this.hora).append(",tipoAula=")
                .append(this.tipoAula).append(",room=").append(this.sala).append(",numeroTurno=")
                .append(this.numeroTurno).append(",dia=").append(this.dia).append(",turma=").append(
                        this.turma).append(",disciplina=").append(this.disciplina).append("]");
        return stringBuffer.toString();

    }
}