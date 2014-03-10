package net.sourceforge.fenixedu.presentationTier.Action.phd.seminar.coordinator;

import net.sourceforge.fenixedu.presentationTier.Action.phd.coordinator.PhdIndividualProgramProcessDA;
import net.sourceforge.fenixedu.presentationTier.Action.phd.seminar.CommonPublicPresentationSeminarDA;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/publicPresentationSeminarProcess", module = "coordinator", functionality = PhdIndividualProgramProcessDA.class)
@Forwards({ @Forward(name = "submitComission", path = "/phd/seminar/coordinator/submitComission.jsp"),
        @Forward(name = "schedulePresentationDate", path = "/phd/seminar/coordinator/schedulePresentationDate.jsp") })
public class PublicPresentationSeminarProcessDA extends CommonPublicPresentationSeminarDA {

}
