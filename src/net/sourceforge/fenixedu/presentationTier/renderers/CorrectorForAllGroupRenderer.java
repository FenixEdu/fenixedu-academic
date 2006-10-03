package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.Iterator;

import net.sourceforge.fenixedu.domain.tests.NewCorrector;
import net.sourceforge.fenixedu.renderers.OutputRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlInlineContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.layouts.Layout;

public class CorrectorForAllGroupRenderer extends OutputRenderer {

	private String classes;

	public String getClasses() {
		return classes;
	}

	public void setClasses(String classes) {
		this.classes = classes;
	}

	@Override
	protected Layout getLayout(Object object, Class type) {
		return new Layout() {

			@Override
			public HtmlComponent createComponent(Object object, Class type) {
				NewCorrector corrector = (NewCorrector) object;

				HtmlInlineContainer container = new HtmlInlineContainer();

				StringBuilder builder = new StringBuilder("Pergunta: ");

				Iterator<Integer> iterator = corrector.getAtomicQuestion().getPath().iterator();
				while (iterator.hasNext()) {
					Integer position = iterator.next();
					builder.append(position);

					if (iterator.hasNext()) {
						builder.append(".");
					}
				}

				container.addChild(new HtmlText(builder.toString() + ":"));

				container.addChild(renderValue(corrector.getPredicate(), corrector.getPredicate()
						.getClass(), null, "values"));

				return container;
			}

		};
	}

}
