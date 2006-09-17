/*
 * Created on 28/Ago/2005
 *
 */
package net.sourceforge.fenixedu.util;

/**
 * @author Ricardo Rodrigues
 * 
 */
public enum StudentPersonalDataAuthorizationChoice {

    PROFESSIONAL_ENDS, /*
                         * only to professional ends (job propositions,
                         * scholarships, internship, etc)
                         */
    SEVERAL_ENDS, /*
                     * several non comercial ends (biographic, recreational,
                     * cultural, etc)
                     */
    ALL_ENDS, /* all ends, including comercial ones */

    NO_END; /* doesn't authorize the use of the data */

    public String getName() {
        return name();
    }

    public String getQualifiedName() {
        return StudentPersonalDataAuthorizationChoice.class.getSimpleName() + "." + name();
    }

    public String getFullyQualifiedName() {
        return StudentPersonalDataAuthorizationChoice.class.getName() + "." + name();
    }
}