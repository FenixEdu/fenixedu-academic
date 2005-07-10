package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.precedences.IPrecedence;
import net.sourceforge.fenixedu.domain.precedences.IRestriction;
import net.sourceforge.fenixedu.domain.precedences.IRestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.Precedence;
import net.sourceforge.fenixedu.domain.precedences.RestrictionByNumberOfDoneCurricularCourses;
import net.sourceforge.fenixedu.domain.precedences.RestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse;

public class PrecedenceTest extends DomainTestBase {

	private IPrecedence precedence;
	private IRestriction restriction;
	private IRestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse restrictionByCurricularCourse;
	
	protected void setUp() throws Exception {
        super.setUp();
		
		precedence = new Precedence();
		precedence.setIdInternal(1);
		
		restriction = new RestrictionByNumberOfDoneCurricularCourses();
		restrictionByCurricularCourse = new RestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse();
		
		restriction.setIdInternal(1);
		restrictionByCurricularCourse.setIdInternal(2);

		ICurricularCourse curricularCourse = new CurricularCourse();
		curricularCourse.setIdInternal(1);
		
		List<IRestriction> restrictions = new ArrayList<IRestriction>();
		
		restrictions.add(restriction);
		restrictions.add(restrictionByCurricularCourse);
		precedence.setRestrictions(restrictions);
		restrictionByCurricularCourse.setPrecedentCurricularCourse(curricularCourse);
		precedence.setCurricularCourse(curricularCourse);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }
	
	public void testDeleteRestriction() {
		
		precedence.deletePrecedence();
		
		assertNull(precedence.getCurricularCourse());
		assertTrue(precedence.getRestrictions().isEmpty());
				
		assertNull(restriction.getPrecedence());
		assertNull(restrictionByCurricularCourse.getPrecedence());
		assertNull(restrictionByCurricularCourse.getPrecedentCurricularCourse());
	}
}
