package net.sourceforge.fenixedu.dataTransferObject;

import java.util.StringTokenizer;

import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.branch.BranchType;
import pt.utl.ist.fenix.tools.util.i18n.Language;

/**
 * @author dcs-rjao
 * @author Fernanda Quitério
 * 
 *         19/Mar/2003
 */

public class InfoBranch extends InfoObject {

    private final Branch branch;

    public Branch getBranch() {
        return branch;
    }

    private boolean showEnVersion = (Language.getUserLanguage() == Language.en);

    public InfoBranch(final Branch branch) {
        this.branch = branch;
    }

    @Override
    public String toString() {
        return getBranch().toString();
    }

    public Boolean representsCommonBranch() {
        return Boolean.valueOf(getName() != null && getName().equals("") && getCode() != null && getCode().equals(""));
    }

    /**
     * returns an empty string if there is no branch or branch initials in case
     * it exists
     */
    public String getPrettyCode() {
        if (representsCommonBranch().booleanValue()) {
            return "";
        }
        StringBuilder prettyCode = new StringBuilder();
        String namePart = null;
        StringTokenizer stringTokenizer = new StringTokenizer(getName(), " ");
        while (stringTokenizer.hasMoreTokens()) {
            namePart = stringTokenizer.nextToken();
            if (!namePart.equalsIgnoreCase("RAMO") && namePart.length() > 2) {
                prettyCode = prettyCode.append(namePart.substring(0, 1));
            }
        }
        return prettyCode.toString();
    }

    public String getCode() {
        return getBranch().getCode();
    }

    public String getName() {
        return showEnVersion && getBranch().getNameEn() != null && getBranch().getNameEn().length() > 0 ? getBranch().getNameEn() : getBranch()
                .getName();
    }

    public InfoDegreeCurricularPlan getInfoDegreeCurricularPlan() {
        return InfoDegreeCurricularPlan.newInfoFromDomain(getBranch().getDegreeCurricularPlan());
    }

    public String getAcronym() {
        return getBranch().getAcronym();
    }

    public Integer getSecondaryCredits() {
        return getBranch().getSecondaryCredits();
    }

    public Integer getSpecializationCredits() {
        return getBranch().getSpecializationCredits();
    }

    public BranchType getBranchType() {
        return getBranch().getBranchType();
    }

    public static InfoBranch newInfoFromDomain(Branch branch) {
        return branch == null ? null : new InfoBranch(branch);
    }

    public String getNameEn() {
        return getBranch().getNameEn();
    }

    @Override
    public Integer getExternalId() {
        return getBranch().getExternalId();
    }

    @Override
    public void setExternalId(Integer integer) {
        throw new Error("Method should not be called!");
    }

}
