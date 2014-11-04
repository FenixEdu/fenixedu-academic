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
package net.sourceforge.fenixedu.domain.candidacyProcess.erasmus;

public enum StorkAttributeType {
    STORK_EIDENTIFIER("eIdentificador"),

    STORK_NAME("NomeProprio"), STORK_SURNAME("NomeApelido"), STORK_FAMILY_NAME("NomeFamilia"), STORK_ADOPTED_FAMILY_NAME(
            "NomeAdoptado"),

    STORK_GENDER("Sexo"), STORK_BIRTHDATE("DataNascimento"),

    STORK_COUNTRY_OF_BIRTH("Naturalidade"), STORK_NATIONALITY("Nacionalidade"),

    STORK_RESIDENCE_ADDRESS("Morada"), STORK_TEXT_RESIDENCE_ADDRESS("TextoMorada"), STORK_CANONICAL_ADDRESS("MoradaCanonica"),

    STORK_EMAIL("CorreioElectronico"), STORK_PHONE_CONTACT("Contactos"),

    STORK_RETURN_CODE("returnCode"), STORK_ERROR_CODE("errorCode"), STORK_ERROR_MESSAGE("errorMessage");

    private final String storkName;

    StorkAttributeType(String name) {
        this.storkName = name;
    }

    public String getStorkName() {
        return this.storkName;
    }

    public static StorkAttributeType getTypeFromStorkName(String value) {
        for (StorkAttributeType type : StorkAttributeType.values()) {
            if (value.equals(type.getStorkName())) {
                return type;
            }
        }

        return null;
    }

}
