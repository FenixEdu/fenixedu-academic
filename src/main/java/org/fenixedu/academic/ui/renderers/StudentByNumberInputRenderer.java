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
package net.sourceforge.fenixedu.presentationTier.renderers;

import net.sourceforge.fenixedu.domain.student.Student;
import pt.ist.fenixWebFramework.renderers.NumberInputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.converters.ConversionException;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.converters.IntegerNumberConverter;

public class StudentByNumberInputRenderer extends NumberInputRenderer {

    @Override
    public HtmlComponent render(Object targetObject, Class type) {
        if (targetObject == null) {
            return super.render(null, type);
        } else {
            Student student = (Student) targetObject;
            return super.render(student.getNumber(), type);
        }
    }

    @Override
    protected Converter getConverter() {
        return new StudentNumberConverter();
    }

    public static class StudentNumberConverter extends Converter {

        /**
         * Default Serial id.
         */
        private static final long serialVersionUID = 1L;

        @Override
        public Object convert(Class type, Object value) {
            try {
                IntegerNumberConverter converter = new IntegerNumberConverter();
                Integer studentNumber = (Integer) converter.convert(type, value);

                return Student.readStudentByNumber(studentNumber);
            } catch (ConversionException e) {
                return null;
            }
        }

    }
}
