package net.sourceforge.fenixedu.predicates;

import net.sourceforge.fenixedu.domain.MarkSheet;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;

public class MarkSheetPredicates {

    public static final AccessControlPredicate<MarkSheet> confirmPredicate = new AccessControlPredicate<MarkSheet>() {

        @Override
        public boolean evaluate(final MarkSheet markSheet) {
            return AcademicPredicates.MANAGE_MARKSHEETS.evaluate(null) && (!markSheet.isRectification() || checkRectification());
        }

    };

    public static final AccessControlPredicate<MarkSheet> editPredicate = new AccessControlPredicate<MarkSheet>() {

        @Override
        public boolean evaluate(final MarkSheet markSheet) {
            return hasScientificCouncilRole()
                    || hasTeacherRole()
                    || (AcademicPredicates.MANAGE_MARKSHEETS.evaluate(null)
                            && (!markSheet.isRectification() || checkRectification()) && (!markSheet.isDissertation() || checkDissertation()));
        }

    };

    public static final AccessControlPredicate<MarkSheet> rectifyPredicate = new AccessControlPredicate<MarkSheet>() {

        @Override
        public boolean evaluate(MarkSheet markSheet) {
            return AcademicPredicates.RECTIFICATION_MARKSHEETS.evaluate(null) && checkRectification()
                    && (!markSheet.isDissertation() || checkDissertation());
        }
    };

    static public boolean checkRectification() {
        return AcademicPredicates.RECTIFICATION_MARKSHEETS.evaluate(null);
    }

    static public boolean checkDissertation() {
        return AcademicPredicates.DISSERTATION_MARKSHEETS.evaluate(null);
    }

    private static boolean hasScientificCouncilRole() {
        return RolePredicates.SCIENTIFIC_COUNCIL_PREDICATE.evaluate(null);
    }

    private static boolean hasTeacherRole() {
        return RolePredicates.TEACHER_PREDICATE.evaluate(null);
    }
}
