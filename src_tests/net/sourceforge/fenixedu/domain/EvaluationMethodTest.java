/*
 * Created on Jul 6, 2005
 *  by jdnf and mrsp
 */
package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.util.MultiLanguageString;

public class EvaluationMethodTest extends DomainTestBase {

    private ExecutionCourse executionCourse;

    private EvaluationMethod evaluationMethod;

    protected void setUp() throws Exception {
        super.setUp();

        executionCourse = new ExecutionCourse();
        executionCourse.setIdInternal(0);

        evaluationMethod = new EvaluationMethod();
        evaluationMethod.setIdInternal(0);
        evaluationMethod.setEvaluationElements(new MultiLanguageString(Language.pt, "evaluationElements"));
        evaluationMethod.setExecutionCourse(executionCourse);
    }

    public void testEdit() {
        try {
            evaluationMethod.edit(new MultiLanguageString(Language.pt, "newEvaluationElements"));
            fail("Expected NullPointerException!");
        } catch (NullPointerException e) {
            checkIfEvaluationMethodAttributesAreCorrect("evaluationElements", "evaluationElementsEng",
                    this.executionCourse);
        }

        try {
            evaluationMethod.edit(new MultiLanguageString(Language.pt, "newEvaluationElementsEng"));
            fail("Expected NullPointerException!");
        } catch (NullPointerException e) {
            checkIfEvaluationMethodAttributesAreCorrect("evaluationElements", "evaluationElementsEng",
                    this.executionCourse);
        }

        evaluationMethod.edit(new MultiLanguageString(Language.pt, "newEvaluationElements"));
        checkIfEvaluationMethodAttributesAreCorrect("newEvaluationElements", "newEvaluationElementsEng",
                this.executionCourse);
    }

    public void testDelete() {
        assertNotNull("Expected Not Null ExecutionCourse in EvaluationMethod!", evaluationMethod
                .getExecutionCourse());
        assertNotNull("Expected Not Null EvaluationMethod in ExecutionCourse!", executionCourse
                .getEvaluationMethod());
        evaluationMethod.delete();
        assertNull("Expected Null ExecutionCourse in EvaluationMethod!", evaluationMethod
                .getExecutionCourse());
        assertNull("Expected Null EvaluationMethod in ExecutionCourse!", executionCourse
                .getEvaluationMethod());
    }

    private void checkIfEvaluationMethodAttributesAreCorrect(final String evaluationElements,
            final String evaluationElementsEng, final ExecutionCourse executionCourse) {
        assertEquals("Different EvaluationElements in EvaluationMethod!", evaluationElements,
                evaluationMethod.getEvaluationElements());
        assertEquals("Different EvaluationElementsEng in EvaluationMethod!", evaluationElementsEng,
                evaluationMethod.getEvaluationElements().getContent(Language.en));
        assertTrue("Different ExecutionCourse in EvaluationMethod!", evaluationMethod
                .getExecutionCourse().equals(executionCourse));
    }

}
