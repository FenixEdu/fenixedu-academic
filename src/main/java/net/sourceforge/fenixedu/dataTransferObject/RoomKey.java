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
/*
 * KeysSala.java
 *
 * Created on 31 de Outubro de 2002, 12:27
 */

package net.sourceforge.fenixedu.dataTransferObject;

/**
 * 
 * @author tfc130
 */
public class RoomKey extends InfoObject {
    protected String _nomeSala;

    public RoomKey() {
    }

    public RoomKey(String nomeSala) {
        setNomeSala(nomeSala);
    }

    public String getNomeSala() {
        return _nomeSala;
    }

    public void setNomeSala(String nomeSala) {
        _nomeSala = nomeSala;
    }

    @Override
    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof RoomKey) {
            RoomKey keySala = (RoomKey) obj;

            resultado = (getNomeSala().equals(keySala.getNomeSala()));
        }

        return resultado;
    }

    @Override
    public String toString() {
        String result = "[KEYSALA";
        result += ", sala=" + _nomeSala;
        result += "]";
        return result;
    }

}