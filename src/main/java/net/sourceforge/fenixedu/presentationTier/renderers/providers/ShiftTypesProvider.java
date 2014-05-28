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
package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.oldInquiries.TeacherInquiryDTO;
import net.sourceforge.fenixedu.domain.ShiftType;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ShiftTypesProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        final TeacherInquiryDTO teacherInquiryDTO = (TeacherInquiryDTO) source;
        return teacherInquiryDTO.getExecutionCourse().getShiftTypes();
    }

    @Override
    public Converter getConverter() {
        return new Converter() {
            @Override
            public Object convert(Class type, Object value) {
                final List<ShiftType> shiftTypes = new ArrayList<ShiftType>();
                for (final String o : (String[]) value) {
                    shiftTypes.add(ShiftType.valueOf(o));
                }
                return shiftTypes;
            }
        };
    }

}
