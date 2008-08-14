package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student;

public class StudentEquivalencesDA extends StudentDismissalsDA {
    @Override
    protected String getServiceName() {
	return "CreateNewEquivalenceDismissal";
    }
}
