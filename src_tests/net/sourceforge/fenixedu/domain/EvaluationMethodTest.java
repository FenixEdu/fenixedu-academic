/*
 * Created on Jul 6, 2005
 *  by jdnf and mrsp
 */
package net.sourceforge.fenixedu.domain;

public class EvaluationMethodTest extends DomainTestBase {

    private IExecutionCourse executionCourse;

    private IEvaluationMethod evaluationMethod;

    protected void setUp() throws Exception {
        super.setUp();

        executionCourse = new ExecutionCourse();
        executionCourse.setIdInternal(0);

        evaluationMethod = new EvaluationMethod();
        evaluationMethod.setIdInternal(0);
        evaluationMethod.setEvaluationElements("evaluationElements");
        evaluationMethod.setEvaluationElementsEn("evaluationElementsEng");
        evaluationMethod.setExecutionCourse(executionCourse);
    }

    public void testEdit() {
        try {
            evaluationMethod.edit("newEvaluationElements", null);
            fail("Expected NullPointerException!");
        } catch (NullPointerException e) {
            checkIfEvaluationMethodAttributesAreCorrect("evaluationElements", "evaluationElementsEng",
                    this.executionCourse);
        }

        try {
            evaluationMethod.edit(null, "newEvaluationElementsEng");
            fail("Expected NullPointerException!");
        } catch (NullPointerException e) {
            checkIfEvaluationMethodAttributesAreCorrect("evaluationElements", "evaluationElementsEng",
                    this.executionCourse);
        }

        evaluationMethod.edit("newEvaluationElements", "newEvaluationElementsEng");
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
            final String evaluationElementsEng, final IExecutionCourse executionCourse) {
        assertEquals("Different EvaluationElements in EvaluationMethod!", evaluationElements,
                evaluationMethod.getEvaluationElements());
        assertEquals("Different EvaluationElementsEng in EvaluationMethod!", evaluationElementsEng,
                evaluationMethod.getEvaluationElementsEn());
        assertTrue("Different ExecutionCourse in EvaluationMethod!", evaluationMethod
                .getExecutionCourse().equals(executionCourse));
    }

}
