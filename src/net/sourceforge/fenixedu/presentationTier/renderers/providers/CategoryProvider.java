package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.List;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class CategoryProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	List<String> result = new ArrayList<String>();

	result.add(hardcoded("Assistente Convidado", "Invited Lecturer"));
	result.add(hardcoded("Professor Associado", "Associate Professor"));
	result.add(hardcoded("Professor Auxiliar", "Assistant Professor"));
	result.add(hardcoded("Professor Catedratico ", "Full Professor"));
	result.add(hardcoded("Professor Catedratico Convidado", "Invited Full Professor"));
	result.add(hardcoded("Professor Associado Convidado", "Invited Associate Professor"));
	result.add(hardcoded("Professor Auxiliar Convidado", "Invited Assistant Professor"));

	result.add(hardcoded("Especialista", "Especialist"));
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
