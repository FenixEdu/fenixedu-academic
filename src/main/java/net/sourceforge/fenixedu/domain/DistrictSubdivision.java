package net.sourceforge.fenixedu.domain;

import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;

import org.fenixedu.bennu.core.domain.Bennu;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.StringUtils;
import pt.utl.ist.fenix.tools.util.StringNormalizer;

//TODO: Refactor remaining object to use district subdivision instead of strings
public class DistrictSubdivision extends DistrictSubdivision_Base {

    public static Comparator<DistrictSubdivision> COMPARATOR_BY_NAME = new Comparator<DistrictSubdivision>() {
        @Override
        public int compare(DistrictSubdivision leftDistrictSubdivision, DistrictSubdivision rightDistrictSubdivision) {
            int comparationResult = leftDistrictSubdivision.getName().compareTo(rightDistrictSubdivision.getName());
            return (comparationResult == 0) ? leftDistrictSubdivision.getExternalId().compareTo(
                    rightDistrictSubdivision.getExternalId()) : comparationResult;
        }
    };

    private DistrictSubdivision() {
        super();
        super.setRootDomainObject(Bennu.getInstance());
    }

    public DistrictSubdivision(final String code, final String name, final District district) {
        this();
        init(code, name, district);
    }

    private void init(final String code, final String name, final District district) {
        checkParameters(code, name, district);

        super.setCode(code);
        super.setName(name);
        super.setDistrict(district);
    }

    private void checkParameters(String code, String name, District district) {

        if (code == null) {
            throw new DomainException("error.net.sourceforge.fenixedu.domain.DistrictSubdivision.code.cannot.be.null");
        }

        if (name == null) {
            throw new DomainException("error.net.sourceforge.fenixedu.domain.DistrictSubdivision.name.cannot.be.null");
        }

        if (district == null) {
            throw new DomainException("error.net.sourceforge.fenixedu.domain.DistrictSubdivision.district.cannot.be.null");
        }

    }

    static public Collection<DistrictSubdivision> findByName(String name, int size) {
        String normalizedName = StringNormalizer.normalize(name).toLowerCase();
        Collection<DistrictSubdivision> result = new TreeSet<DistrictSubdivision>(COMPARATOR_BY_NAME);

        for (DistrictSubdivision districtSubdivision : Bennu.getInstance().getDistrictSubdivisionsSet()) {
            if (StringNormalizer.normalize(districtSubdivision.getName()).toLowerCase().contains(normalizedName)) {
                result.add(districtSubdivision);
                if (result.size() >= size) {
                    break;
                }
            }
        }

        return result;
    }

    static public DistrictSubdivision readByCode(final String code) {
        DistrictSubdivision result = null;

        if (!StringUtils.isEmpty(code)) {
            for (final DistrictSubdivision iter : Bennu.getInstance().getDistrictSubdivisionsSet()) {
                if (iter.getCode().equalsIgnoreCase(code)) {
                    result = iter;
                }
            }
        }

        return result;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.PersonalIngressionData> getPersonalIngressionsData() {
        return getPersonalIngressionsDataSet();
    }

    @Deprecated
    public boolean hasAnyPersonalIngressionsData() {
        return !getPersonalIngressionsDataSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.PersonalIngressionData> getSchoolTimePersonalIngressionsData() {
        return getSchoolTimePersonalIngressionsDataSet();
    }

    @Deprecated
    public boolean hasAnySchoolTimePersonalIngressionsData() {
        return !getSchoolTimePersonalIngressionsDataSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacy> getSchoolTimeIndividualCandidacies() {
        return getSchoolTimeIndividualCandidaciesSet();
    }

    @Deprecated
    public boolean hasAnySchoolTimeIndividualCandidacies() {
        return !getSchoolTimeIndividualCandidaciesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.candidacy.StudentCandidacy> getSchoolTimeStudentCandidacies() {
        return getSchoolTimeStudentCandidaciesSet();
    }

    @Deprecated
    public boolean hasAnySchoolTimeStudentCandidacies() {
        return !getSchoolTimeStudentCandidaciesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacy> getIndividualCandidacies() {
        return getIndividualCandidaciesSet();
    }

    @Deprecated
    public boolean hasAnyIndividualCandidacies() {
        return !getIndividualCandidaciesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.candidacy.StudentCandidacy> getStudentCandidacies() {
        return getStudentCandidaciesSet();
    }

    @Deprecated
    public boolean hasAnyStudentCandidacies() {
        return !getStudentCandidaciesSet().isEmpty();
    }

    @Deprecated
    public boolean hasName() {
        return getName() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasDistrict() {
        return getDistrict() != null;
    }

    @Deprecated
    public boolean hasCode() {
        return getCode() != null;
    }

}
