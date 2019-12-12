package org.fenixedu.academic.domain.curriculum.grade;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.LocalizedString;

public class GradeScaleEntry extends GradeScaleEntry_Base {

    public static final Comparator<GradeScaleEntry> COMPARE_BY_GRADE_ORDER = (o1, o2) -> {
        int c = Integer.compare(o1.getGradeOrder(), o2.getGradeOrder());

        return c != 0 ? c : o1.getExternalId().compareTo(o2.getExternalId());
    };

    public GradeScaleEntry() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    protected GradeScaleEntry(final GradeScale gradeScaleInstance, 
            final String value, final LocalizedString description, 
            final boolean allowsApproval) {
        this();
        
        setGradeScale(gradeScaleInstance);
        setValue(value);
        setDescription(description);
        setAllowsApproval(allowsApproval);
        
        if(isAllowsApproval()) {
            // The new approval grade entry can be ordered at top
            int newGradeOrder = gradeScaleInstance.getGradeScaleEntriesSet().stream().max(COMPARE_BY_GRADE_ORDER).get().getGradeOrder() + 1;
            setGradeOrder(newGradeOrder);
        } else {
            // Check the minimum aproved grade
            Optional<GradeScaleEntry> lowestApprovalGrade= gradeScaleInstance.getGradeScaleEntriesSet().stream()
                .filter(GradeScaleEntry::isAllowsApproval).findFirst();
            
            if(lowestApprovalGrade.isPresent()) {
                int newGradeOrder = lowestApprovalGrade.get().getGradeOrder();

                lowestApprovalGrade.get().setGradeOrder(newGradeOrder + 1);
                
                int nextOrder = newGradeOrder + 2;
                for (GradeScaleEntry gradeScaleEntry : lowestApprovalGrade.get().getHigherGradeEntries()) {
                    gradeScaleEntry.setGradeOrder(nextOrder);
                    nextOrder++;
                }
                
                setGradeOrder(newGradeOrder);
            } else {
                int newGradeOrder = gradeScaleInstance.getGradeScaleEntriesSet().stream().max(COMPARE_BY_GRADE_ORDER).get().getGradeOrder() + 1;
                setGradeOrder(newGradeOrder);
            }
        }
        
        
        checkRules();
    }
    
    private void checkRules() {
        if (getGradeScale() == null) {
            throw new DomainException("error.GradeScaleEntry.gradeScale.is.required");
        }

        if (StringUtils.isBlank(getValue())) {
            throw new DomainException("error.GradeScaleEntry.value.is.required");
        }

        if (getDescription() == null || getDescription().isEmpty()) {
            throw new DomainException("error.GradeScaleEntry.description.is.required");
        }
        
        if(find(getGradeScale(), getValue()).count() >= 2) {
            throw new DomainException("error.GradeScaleEntry.already.registered.in.scale");
        }
        
    }

    public void edit(final LocalizedString description) {
        super.setDescription(description);

        checkRules();
    }

    public boolean isAllowsApproval() {
        return getAllowsApproval();
    }
    
    void delete() {
        setRootDomainObject(null);
        setGradeScale(null);

        super.deleteDomainObject();
    }

    private List<GradeScaleEntry> getHigherGradeEntries() {
        final List<GradeScaleEntry> gradeEntriesList = new ArrayList<>(getGradeScale().getGradeScaleEntriesSet());
        gradeEntriesList.sort(COMPARE_BY_GRADE_ORDER);

        int indexOf = gradeEntriesList.indexOf(this);
        return indexOf >= 0 && indexOf < (gradeEntriesList.size() - 1) ? gradeEntriesList.subList(indexOf + 1,
                gradeEntriesList.size()) : Collections.emptyList();
    }
    
    public boolean isLast() {
        final List<GradeScaleEntry> orderedGradeScaleEntries = getGradeScale().getOrderedGradeScaleEntriesList();
        return getGradeScale().getOrderedGradeScaleEntriesList().indexOf(this) == orderedGradeScaleEntries.size() - 1;
    }
    
    public boolean isFirst() {
        final List<GradeScaleEntry> orderedGradeScaleEntries = getGradeScale().getOrderedGradeScaleEntriesList();
        return orderedGradeScaleEntries.indexOf(this) == 0;
    }

    // ########
    // SERVICES
    // ########

    static GradeScaleEntry create(final GradeScale gradeScaleInstance, 
            final String value,
            final LocalizedString description, 
            final boolean allowsApproval) {
        final GradeScaleEntry result = new GradeScaleEntry(gradeScaleInstance, value, description, allowsApproval);
        
        return result;
    }

    public static Stream<GradeScaleEntry> findAll() {
        return Bennu.getInstance().getGradeScaleEntriesSet().stream();
    }

    public static Stream<GradeScaleEntry> find(GradeScale gradeScale) {
        return gradeScale.getGradeScaleEntriesSet().stream();
    }

    public static Stream<GradeScaleEntry> find(GradeScale gradeScale, final String value) {
        return find(gradeScale).filter(e -> e.getValue().equals(value));
    }
    
}
