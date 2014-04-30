package net.sourceforge.fenixedu.presentationTier.Action.phd.seminar.teacher;

import net.sourceforge.fenixedu.presentationTier.Action.phd.seminar.CommonPublicPresentationSeminarDA;
import net.sourceforge.fenixedu.presentationTier.Action.phd.teacher.PhdIndividualProgramProcessDA;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/publicPresentationSeminarProcess", module = "teacher", functionality = PhdIndividualProgramProcessDA.class)
@Forwards(@Forward(name = "uploadReport", path = "/phd/seminar/teacher/uploadReport.jsp"))
public class PublicPresentationSeminarProcessDA extends CommonPublicPresentationSeminarDA {

}
