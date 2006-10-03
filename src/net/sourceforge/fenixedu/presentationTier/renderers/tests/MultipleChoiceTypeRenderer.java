package net.sourceforge.fenixedu.presentationTier.renderers.tests;

import net.sourceforge.fenixedu.domain.tests.NewAtomicQuestion;
import net.sourceforge.fenixedu.renderers.layouts.Layout;

public class MultipleChoiceTypeRenderer extends AnswerTypeRenderer {

    public MultipleChoiceTypeRenderer(NewAtomicQuestion atomicQuestion) {
        super(atomicQuestion);
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
    protected final NewMultipleChoiceType multipleChoiceType;

    public MultipleChoiceTypeRenderer(NewAtomicQuestion atomicQuestion) {
        super(atomicQuestion);

        multipleChoiceType = (NewMultipleChoiceType) atomicQuestion.getAnswerType();
    }

    protected class MultipleChoiceTypeConverter extends DomainObjectKeyArrayConverter {
        private static final long serialVersionUID = -6998438356430781240L;

        @Override
        public Object convert(Class type, Object value) {
            List<NewChoice> choices = (List<NewChoice>) super.convert(type, value);

            StringBuffer stringBuffer = new StringBuffer();

            for (NewChoice choice : choices) {
                stringBuffer.append(choice.getIdInternal());
                stringBuffer.append(" ");
            }

            return stringBuffer.toString();
        }
    }

    protected class MultipleChoiceLayout extends Layout {

        @Override
        public HtmlComponent createComponent(Object object, Class type) {
            HtmlCheckBoxList checkBoxList = new HtmlCheckBoxList();

            checkBoxList.setConverter(new MultipleChoiceTypeConverter());
            checkBoxList.setTargetSlot((MetaSlotKey) getInputContext().getMetaObject().getKey());

            checkBoxList.setClasses("choices");

            List<NewChoice> choices = processPreviousAnswers();

            for (NewChoice choice : multipleChoiceType.getChoices()) {
                MetaObject metaObject = MetaObjectFactory.createObject(choice
                        .getOrderedPresentationMaterials(), null);

                PresentationContext context = getContext().createSubContext(metaObject);
                context.setRenderMode(RenderMode.getMode("output"));

                Properties properties = new Properties();
                properties.put("classes", "choicePresentationMaterials");
                properties.put("eachClasses", "choicePresentationMaterial");

                context.setProperties(properties);
                context.setLayout("flowLayout");

                HtmlComponent component = RenderKit.getInstance().render(context,
                        choice.getOrderedPresentationMaterials());
                HtmlCheckBox htmlCheckBox = checkBoxList.addOption(component, MetaObjectFactory
                        .createObject(choice, null).getKey().toString());

                htmlCheckBox.setChecked(choices.contains(choice));

                htmlCheckBox.setClasses("choice");
            }

            return checkBoxList;
        }

        @Override
        public void applyStyle(HtmlComponent component) {
            // Left blank on purpose
        }

        private List<NewChoice> processPreviousAnswers() {
            List<NewChoice> choices = new ArrayList<NewChoice>();

            if (atomicQuestion.getAnswerValue() == null
                    || atomicQuestion.getAnswerValue().getAnswer().trim().length() == 0)
                return choices;

            String composedAnswer = atomicQuestion.getAnswerValue().getAnswer();
            String[] choiceIds = composedAnswer.split(" ");

            for (String choiceId : choiceIds) {
                NewChoice choice = RootDomainObject.getInstance().readNewChoiceByOID(
                        Integer.parseInt(choiceId));
                choices.add(choice);
            }

            return choices;
        }

    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new MultipleChoiceLayout();
    }*/

}
