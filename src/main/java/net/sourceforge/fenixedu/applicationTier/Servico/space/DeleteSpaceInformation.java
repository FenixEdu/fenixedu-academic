package net.sourceforge.fenixedu.applicationTier.Servico.space;


import net.sourceforge.fenixedu.domain.space.SpaceInformation;
import pt.ist.fenixframework.Atomic;

public class DeleteSpaceInformation {

    @Atomic
    public static void run(final SpaceInformation spaceInformation) {
        if (spaceInformation != null) {
            spaceInformation.delete();
        }
    }

}