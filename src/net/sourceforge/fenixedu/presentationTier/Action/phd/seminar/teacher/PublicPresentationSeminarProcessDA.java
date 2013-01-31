package net.sourceforge.fenixedu.presentationTier.Action.phd.seminar.teacher;

import net.sourceforge.fenixedu.presentationTier.Action.phd.seminar.CommonPublicPresentationSeminarDA;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(path = "/publicPresentationSeminarProcess", module = "teacher")
@Forwards(tileProperties = @Tile(navLocal = "/teacher/commons/navigationBarIndex.jsp"), value = {

@Forward(name = "uploadReport", path = "/phd/seminar/teacher/uploadReport.jsp")

})
public class PublicPresentationSeminarProcessDA extends CommonPublicPresentationSeminarDA {

}
