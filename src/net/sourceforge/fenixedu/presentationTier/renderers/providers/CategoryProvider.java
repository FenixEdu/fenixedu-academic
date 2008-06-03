package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

import pt.utl.ist.fenix.tools.util.i18n.Language;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.teacher.Category;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class CategoryProvider implements DataProvider {

    private static String[] USED_CATEGORIES = { "ASC", "ESP", "PCA", "PCC", "PAS", "PSC", "PAX", "PXC" };

    public Object provide(Object source, Object currentValue) {
	List<String> result = new ArrayList<String>();

	List<String> allowed = Arrays.asList(USED_CATEGORIES);
	for (Category category : new TreeSet<Category>(Category.readTeacherCategories())) {
	    if (allowed.contains(category.getCode().toUpperCase())) {
		result.add(category.getName().getContent());
	    }
	}

	result.add(hardcoded("Investigador Principal", "Main Researcher"));
	result.add(hardcoded("Investigador Auxiliar", "Auxiliar Researcher"));
	result.add(hardcoded("Investigador Coordenador", "Coordinator Researcher"));

	return result;
    }

    private String hardcoded(String pt, String en) {
	MultiLanguageString mlString = new MultiLanguageString();

	mlString.setContent(Language.pt, pt);
	mlString.setContent(Language.en, en);

	return mlString.getContent();
    }

    public Converter getConverter() {
	return null;
    }

}
