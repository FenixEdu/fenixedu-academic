package net.sourceforge.fenixedu.presentationTier.renderers.tests;

import net.sourceforge.fenixedu.renderers.InputRenderer;
import net.sourceforge.fenixedu.renderers.layouts.Layout;

public class AnswerInputDispatchRenderer extends InputRenderer {
    @Override
    protected Layout getLayout(Object object, Class type) {
        return null;
    }
    
    /*private static final Map<Class, Class> renderers = new HashMap<Class, Class>();

    static {
        renderers.put(NewDateAnswerType.class, DateAnswerTypeRenderer.class);
        renderers.put(NewStringAnswerType.class, StringAnswerTypeRenderer.class);
        renderers.put(NewMultipleChoiceType.class, MultipleChoiceTypeRenderer.class);
        renderers.put(NewNumericAnswerType.class, NumericAnswerTypeRenderer.class);
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        NewAtomicQuestion atomicQuestion = (NewAtomicQuestion) getInputContext().getParentContext()
                .getMetaObject().getObject();

        MetaSlot slot = (MetaSlot) getInputContext().getMetaObject();

        final HtmlComponent component;

        try {
            Class rendererClass = renderers.get(atomicQuestion.getAnswerType().getClass());
            Constructor rendererConstructor = rendererClass
                    .getConstructor(new Class[] { NewAtomicQuestion.class });
            Renderer renderer = (Renderer) rendererConstructor
                    .newInstance(new Object[] { atomicQuestion });

            component = renderSlot(renderer, slot);
        } catch (Exception e) {
            throw new Error("could not create renderer", e);
        }

        // Validatable validatable = findValidatableComponent(component);

        return new Layout() {
            @Override
            public HtmlComponent createComponent(Object object, Class type) {
                return component;
            }

            @Override
            public void applyStyle(HtmlComponent component) {
                // Left blank on purpose
            }

        };
    }*/
}
