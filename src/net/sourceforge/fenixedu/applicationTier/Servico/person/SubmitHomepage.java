package net.sourceforge.fenixedu.applicationTier.Servico.person;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.homepage.Homepage;
import net.sourceforge.fenixedu.util.MultiLanguageString;

public class SubmitHomepage extends Service {

    public void run(final Person person, final Boolean activated, final Boolean showUnit,
	    final Boolean showCategory, final Boolean showPhoto, final Boolean showEmail,
	    final Boolean showTelephone, final Boolean showWorkTelephone, final Boolean showMobileTelephone,
	    final Boolean showAlternativeHomepage, final Boolean showResearchUnitHomepage,
	    final Boolean showCurrentExecutionCourses, final Boolean showActiveStudentCurricularPlans,
	    final Boolean showAlumniDegrees, final String researchUnitHomepage,
	    final MultiLanguageString researchUnit, final Boolean showCurrentAttendingExecutionCourses,
	    final Boolean showPublications, final Boolean showPatents, final Boolean showInterests,
	    final Boolean showParticipations) {

	Homepage homepage = person.getHomepage();
	if (homepage == null) {
	    homepage = new Homepage(person);
	}

	homepage.setActivated(activated);
	homepage.setShowUnit(showUnit);
	homepage.setShowCategory(showCategory);
	homepage.setShowPhoto(showPhoto);
	homepage.setShowEmail(showEmail);
	homepage.setShowTelephone(showTelephone);
	homepage.setShowWorkTelephone(showWorkTelephone);
	homepage.setShowMobileTelephone(showMobileTelephone);
	homepage.setShowAlternativeHomepage(showAlternativeHomepage);
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
    }

}