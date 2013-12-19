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

    @Mapping(path = "/naoAutorizado", parameter = "/error.jsp")
    public static class NaoAutorizado extends ForwardAction {
    }

    @Mapping(path = "/naoExecutado", parameter = "/error.jsp")
    public static class NaoExecutado extends ForwardAction {
    }

    @Mapping(path = "/notAuthorized", parameter = "/fenixLayout_error.jsp")
    public static class NotAuthorized extends ForwardAction {
    }

    @Mapping(path = "/naoExistente", parameter = "/error.jsp")
    public static class NaoExistente extends ForwardAction {
    }

    @Mapping(path = "/nonExistingObject", parameter = "/nonExistingError.jsp")
    public static class NonExistingObject extends ForwardAction {
    }

    @Mapping(path = "/publicNotAuthorized", parameter = "/exception/publicNotAuthorized.jsp")
    public static class PublicNotAuthorized extends ForwardAction {
    }

    @Mapping(path = "/userDoesNotExistOrIsInactive", parameter = "/userDoesNotExistOrIsInactive.jsp")
    public static class UserDoesNotExistOrIsInactive extends ForwardAction {
    }

    @Mapping(path = "/alumniReminder", parameter = "/showAlumniDataReminder.jsp")
    public static class AlumniReminder extends ForwardAction {
    }

}
