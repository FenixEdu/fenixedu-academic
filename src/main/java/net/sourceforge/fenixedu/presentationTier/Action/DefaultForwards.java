package net.sourceforge.fenixedu.presentationTier.Action;

import org.apache.struts.actions.ForwardAction;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * Contains General forwards, previously in struts-default.xml
 * 
 * Use Struts's {@link ForwardAction}, which takes the {@link Mapping}s parameter as the destination location.
 */
public final class DefaultForwards {

    @Mapping(path = "/showErrorPageRegistered", parameter = "/exception/errorRegistered.jsp")
    public static class ShowErrorPageRegistered extends ForwardAction {
    }

    @Mapping(path = "/nonExistingObject", parameter = "/nonExistingError.jsp")
    public static class NonExistingObject extends ForwardAction {
    }

    @Mapping(path = "/publicNotAuthorized", parameter = "/exception/publicNotAuthorized.jsp")
    public static class PublicNotAuthorized extends ForwardAction {
    }

    @Mapping(path = "/alumniReminder", parameter = "/showAlumniDataReminder.jsp")
    public static class AlumniReminder extends ForwardAction {
    }

    @Mapping(path = "/notFound", module = "publico", parameter = "/notFound.jsp")
    public static class NotFoundAction extends ForwardAction {
    }

    @Mapping(path = "/fenixEduIndex", parameter = "/fenixEduIndex.jsp")
    public static class FenixEduIndexAction extends ForwardAction {
    }

}
