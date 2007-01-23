package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

/**
 * This exception is used to signal when the user tries to add a file but the
 * file size exceeds the site quota.
 *
 * @see Site#getQuota()
 * 
 * @author cfgi
 */
public class SiteFileQuotaExceededException extends DomainException {

    private static final long serialVersionUID = 1L;
    
    private static String MESSAGE = "site.quota.exceeded";
    
    public SiteFileQuotaExceededException(Site site, int size) {
        super(MESSAGE, getSizeValue(site.getQuota()));
    }

    private static String getSizeValue(long quota) {
        return String.format("%.2f MB", quota / 1024.0 / 1024.0);
    }

}
