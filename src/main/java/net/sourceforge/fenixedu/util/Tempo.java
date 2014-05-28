/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
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

    @Override
    public boolean equals(Object o) {
        return o instanceof Tempo && _hora == ((Tempo) o).hora() && _minutos == ((Tempo) o).minutos();
    }
}