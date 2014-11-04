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
package net.sourceforge.fenixedu.domain.exceptions;

/**
 * This exception signals a point in the domain logic were a certain field was
 * required but was not available.
 * 
 * @author cfgi
 */
public class FieldIsRequiredException extends DomainException {

    private String field;

    protected FieldIsRequiredException(String key, String... args) {
        super(key, args);
    }

    public FieldIsRequiredException(String field, String key) {
        this(field, key, new String[] { field });
    }

    public FieldIsRequiredException(String field, String key, String... args) {
        this(key, args);

        setField(field);
    }

    /**
     * @return the name of the field that was required
     */
    public String getField() {
        return this.field;
    }

    protected void setField(String field) {
        this.field = field;
    }

}
