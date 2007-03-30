package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.teacher.Category;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class CategoryProvider implements DataProvider {

    private static String[] USED_CATEGORIES = {
            "ASC", "ESP", "PCA", "PCC", "PAS", "PSC", "PAX", "PXC"
    };
    
    public Object provide(Object source, Object currentValue) {
        List<String> result = new ArrayList<String>();
        
        List<String> allowed = Arrays.asList(USED_CATEGORIES);
        for (Category category : new TreeSet<Category>(RootDomainObject.getInstance().getCategorys())) {
            if (allowed.contains(category.getCode().toUpperCase())) {
                result.add(category.getName().getContent());
            }
        }
        
        return result;
    }

    public Converter getConverter() {
        return null;
    }

}
