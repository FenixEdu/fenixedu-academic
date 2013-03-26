package net.sourceforge.fenixedu.presentationTier.Action.phd.seminar.manager;

import net.sourceforge.fenixedu.presentationTier.Action.phd.seminar.academicAdminOffice.PublicPresentationSeminarProcessDA;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/publicPresentationSeminarProcess", module = "manager")
@Forwards({

@Forward(name = "manageStates", path = "/phd/seminar/manager/manageStates.jsp")

})
public class PublicPresentationSeminarProcessDAForManager extends PublicPresentationSeminarProcessDA {

}
