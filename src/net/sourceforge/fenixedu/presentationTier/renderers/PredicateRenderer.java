package net.sourceforge.fenixedu.presentationTier.renderers;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;

import net.sourceforge.fenixedu.domain.tests.NewAtomicQuestion;
import net.sourceforge.fenixedu.domain.tests.NewCorrector;
import net.sourceforge.fenixedu.domain.tests.predicates.AndPredicate;
import net.sourceforge.fenixedu.domain.tests.predicates.AnsweredQuestionPredicate;
import net.sourceforge.fenixedu.domain.tests.predicates.CompositePredicate;
import net.sourceforge.fenixedu.domain.tests.predicates.GivenUpQuestionPredicate;
import net.sourceforge.fenixedu.domain.tests.predicates.MultipleChoiceAnswerPredicate;
import net.sourceforge.fenixedu.domain.tests.predicates.MultipleChoiceCountPredicate;
import net.sourceforge.fenixedu.domain.tests.predicates.NotPredicate;
import net.sourceforge.fenixedu.domain.tests.predicates.NumericEqualsPredicate;
import net.sourceforge.fenixedu.domain.tests.predicates.NumericGreaterThanOrEqualPredicate;
import net.sourceforge.fenixedu.domain.tests.predicates.NumericGreaterThanPredicate;
import net.sourceforge.fenixedu.domain.tests.predicates.NumericLessThanOrEqualPredicate;
import net.sourceforge.fenixedu.domain.tests.predicates.NumericLessThanPredicate;
import net.sourceforge.fenixedu.domain.tests.predicates.OrPredicate;
import net.sourceforge.fenixedu.domain.tests.predicates.OutcomeByCorrectorPredicate;
import net.sourceforge.fenixedu.domain.tests.predicates.OutcomeByNoCorrectorPredicate;
import net.sourceforge.fenixedu.domain.tests.predicates.Predicate;
import net.sourceforge.fenixedu.domain.tests.predicates.StringSizeEqualsPredicate;
import net.sourceforge.fenixedu.domain.tests.predicates.StringSizeLessThanPredicate;
import net.sourceforge.fenixedu.domain.tests.predicates.StringSizeMoreThanPredicate;
import net.sourceforge.fenixedu.renderers.OutputRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlInlineContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

public class PredicateRenderer extends OutputRenderer {

	private static final HashMap<Class, Class<? extends Layout>> dispatchMap = new HashMap<Class, Class<? extends Layout>>();

	static {
		dispatchMap.put(OutcomeByCorrectorPredicate.class, OutcomeByCorrectorRenderer.class);
		dispatchMap.put(OutcomeByNoCorrectorPredicate.class, OutcomeByNoCorrectorRenderer.class);
		dispatchMap.put(GivenUpQuestionPredicate.class, GivenUpQuestionRenderer.class);
		dispatchMap.put(AnsweredQuestionPredicate.class, AnsweredQuestionRenderer.class);
		dispatchMap.put(AndPredicate.class, AndRenderer.class);
		dispatchMap.put(OrPredicate.class, OrRenderer.class);
		dispatchMap.put(NotPredicate.class, NotRenderer.class);
		dispatchMap.put(MultipleChoiceAnswerPredicate.class, MultipleChoiceAnswerRenderer.class);
		dispatchMap.put(MultipleChoiceCountPredicate.class, MultipleChoiceCountRenderer.class);
		dispatchMap.put(StringSizeEqualsPredicate.class, StringSizeEqualsRenderer.class);
		dispatchMap.put(StringSizeMoreThanPredicate.class, StringSizeMoreThanRenderer.class);
		dispatchMap.put(StringSizeLessThanPredicate.class, StringSizeLessThanRenderer.class);
		dispatchMap.put(NumericEqualsPredicate.class, NumericEqualsRenderer.class);
		dispatchMap.put(NumericGreaterThanPredicate.class, NumericGreaterThanRenderer.class);
		dispatchMap.put(NumericLessThanPredicate.class, NumericLessThanRenderer.class);
		dispatchMap.put(NumericGreaterThanOrEqualPredicate.class, NumericGreaterThanOrEqualRenderer.class);
		dispatchMap.put(NumericLessThanOrEqualPredicate.class, NumericLessThanOrEqualRenderer.class);
	}

	@Override
	protected Layout getLayout(Object object, Class type) {
		Class<? extends Layout> dispatchClass = dispatchMap.get(object.getClass());
		try {
			Constructor<? extends Layout> dispatchConstructor = dispatchClass
					.getConstructor(new Class[] { PredicateRenderer.class });
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

	private abstract class CompositeRenderer extends Layout {

		public CompositeRenderer() {
			super();
		}

		private HtmlComponent createComponent(Object object, Class type, String separator) {
			CompositePredicate compositePredicate = (CompositePredicate) object;
			HtmlInlineContainer container = new HtmlInlineContainer();

			Iterator<Predicate> predicateIterator = compositePredicate.getPredicates().iterator();

			while (predicateIterator.hasNext()) {
				Predicate nextPredicate = predicateIterator.next();
				HtmlComponent renderedPredicate = renderValue(nextPredicate, null, "values");

				container.addChild(new HtmlText("("));
				container.addChild(renderedPredicate);
				container.addChild(new HtmlText(")"));

				if (predicateIterator.hasNext()) {
					container.addChild(new HtmlText(separator));
				}
			}

			return container;
		}

	}

	private class AndRenderer extends CompositeRenderer {

		public AndRenderer() {
			super();
		}

		@Override
		public HtmlComponent createComponent(Object object, Class type) {
			return super.createComponent(object, type, "e");
		}

	}

	private class OrRenderer extends CompositeRenderer {

		public OrRenderer() {
			super();
		}

		@Override
		public HtmlComponent createComponent(Object object, Class type) {
			return super.createComponent(object, type, "ou");
		}

	}

	private class NotRenderer extends Layout {

		public NotRenderer() {
			super();
		}

		@Override
		public HtmlComponent createComponent(Object object, Class type) {
			NotPredicate notPredicate = (NotPredicate) object;
			HtmlInlineContainer container = new HtmlInlineContainer();

			container.addChild(new HtmlText("não"));
			container.addChild(renderValue(notPredicate.getPredicates().get(0), null, "values"));

			return container;
		}

	}

	private class MultipleChoiceAnswerRenderer extends Layout {

		public MultipleChoiceAnswerRenderer() {
			super();
		}

		@Override
		public HtmlComponent createComponent(Object object, Class type) {
			MultipleChoiceAnswerPredicate predicate = (MultipleChoiceAnswerPredicate) object;
			return new HtmlText("Resposta " + predicate.getChoice().getPosition());
		}

	}

	private class MultipleChoiceCountRenderer extends Layout {

		public MultipleChoiceCountRenderer() {
			super();
		}

		@Override
		public HtmlComponent createComponent(Object object, Class type) {
			MultipleChoiceCountPredicate predicate = (MultipleChoiceCountPredicate) object;
			return new HtmlText(predicate.getCount() + " respostas");
		}

	}

	private class OutcomeByCorrectorRenderer extends Layout {

		public OutcomeByCorrectorRenderer() {
			super();
		}

		@Override
		public HtmlComponent createComponent(Object object, Class type) {
			OutcomeByCorrectorPredicate predicate = (OutcomeByCorrectorPredicate) object;

			NewCorrector corrector = predicate.getCorrector();
			HtmlComponent rendererCorrector = renderValue(corrector, corrector.getClass(), null,
					"values");

			HtmlInlineContainer container = new HtmlInlineContainer();
			container.addChild(new HtmlText(RenderUtils.getResourceString("ENUMERATION_RESOURCES",
					"OUTCOME_BY_CORRECTOR")
					+ ":"));
			container.addChild(rendererCorrector);

			return container;
		}

	}

	private class OutcomeByNoCorrectorRenderer extends Layout {

		public OutcomeByNoCorrectorRenderer() {
			super();
		}

		@Override
		public HtmlComponent createComponent(Object object, Class type) {
			OutcomeByNoCorrectorPredicate predicate = (OutcomeByNoCorrectorPredicate) object;

			NewAtomicQuestion atomicQuestion = predicate.getAtomicQuestion();
			HtmlComponent rendererAtomicQuestion = renderValue(atomicQuestion,
					atomicQuestion.getClass(), null, "sub-question");

			HtmlInlineContainer container = new HtmlInlineContainer();
			container.addChild(new HtmlText(RenderUtils.getResourceString("ENUMERATION_RESOURCES",
					"OUTCOME_BY_NO_CORRECTOR")
					+ ":"));
			container.addChild(rendererAtomicQuestion);

			return container;
		}

	}

	private class GivenUpQuestionRenderer extends Layout {

		public GivenUpQuestionRenderer() {
			super();
		}

		@Override
		public HtmlComponent createComponent(Object object, Class type) {
			GivenUpQuestionPredicate predicate = (GivenUpQuestionPredicate) object;

			NewAtomicQuestion atomicQuestion = predicate.getAtomicQuestion();
			HtmlComponent rendererAtomicQuestion = renderValue(atomicQuestion,
					atomicQuestion.getClass(), null, "sub-question");

			HtmlInlineContainer container = new HtmlInlineContainer();
			container.addChild(new HtmlText(RenderUtils.getResourceString("ENUMERATION_RESOURCES",
					"GIVEN_UP_QUESTION")
					+ ":"));
			container.addChild(rendererAtomicQuestion);

			return container;
		}

	}

	private class AnsweredQuestionRenderer extends Layout {

		public AnsweredQuestionRenderer() {
			super();
		}

		@Override
		public HtmlComponent createComponent(Object object, Class type) {
			AnsweredQuestionPredicate predicate = (AnsweredQuestionPredicate) object;

			NewAtomicQuestion atomicQuestion = predicate.getAtomicQuestion();
			HtmlComponent rendererAtomicQuestion = renderValue(atomicQuestion,
					atomicQuestion.getClass(), null, "sub-question");

			HtmlInlineContainer container = new HtmlInlineContainer();
			container.addChild(new HtmlText(RenderUtils.getResourceString("ENUMERATION_RESOURCES",
					"ANSWERED_QUESTION")
					+ ":"));
			container.addChild(rendererAtomicQuestion);

			return container;
		}

	}

	private class StringSizeEqualsRenderer extends Layout {

		public StringSizeEqualsRenderer() {
			super();
		}

		@Override
		public HtmlComponent createComponent(Object object, Class type) {
			StringSizeEqualsPredicate predicate = (StringSizeEqualsPredicate) object;
			return new HtmlText("Tamanho de resposta igual a " + predicate.getSize());
		}

	}

	private class StringSizeMoreThanRenderer extends Layout {

		public StringSizeMoreThanRenderer() {
			super();
		}

		@Override
		public HtmlComponent createComponent(Object object, Class type) {
			StringSizeMoreThanPredicate predicate = (StringSizeMoreThanPredicate) object;
			return new HtmlText("Tamanho de resposta maior que " + predicate.getSize());
		}

	}

	private class StringSizeLessThanRenderer extends Layout {

		public StringSizeLessThanRenderer() {
			super();
		}

		@Override
		public HtmlComponent createComponent(Object object, Class type) {
			StringSizeLessThanPredicate predicate = (StringSizeLessThanPredicate) object;
			return new HtmlText("Tamanho de resposta menor que " + predicate.getSize());
		}

	}

	private class NumericEqualsRenderer extends Layout {

		public NumericEqualsRenderer() {
			super();
		}

		@Override
		public HtmlComponent createComponent(Object object, Class type) {
			NumericEqualsPredicate predicate = (NumericEqualsPredicate) object;

			return new HtmlText(RenderUtils.getResourceString("ENUMERATION_RESOURCES", "NUMERIC_EQUALS")
					+ " " + predicate.getValue());
		}

	}

	private class NumericGreaterThanRenderer extends Layout {

		public NumericGreaterThanRenderer() {
			super();
		}

		@Override
		public HtmlComponent createComponent(Object object, Class type) {
			NumericGreaterThanPredicate predicate = (NumericGreaterThanPredicate) object;

			return new HtmlText(RenderUtils.getResourceString("ENUMERATION_RESOURCES",
					"NUMERIC_GREATER_THAN")
					+ " " + predicate.getValue());
		}

	}

	private class NumericLessThanRenderer extends Layout {

		public NumericLessThanRenderer() {
			super();
		}

		@Override
		public HtmlComponent createComponent(Object object, Class type) {
			NumericLessThanPredicate predicate = (NumericLessThanPredicate) object;

			return new HtmlText(RenderUtils.getResourceString("ENUMERATION_RESOURCES",
					"NUMERIC_LESS_THAN")
					+ " " + predicate.getValue());
		}

	}

	private class NumericGreaterThanOrEqualRenderer extends Layout {

		public NumericGreaterThanOrEqualRenderer() {
			super();
		}

		@Override
		public HtmlComponent createComponent(Object object, Class type) {
			NumericGreaterThanOrEqualPredicate predicate = (NumericGreaterThanOrEqualPredicate) object;

			return new HtmlText(RenderUtils.getResourceString("ENUMERATION_RESOURCES",
					"NUMERIC_GREATER_THAN_OR_EQUAL")
					+ " " + predicate.getValue());
		}

	}

	private class NumericLessThanOrEqualRenderer extends Layout {

		public NumericLessThanOrEqualRenderer() {
			super();
		}

		@Override
		public HtmlComponent createComponent(Object object, Class type) {
			NumericLessThanOrEqualPredicate predicate = (NumericLessThanOrEqualPredicate) object;

			return new HtmlText(RenderUtils.getResourceString("ENUMERATION_RESOURCES",
					"NUMERIC_LESS_THAN_OR_EQUAL")
					+ " " + predicate.getValue());
		}

	}

}
