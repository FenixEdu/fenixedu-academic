package net.sourceforge.fenixedu.presentationTier.servlets;

/**
 * 
 * @author João Cachopo
 *
 */
public class DomainObjectsExternalDescriptions {

    public static String getShortDescription(net.sourceforge.fenixedu.domain.messaging.Announcement ann) {
        return ann.getSubject().getContent();
    }

}
