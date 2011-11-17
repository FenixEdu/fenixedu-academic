package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;

import net.sourceforge.fenixedu.domain.phd.debts.PhdGratuityModel;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class PhdGratuityModelProvider implements DataProvider{

    @Override
    public Converter getConverter() {
	return null;
    }

    @Override
    public Object provide(Object arg0, Object arg1) {
	ArrayList<PhdGratuityModel> arrayList = new ArrayList<PhdGratuityModel>();
	arrayList.add(PhdGratuityModel.DEFAULT);
	arrayList.add(PhdGratuityModel.FCT);
	return arrayList;
    }

}
