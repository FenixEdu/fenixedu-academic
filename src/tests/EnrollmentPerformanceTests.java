/*
 * Created on Feb 7, 2005
 *
 */
package tests;

import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.enrollment.DeleteEnrolment;
import ServidorAplicacao.Servico.enrollment.ShowAvailableCurricularCourses;
import ServidorAplicacao.Servico.enrollment.WriteEnrollment;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.ObjectFenixOJB;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.enrollment.CurricularCourseEnrollmentType;

/**
 * @author Luis Cruz
 * 
 */
public class EnrollmentPerformanceTests extends ObjectFenixOJB
{

    private static final int NUM_ITERATIONS = 2;

    private static ISuportePersistente persistentSupport;

    private static final UserView userView = new UserView("TestScript", null);

    public static void main(String[] args)
    {
        try
        {
            System.out.println("Testing with " + NUM_ITERATIONS + " iterations.");
            persistentSupport = SuportePersistenteOJB.getInstance();
            long startTime = System.currentTimeMillis();
            for (int i = 0; i < NUM_ITERATIONS; i++)
            {
                run();
            }
            long entTime = System.currentTimeMillis();
            System.out.println("Total time: " + (entTime - startTime) + "ms");
        } catch (Exception ex)
        {
            try
            {
                persistentSupport.cancelarTransaccao();
                throw new RuntimeException("Transaction aborted.", ex);
            } catch (ExcepcaoPersistencia exp)
            {
                throw new RuntimeException("Unable to abort transaction.", exp);
            }
        }
        System.out.println("Done.");
        System.exit(0);
    }

    protected static void run() throws FenixServiceException, ExcepcaoPersistencia
    {
        long startTime = System.currentTimeMillis();
        testEnrollmentProcess(null, new Integer(9068), new Integer(54342), new Integer(82),
                CurricularCourseEnrollmentType.TEMPORARY,
                new Integer[] { new Integer(3170), new Integer(3239), new Integer(3268), new Integer(3258), new Integer(3609),
                        new Integer(3275), new Integer(3167) },
                new Integer[] { null, null, null, null, null, null, null });
        long entTime = System.currentTimeMillis();
        System.out.println("LEIC-Taguspark - 2º ano - 7 enrollments: : " + (entTime - startTime) + "ms");

        startTime = System.currentTimeMillis();
        testEnrollmentProcess(null, new Integer(3473), new Integer(48000), new Integer(82),
                CurricularCourseEnrollmentType.TEMPORARY,
                new Integer[] { new Integer(3046) },
                new Integer[] { new Integer(1) });
        entTime = System.currentTimeMillis();
        System.out.println("LEEC2003/2004 - 5º ano - 1 enrollments: : " + (entTime - startTime) + "ms");

        startTime = System.currentTimeMillis();
        testEnrollmentProcess(null, new Integer(3880), new Integer(48500), new Integer(82),
                CurricularCourseEnrollmentType.TEMPORARY,
                new Integer[] { new Integer(2755) },
                new Integer[] { new Integer(2) });
        entTime = System.currentTimeMillis();
        System.out.println("LEM2003/2004 - 5º ano - 1 enrollments: : " + (entTime - startTime) + "ms");

        startTime = System.currentTimeMillis();
        testEnrollmentProcess(null, new Integer(4246), new Integer(49000), new Integer(82),
                CurricularCourseEnrollmentType.TEMPORARY,
                new Integer[] { new Integer(3655), new Integer(3372), new Integer(11303), new Integer(3395), new Integer(3339), new Integer(3526), new Integer(3423) },
                new Integer[] { new Integer(2), new Integer(2), new Integer(2), new Integer(2), new Integer(2), new Integer(2), new Integer(2) });
        entTime = System.currentTimeMillis();
        System.out.println("LEAM2003/2004 - 5º ano - 7 enrollments: : " + (entTime - startTime) + "ms");
//
//        startTime = System.currentTimeMillis();
//        testEnrollmentProcess(null, new Integer(), new Integer(), new Integer(82),
//                CurricularCourseEnrollmentType.TEMPORARY,
//                new Integer[] { new Integer() },
//                new Integer[] { new Integer() });
//        entTime = System.currentTimeMillis();
//        System.out.println(" - º ano -  enrollments: : " + (entTime - startTime) + "ms");
}

    protected static void testEnrollmentProcess(final Integer executionDegreeId,
            final Integer studentCurricularPlanId, final Integer studentNumber,
            final Integer executionPeriodId, final CurricularCourseEnrollmentType enrollmentType,
            final Object[] curricularCourseIds, final Object[] enrollmentTypes)
            throws FenixServiceException, ExcepcaoPersistencia
    {
        showAvailableCurricularCourses(executionDegreeId, studentCurricularPlanId, studentNumber);

        final Integer enrollmentIds[] = new Integer[curricularCourseIds.length];
        for (int i = 0; i < curricularCourseIds.length; i++)
        {
            final Integer curricularCourseId = (Integer) curricularCourseIds[i];
            final Integer enrollmentClass = (Integer) enrollmentTypes[i];

            final Integer enrollmentId = writeEnrollment(executionDegreeId, studentCurricularPlanId,
                    studentNumber, executionPeriodId, curricularCourseId, enrollmentType,
                    enrollmentClass);
            enrollmentIds[i] = enrollmentId;
            showAvailableCurricularCourses(executionDegreeId, studentCurricularPlanId, studentNumber);
        }

        for (int i = enrollmentIds.length; i > 0; i--)
        {
            final Integer enrollmentId = enrollmentIds[i - 1];

            if (enrollmentId != null) {
                deleteEnrolment(executionDegreeId, studentCurricularPlanId, enrollmentId);
                showAvailableCurricularCourses(executionDegreeId, studentCurricularPlanId, studentNumber);
            }
        }
    }

    protected static void deleteEnrolment(final Integer executionDegreeId,
            final Integer studentCurricularPlanId, final Integer enrollmentId)
            throws FenixServiceException, ExcepcaoPersistencia
    {
        persistentSupport.iniciarTransaccao();
        final DeleteEnrolment service = new DeleteEnrolment();
        service.run(executionDegreeId, studentCurricularPlanId, enrollmentId);
        persistentSupport.confirmarTransaccao();
    }

    protected static Integer writeEnrollment(final Integer executionDegreeId,
            final Integer studentCurricularPlanId, final Integer studentNumber,
            final Integer executionPeriodId, final Integer curricularCourseId,
            final CurricularCourseEnrollmentType enrollmentType, final Integer enrollmentClass)
            throws ExcepcaoPersistencia
    {
        persistentSupport.iniciarTransaccao();
        final WriteEnrollment service = new WriteEnrollment();
        final Integer enrollmentId = service.run(executionDegreeId, studentCurricularPlanId, curricularCourseId,
                executionPeriodId, enrollmentType, enrollmentClass, userView);
        persistentSupport.confirmarTransaccao();

        return enrollmentId;
    }

    protected static void showAvailableCurricularCourses(final Integer executionDegreeId,
            final Integer studentCurricularPlanId, final Integer studentNumber)
            throws FenixServiceException, ExcepcaoPersistencia
    {
        persistentSupport.iniciarTransaccao();
        final ShowAvailableCurricularCourses service = new ShowAvailableCurricularCourses();
        service.run(executionDegreeId, studentCurricularPlanId, studentNumber);
        persistentSupport.confirmarTransaccao();
    }

}