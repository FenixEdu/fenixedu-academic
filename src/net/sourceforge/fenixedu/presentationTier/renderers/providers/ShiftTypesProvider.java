package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.inquiries.TeacherInquiryDTO;
import net.sourceforge.fenixedu.domain.ShiftType;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ShiftTypesProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	final TeacherInquiryDTO teacherInquiryDTO = (TeacherInquiryDTO) source;
	return teacherInquiryDTO.getExecutionCourse().getShiftTypes();
    }

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
