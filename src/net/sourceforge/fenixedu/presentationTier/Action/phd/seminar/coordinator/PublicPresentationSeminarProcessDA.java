package net.sourceforge.fenixedu.presentationTier.Action.phd.seminar.coordinator;

import net.sourceforge.fenixedu.presentationTier.Action.phd.seminar.CommonPublicPresentationSeminarDA;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(path = "/publicPresentationSeminarProcess", module = "coordinator")
@Forwards(tileProperties = @Tile(navLocal = "/coordinator/localNavigationBar.jsp"), value = {

@Forward(name = "submitComission", path = "/phd/seminar/coordinator/submitComission.jsp"),

@Forward(name = "schedulePresentationDate", path = "/phd/seminar/coordinator/schedulePresentationDate.jsp")

})
public class PublicPresentationSeminarProcessDA extends CommonPublicPresentationSeminarDA {

}
