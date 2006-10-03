package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.Iterator;

import net.sourceforge.fenixedu.domain.tests.NewAtomicQuestion;
import net.sourceforge.fenixedu.renderers.StringRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlText;

public class AtomicQuestionForAllGroupRenderer extends StringRenderer {

	private String classes;

	public String getClasses() {
		return classes;
	}

	public void setClasses(String classes) {
		this.classes = classes;
	}

	@Override
	public HtmlComponent render(Object object, Class type) {
		NewAtomicQuestion atomicQuestion = (NewAtomicQuestion) object;

		StringBuilder builder = new StringBuilder("Alinea ");

		Iterator<Integer> iterator = atomicQuestion.getPath().iterator();
		while (iterator.hasNext()) {
			Integer position = iterator.next();
			builder.append(position);

			if (iterator.hasNext()) {
				builder.append(".");
			}
		}

		return new HtmlText(builder.toString());
	}

}
