package net.sourceforge.fenixedu.util;

public class Tempo extends FenixUtil {
    private int _hora;

    private int _minutos;

    private int _segundos;

    public Tempo(int hora, int minutos) {
        _hora = hora;
        _minutos = minutos;
        _segundos = 0;
    }

    public Tempo(int hora, int minutos, int segundos) {
        _hora = hora;
        _minutos = minutos;
        _segundos = segundos;
    }

    /* Selectores */

    public int hora() {
        return _hora;
    }

    public int minutos() {
        return _minutos;
    }

    public int segundos() {
        return _segundos;
    }

    /* Modificadores */

    public void hora(int hora) {
        _hora = hora;
    }

    public void minutos(int minutos) {
        _minutos = minutos;
    }

    public void segundos(int segundos) {
        _segundos = segundos;
    }

    /* Comparador */

    public boolean equals(Object o) {
        return o instanceof Tempo && _hora == ((Tempo) o).hora() && _minutos == ((Tempo) o).minutos();
    }
}