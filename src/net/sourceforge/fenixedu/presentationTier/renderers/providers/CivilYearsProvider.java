package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.alumni.formation.AlumniFormation;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

import org.joda.time.DateTime;

public class CivilYearsProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {

	AlumniFormation formation = (AlumniFormation) source;
	int firstYear = formation.getFirstYear();
	
	int currentYear = new DateTime().year().get();
	List<String> years = new ArrayList<String>();
	do
	{
	    years.add(String.valueOf(firstYear));
	}
	while(++firstYear <= currentYear);

	return years;
    }

    public Converter getConverter() {
        return null; 
    }

}
