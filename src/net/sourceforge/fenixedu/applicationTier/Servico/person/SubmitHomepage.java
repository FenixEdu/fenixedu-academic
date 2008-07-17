package net.sourceforge.fenixedu.applicationTier.Servico.person;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.homepage.Homepage;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class SubmitHomepage extends Service {

    public void run(final Person person, final Boolean activated, final Boolean showUnit, final Boolean showCategory,
	    final Boolean showPhoto, final Boolean showResearchUnitHomepage, final Boolean showCurrentExecutionCourses,
	    final Boolean showActiveStudentCurricularPlans, final Boolean showAlumniDegrees, final String researchUnitHomepage,
	    final MultiLanguageString researchUnit, final Boolean showCurrentAttendingExecutionCourses,
	    final Boolean showPublications, final Boolean showPatents, final Boolean showInterests,
	    final Boolean showParticipations, final Boolean showPrizes) {

	Homepage homepage = person.initializeSite();

	homepage.setActivated(activated);
	homepage.setShowUnit(showUnit);
	homepage.setShowCategory(showCategory);
	homepage.setShowPhoto(showPhoto);
	homepage.setShowResearchUnitHomepage(showResearchUnitHomepage);
	homepage.setShowCurrentExecutionCourses(showCurrentExecutionCourses);
	homepage.setShowActiveStudentCurricularPlans(showActiveStudentCurricularPlans);
	homepage.setShowAlumniDegrees(showAlumniDegrees);
	homepage.setResearchUnitHomepage(researchUnitHomepage);
	homepage.setResearchUnit(researchUnit);
	homepage.setShowCurrentAttendingExecutionCourses(showCurrentAttendingExecutionCourses);
	homepage.setShowPublications(showPublications);
	homepage.setShowPatents(showPatents);
	homepage.setShowInterests(showInterests);
	homepage.setShowParticipations(showParticipations);
	homepage.setShowPrizes(showPrizes);
    }

}