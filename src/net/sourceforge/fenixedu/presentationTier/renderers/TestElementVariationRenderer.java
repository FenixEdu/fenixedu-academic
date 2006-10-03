package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import net.sourceforge.fenixedu.domain.tests.NewAtomicQuestion;
import net.sourceforge.fenixedu.domain.tests.NewMultipleChoiceQuestion;
import net.sourceforge.fenixedu.domain.tests.NewSection;
import net.sourceforge.fenixedu.domain.tests.NewTest;
import net.sourceforge.fenixedu.renderers.Renderer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.layouts.Layout;

public class TestElementVariationRenderer extends Renderer {

	// Our very own polymorphic key finding hashtable... niiiice
	private static Map<Class, String> templateMap = new HashMap<Class, String>() {
		@Override
		public String get(Object key) {
			Class type = (Class) key;

			while (true) {
				// Found none, return a nice null
				if (type == null) {
					return null;
				}

				// Evaluate current type
				String value = super.get(type);
				if (value != null) {
					return value;
				}

				// Not found, try the superclass
				type = type.getSuperclass();
			}
		}
	};

	static {
		templateMap.put(NewTest.class, "/teacher/tests/templates/test/variations/test.jsp");
		templateMap.put(NewSection.class,
				"/teacher/tests/templates/test/variations/section.jsp");
		templateMap.put(NewAtomicQuestion.class, "/teacher/tests/templates/test/variations/atomicQuestion.jsp");
		templateMap.put(NewMultipleChoiceQuestion.class, "/teacher/tests/templates/test/variations/multipleChoiceQuestion.jsp");
	}

	@Override
	protected Layout getLayout(Object object, Class type) {
		return new Layout() {

			@Override
			public HtmlComponent createComponent(Object object, Class type) {
				Properties properties = new Properties();
				properties.setProperty("template", templateMap.get(object.getClass()));

				return renderValue(object, type, null, "template", properties);
			}

		};
	}

}
