package net.sourceforge.fenixedu.domain;

import java.util.Comparator;

import net.sourceforge.fenixedu.util.BundleUtil;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.collections.comparators.NullComparator;
import org.fenixedu.bennu.core.domain.Bennu;

public class BibliographicReference extends BibliographicReference_Base {

    public static final Comparator<BibliographicReference> COMPARATOR_BY_ORDER = new Comparator<BibliographicReference>() {

        private ComparatorChain chain = null;

        @Override
        public int compare(BibliographicReference one, BibliographicReference other) {
            if (this.chain == null) {
                chain = new ComparatorChain();

                chain.addComparator(new BeanComparator("referenceOrder", new NullComparator(true)));
                chain.addComparator(new BeanComparator("title"));
                chain.addComparator(new BeanComparator("year"));
                chain.addComparator(DomainObjectUtil.COMPARATOR_BY_ID);
            }

            return chain.compare(one, other);
        }
    };

    public BibliographicReference() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public void edit(final String title, final String authors, final String reference, final String year, final Boolean optional) {
        if (title == null || authors == null || reference == null || year == null || optional == null) {
            throw new NullPointerException();
        }

        setTitle(title);
        setAuthors(authors);
        setReference(reference);
        setYear(year);
        setOptional(optional);
        ExecutionCourse executionCourse = getExecutionCourse();
        final String type;
        if (optional) {
            type =
                    BundleUtil.getStringFromResourceBundle("resources.ApplicationResources",
                            "option.bibliographicReference.optional");
        } else {
            type =
                    BundleUtil.getStringFromResourceBundle("resources.ApplicationResources",
                            "option.bibliographicReference.recommended");
        }
        CurricularManagementLog.createLog(executionCourse, "resources.MessagingResources",
                "log.executionCourse.curricular.bibliographic.edited", type, title, executionCourse.getName(),
                executionCourse.getDegreePresentationString());
    }

    public void delete() {
        ExecutionCourse executionCourse = getExecutionCourse();
        String blBibliographicReference = getTitle();
        Boolean optional = getOptional();

        final String type;
        if (optional) {
            type =
                    BundleUtil.getStringFromResourceBundle("resources.ApplicationResources",
                            "option.bibliographicReference.optional");
        } else {
            type =
                    BundleUtil.getStringFromResourceBundle("resources.ApplicationResources",
                            "option.bibliographicReference.recommended");
        }

        CurricularManagementLog.createLog(executionCourse, "resources.MessagingResources",
                "log.executionCourse.curricular.bibliographic.removed", type, blBibliographicReference,
                executionCourse.getName(), executionCourse.getDegreePresentationString());

        setExecutionCourse(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    public boolean isOptional() {
        return getOptional() == null || getOptional();
    }

    @Deprecated
    public boolean hasExecutionCourse() {
        return getExecutionCourse() != null;
    }

    @Deprecated
    public boolean hasYear() {
        return getYear() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasReference() {
        return getReference() != null;
    }

    @Deprecated
    public boolean hasAuthors() {
        return getAuthors() != null;
    }

    @Deprecated
    public boolean hasReferenceOrder() {
        return getReferenceOrder() != null;
    }

    @Deprecated
    public boolean hasTitle() {
        return getTitle() != null;
    }

    @Deprecated
    public boolean hasOptional() {
        return getOptional() != null;
    }

}
