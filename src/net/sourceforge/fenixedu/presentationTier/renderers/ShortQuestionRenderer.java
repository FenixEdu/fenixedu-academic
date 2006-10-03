package net.sourceforge.fenixedu.presentationTier.renderers;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import net.sourceforge.fenixedu.domain.tests.NewAllGroup;
import net.sourceforge.fenixedu.domain.tests.NewAtomicQuestion;
import net.sourceforge.fenixedu.domain.tests.NewDateQuestion;
import net.sourceforge.fenixedu.domain.tests.NewMultipleChoiceQuestion;
import net.sourceforge.fenixedu.domain.tests.NewNumericQuestion;
import net.sourceforge.fenixedu.domain.tests.NewQuestionBank;
import net.sourceforge.fenixedu.domain.tests.NewQuestionGroup;
import net.sourceforge.fenixedu.domain.tests.NewStringQuestion;
import net.sourceforge.fenixedu.renderers.OutputRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

public class ShortQuestionRenderer extends OutputRenderer {

	private static Map<Class, Class<? extends Layout>> dispatchMap = new HashMap<Class, Class<? extends Layout>>();

	static {
		dispatchMap.put(NewQuestionBank.class, QuestionBankLayout.class);
		dispatchMap.put(NewQuestionGroup.class, QuestionGroupLayout.class);
		dispatchMap.put(NewAllGroup.class, AllGroupLayout.class);
		dispatchMap.put(NewMultipleChoiceQuestion.class, AtomicQuestionLayout.class);
		dispatchMap.put(NewDateQuestion.class, AtomicQuestionLayout.class);
		dispatchMap.put(NewStringQuestion.class, AtomicQuestionLayout.class);
		dispatchMap.put(NewNumericQuestion.class, AtomicQuestionLayout.class);
	}

	private class QuestionBankLayout extends Layout {

		public QuestionBankLayout() {
			super();
		}

		@Override
		public HtmlComponent createComponent(Object object, Class type) {
			NewQuestionBank questionBank = (NewQuestionBank) object;
			Properties properties = new Properties();
			properties.setProperty("linkFormat",
					"/tests/questionBank.do?method=manageQuestionBank&view=tree&oid="
							+ questionBank.getIdInternal());
			properties.setProperty("text", RenderUtils.getResourceString("TESTS_RESOURCES",
					"title.managePermissionUnits")
					+ " " + questionBank.getOwner().getName());
			return renderValue(questionBank, NewQuestionBank.class, null, "link", properties);
		}

	}

	private class QuestionGroupLayout extends Layout {

		public QuestionGroupLayout() {
			super();
		}

		@Override
		public HtmlComponent createComponent(Object object, Class type) {
			NewQuestionGroup questionGroup = (NewQuestionGroup) object;
			Properties properties = new Properties();
			properties.setProperty("linkFormat", "/tests/questionBank.do?method=editTestElement&oid="
					+ questionGroup.getIdInternal());
			properties.setProperty("text", questionGroup.getName());
			return renderValue(questionGroup, NewQuestionGroup.class, null, "link", properties);
		}

	}

	private class AllGroupLayout extends Layout {

		public AllGroupLayout() {
			super();
		}

		@Override
		public HtmlComponent createComponent(Object object, Class type) {
			NewAllGroup allGroup = (NewAllGroup) object;
			Properties properties = new Properties();
			properties.setProperty("linkFormat", "/tests/questionBank.do?method=editTestElement&oid="
					+ allGroup.getIdInternal());
			properties.setProperty("text", RenderUtils.getResourceString("TESTS_RESOURCES",
					"label.allGroup"));
			return renderValue(allGroup, NewAllGroup.class, null, "link", properties);
		}

	}

	private class AtomicQuestionLayout extends Layout {

		public AtomicQuestionLayout() {
			super();
		}

		@Override
		public HtmlComponent createComponent(Object object, Class type) {
			NewAtomicQuestion atomicQuestion = (NewAtomicQuestion) object;
			Properties properties = new Properties();
			properties.setProperty("emptyMessageKey", "tests.presentationMaterials.empty");
			properties.setProperty("emptyMessageClasses", "emptyMessage");
			properties.setProperty("emptyMessageBundle", "TESTS_RESOURCES");
			return renderValue(atomicQuestion.getOrderedPresentationMaterials(), List.class, null, "flowLayout", properties);
		}

	}

	@Override
	protected Layout getLayout(Object object, Class type) {
		Class<? extends Layout> dispatchClass = dispatchMap.get(object.getClass());
		try {
			Constructor<? extends Layout> dispatchConstructor = dispatchClass
					.getConstructor(new Class[] { ShortQuestionRenderer.class });
			Layout newInstance = dispatchConstructor.newInstance(new Object[] { this });

			return newInstance;
		} catch (InstantiationException e) {
			throw new RuntimeException("could.not.init.class", e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("could.not.init.class", e);
		} catch (SecurityException e) {
			throw new RuntimeException("could.not.init.class", e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException("could.not.init.class", e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("could.not.init.class", e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException("could.not.init.class", e);
		}
	}

}
