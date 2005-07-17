package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.precedences.IPrecedence;
import net.sourceforge.fenixedu.domain.precedences.IRestriction;
import net.sourceforge.fenixedu.domain.precedences.IRestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.Precedence;
import net.sourceforge.fenixedu.domain.precedences.RestrictionByNumberOfDoneCurricularCourses;
import net.sourceforge.fenixedu.domain.precedences.RestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse;



public class RestrictionTest extends DomainTestBase {

	private IRestriction restriction;
	private IRestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse restrictionByCurricularCourse;
	
	protected void setUp() throws Exception {
        super.setUp();
		
		restriction = new RestrictionByNumberOfDoneCurricularCourses();
		restrictionByCurricularCourse = new RestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse();
		
		restriction.setIdInternal(1);
		restrictionByCurricularCourse.setIdInternal(2);

		IPrecedence precedence = new Precedence();
		precedence.setIdInternal(1);
		
		ICurricularCourse curricularCourse = new CurricularCourse();
		curricularCourse.setIdInternal(1);
		
		restriction.setPrecedence(precedence);
		restrictionByCurricularCourse.setPrecedence(precedence);
		restrictionByCurricularCourse.setPrecedentCurricularCourse(curricularCourse);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }
	
	public void testDelete() {
		
		restriction.delete();
		restrictionByCurricularCourse.delete();

		assertFalse(restriction.hasPrecedence());
		assertFalse(restrictionByCurricularCourse.hasPrecedence());
		assertFalse(restrictionByCurricularCourse.hasPrecedentCurricularCourse());
	}


}
