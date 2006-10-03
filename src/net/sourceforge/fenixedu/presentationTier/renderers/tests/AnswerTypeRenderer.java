package net.sourceforge.fenixedu.presentationTier.renderers.tests;

import net.sourceforge.fenixedu.domain.tests.NewAtomicQuestion;
import net.sourceforge.fenixedu.renderers.InputRenderer;

public abstract class AnswerTypeRenderer extends InputRenderer {

    protected final NewAtomicQuestion atomicQuestion;

    public AnswerTypeRenderer(NewAtomicQuestion atomicQuestion) {
        super();

       this.atomicQuestion = atomicQuestion;
    }
    
}
