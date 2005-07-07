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

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testEdit() {
        try {
            evaluationMethod.edit("newEvaluationElements", null);
            fail("Expected NullPointerException!");
        } catch (NullPointerException e) {
            assertEquals("Different EvaluationElements in EvaluationMethod!", "evaluationElements", evaluationMethod.getEvaluationElements());
            assertEquals("Different EvaluationElementsEng in EvaluationMethod!", "evaluationElementsEng", evaluationMethod.getEvaluationElementsEn());
            assertTrue("Different ExecutionCourse in EvaluationMethod!", evaluationMethod.getExecutionCourse().equals(executionCourse));
        }

        try {
            evaluationMethod.edit(null, "newEvaluationElementsEng");
            fail("Expected NullPointerException!");
        } catch (NullPointerException e) {
            assertEquals("Different EvaluationElements in EvaluationMethod!", "evaluationElements", evaluationMethod.getEvaluationElements());
            assertEquals("Different EvaluationElementsEng in EvaluationMethod!", "evaluationElementsEng", evaluationMethod.getEvaluationElementsEn());
            assertTrue("Different ExecutionCourse in EvaluationMethod!", evaluationMethod.getExecutionCourse().equals(executionCourse));
        }
        
        evaluationMethod.edit("newEvaluationElements", "newEvaluationElementsEng");
        assertEquals("Different EvaluationElements in EvaluationMethod!", "newEvaluationElements", evaluationMethod.getEvaluationElements());
        assertEquals("Different EvaluationElementsEng in EvaluationMethod!", "newEvaluationElementsEng", evaluationMethod.getEvaluationElementsEn());
        assertTrue("Different ExecutionCourse in EvaluationMethod!", evaluationMethod.getExecutionCourse().equals(executionCourse));
    }

    public void testDelete() {
        assertNotNull("Expected Not Null ExecutionCourse in EvaluationMethod!", evaluationMethod.getExecutionCourse());
        assertNotNull("Expected Not Null EvaluationMethod in ExecutionCourse!", executionCourse.getEvaluationMethod());
        evaluationMethod.delete();
        assertNull("Expected Null ExecutionCourse in EvaluationMethod!", evaluationMethod.getExecutionCourse());
        assertNull("Expected Null EvaluationMethod in ExecutionCourse!", executionCourse.getEvaluationMethod());
    }

}
