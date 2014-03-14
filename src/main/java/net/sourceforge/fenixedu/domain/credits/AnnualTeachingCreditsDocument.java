package net.sourceforge.fenixedu.domain.credits;

import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.accessControl.GroupUnion;
import net.sourceforge.fenixedu.domain.accessControl.PersonGroup;
import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.IGroup;

public class AnnualTeachingCreditsDocument extends AnnualTeachingCreditsDocument_Base {

    public AnnualTeachingCreditsDocument(AnnualTeachingCredits annualTeachingCredits, byte[] content,
            boolean hasConfidencialInformation) {
        super();
        String filename = getFilename(annualTeachingCredits);

        final Collection<IGroup> groups = new ArrayList<>();
        final Teacher teacher = annualTeachingCredits.getTeacher();
        if (teacher != null) {
            final Person person = teacher.getPerson();
            if (person != null) {
                groups.add(new PersonGroup(person));
            }
        }
        groups.add(new RoleGroup(RoleType.SCIENTIFIC_COUNCIL));

        init(filename, filename, content, new GroupUnion(groups));
        setAnnualTeachingCredits(annualTeachingCredits);
        setHasConfidencialInformation(hasConfidencialInformation);
    }

    private String getFilename(AnnualTeachingCredits annualTeachingCredits) {
        return (annualTeachingCredits.getTeacher().getPerson().getIstUsername() + "_"
                + annualTeachingCredits.getAnnualCreditsState().getExecutionYear().getYear() + ".pdf").replaceAll(" ", "_")
                .replaceAll("/", "_");
    }

    @Deprecated
    public boolean hasHasConfidencialInformation() {
        return getHasConfidencialInformation() != null;
    }

    @Deprecated
    public boolean hasAnnualTeachingCredits() {
        return getAnnualTeachingCredits() != null;
    }

}
