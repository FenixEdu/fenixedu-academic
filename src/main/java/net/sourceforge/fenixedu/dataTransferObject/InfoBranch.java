/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.StringTokenizer;

import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.branch.BranchType;

import org.fenixedu.commons.i18n.I18N;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

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

    private final boolean showEnVersion = I18N.getLocale().equals(MultiLanguageString.en);

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
    public String getExternalId() {
        return getBranch().getExternalId();
    }

    @Override
    public void setExternalId(String integer) {
        throw new Error("Method should not be called!");
    }

}
