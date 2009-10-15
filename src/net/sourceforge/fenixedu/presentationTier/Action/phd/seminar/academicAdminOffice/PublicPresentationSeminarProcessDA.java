package net.sourceforge.fenixedu.presentationTier.Action.phd.seminar.academicAdminOffice;

import net.sourceforge.fenixedu.presentationTier.Action.phd.seminar.CommonPublicPresentationSeminarDA;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/publicPresentationSeminarProcess", module = "academicAdminOffice")
@Forwards( {

@Forward(name = "submitComission", path = "/phd/seminar/academicAdminOffice/submitComission.jsp"),

@Forward(name = "validateComission", path = "/phd/seminar/academicAdminOffice/validateComission.jsp"),

@Forward(name = "schedulePresentationDate", path = "/phd/seminar/academicAdminOffice/schedulePresentationDate.jsp"),

@Forward(name = "uploadReport", path = "/phd/seminar/academicAdminOffice/uploadReport.jsp"),

@Forward(name = "validateReport", path = "/phd/seminar/academicAdminOffice/validateReport.jsp")

})
public class PublicPresentationSeminarProcessDA extends CommonPublicPresentationSeminarDA {

}
