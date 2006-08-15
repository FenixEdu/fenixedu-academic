package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Locale;
import java.util.StringTokenizer;

import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.branch.BranchType;

/**
 * @author dcs-rjao
 * @author Fernanda Quitério
 * 
 * 19/Mar/2003
 */

public class InfoBranch extends InfoObject {

	private final Branch branch;

	private boolean showEnVersion = false;

    public InfoBranch(final Branch branch) {
    	this.branch = branch;
    }

    public String toString() {
    	return branch.toString();
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
            return new String("");
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
        return branch.getCode();
    }

    public String getName() {
    	return showEnVersion && branch.getNameEn() != null && branch.getNameEn().length() > 0 ?
    			branch.getNameEn() : branch.getName();
    }

    public InfoDegreeCurricularPlan getInfoDegreeCurricularPlan() {
    	return InfoDegreeCurricularPlan.newInfoFromDomain(branch.getDegreeCurricularPlan());
    }

    public String getAcronym() {
        return branch.getAcronym();
    }

    public Integer getSecondaryCredits() {
        return branch.getSecondaryCredits();
    }

    public Integer getSpecializationCredits() {
        return branch.getSpecializationCredits();
    }

    public BranchType getBranchType() {
        return branch.getBranchType();
    }

    public static InfoBranch newInfoFromDomain(Branch branch) {
    	return branch == null ? null : new InfoBranch(branch);
    }

    public String getNameEn() {
        return branch.getNameEn();
    }

    public void prepareEnglishPresentation(Locale locale) {
        if (locale.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
        	showEnVersion = true;
        }
    }

	@Override
	public Integer getIdInternal() {
		return branch.getIdInternal();
	}

    @Override
    public void setIdInternal(Integer integer) {
        throw new Error("Method should not be called!");
    }

}
