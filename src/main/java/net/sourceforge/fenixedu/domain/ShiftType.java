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
package net.sourceforge.fenixedu.domain;

public enum ShiftType {

    TEORICA,

    PRATICA,

    TEORICO_PRATICA,

    LABORATORIAL,

    DUVIDAS,

    RESERVA,

    SEMINARY,

    PROBLEMS,

    FIELD_WORK,

    TRAINING_PERIOD,

    TUTORIAL_ORIENTATION;

    public String getSiglaTipoAula() {
        String value = this.name();
        if (value == ShiftType.TEORICA.name()) {
            return "T";
        }
        if (value == ShiftType.PRATICA.name()) {
            return "P";
        }
        if (value == ShiftType.TEORICO_PRATICA.name()) {
            return "TP";
        }
        if (value == ShiftType.LABORATORIAL.name()) {
            return "L";
        }
        if (value == ShiftType.DUVIDAS.name()) {
            return "D";
        }
        if (value == ShiftType.RESERVA.name()) {
            return "R";
        }
        if (value == ShiftType.SEMINARY.name()) {
            return "S";
        }
        if (value == ShiftType.PROBLEMS.name()) {
            return "PB";
        }
        if (value == ShiftType.FIELD_WORK.name()) {
            return "TC";
        }
        if (value == ShiftType.TRAINING_PERIOD.name()) {
            return "E";
        }
        if (value == ShiftType.TUTORIAL_ORIENTATION.name()) {
            return "OT";
        }
        return "Error: Invalid lesson type";
    }

    public String getFullNameTipoAula() {
        String value = this.name();
        if (value == ShiftType.TEORICA.name()) {
            return "Teórica";
        }
        if (value == ShiftType.PRATICA.name()) {
            return "Prática";
        }
        if (value == ShiftType.TEORICO_PRATICA.name()) {
            return "TeoricoPrática";
        }
        if (value == ShiftType.LABORATORIAL.name()) {
            return "Laboratorial";
        }
        if (value == ShiftType.DUVIDAS.name()) {
            return "Dúvidas";
        }
        if (value == ShiftType.RESERVA.name()) {
            return "Reserva";
        }
        if (value == ShiftType.SEMINARY.name()) {
            return "Seminário";
        }
        if (value == ShiftType.PROBLEMS.name()) {
            return "Problemas";
        }
        if (value == ShiftType.FIELD_WORK.name()) {
            return "Trabalho de Campo";
        }
        if (value == ShiftType.TRAINING_PERIOD.name()) {
            return "Estágio";
        }
        if (value == ShiftType.TUTORIAL_ORIENTATION.name()) {
            return "Orientação Tutorial";
        }
        return "Error: Invalid lesson type";
    }

    public String getName() {
        return name();
    }

}
