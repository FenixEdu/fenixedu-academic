/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on Jul 29, 2006
 *
 */
package org.fenixedu.academic.servlet.taglib;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.organizationalStructure.AccountabilityTypeEnum;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.joda.time.YearMonthDay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mrsp
 * 
 */
public class UnitsTree extends TagSupport {

    private static final Logger logger = LoggerFactory.getLogger(UnitsTree.class);

    private String initialUnit;

    private String unitParamName;

    private String state;

    private String path;

    private String expanded;

    @Override
    public int doStartTag() throws JspException {
        String tree = writeInstitutionTree();
        try {
            pageContext.getOut().print(tree);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return SKIP_BODY;
    }

    public String writeInstitutionTree() {

        StringBuilder buffer = new StringBuilder();
        Unit unit = (Unit) pageContext.findAttribute(this.getInitialUnit());
        YearMonthDay currentDate = new YearMonthDay();

        buffer.append("<ul class='padding1 nobullet'>");
        getSubUnitsList(unit, null, buffer, currentDate, this.getUnitParamName(), this.getPath());
        buffer.append("</ul>");

        return buffer.toString();
    }

    private void getSubUnitsList(Unit parentUnit, Unit parentUnitParent, StringBuilder buffer, YearMonthDay currentDate,
            String paramName, String path) {

        buffer.append("<li>");

        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        List<Unit> subUnits = getUnitSubUnits(parentUnit, currentDate);
        if (!subUnits.isEmpty()) {
            putImage(parentUnit, parentUnitParent, buffer, request);
        }

        buffer.append("<a href=\"").append(request.getContextPath()).append(path).append("&").append(paramName).append("=")
                .append(parentUnit.getExternalId()).append("\">").append(parentUnit.getNameWithAcronym()).append("</a>")
                .append("</li>");

        if (!subUnits.isEmpty()) {
            buffer.append("<ul class='mvert0 nobullet' id=\"").append("aa").append(parentUnit.getExternalId())
                    .append((parentUnitParent != null) ? parentUnitParent.getExternalId() : "").append("\" ")
                    .append("style='display:" + (getExpanded() != null && Boolean.valueOf(getExpanded()) ? "block" : "none"))
                    .append("'>\r\n");

            Collections.sort(subUnits, Unit.COMPARATOR_BY_NAME_AND_ID);
        }

        for (Unit subUnit : subUnits) {
            getSubUnitsList(subUnit, parentUnit, buffer, currentDate, paramName, path);
        }

        if (!subUnits.isEmpty()) {
            buffer.append("</ul>");
        }
    }

    private List<Unit> getUnitSubUnits(Unit parentUnit, YearMonthDay currentDate) {

        List<AccountabilityTypeEnum> accountabilityEnums = new ArrayList<AccountabilityTypeEnum>();

        accountabilityEnums.add(AccountabilityTypeEnum.ORGANIZATIONAL_STRUCTURE);
        accountabilityEnums.add(AccountabilityTypeEnum.ACADEMIC_STRUCTURE);
        accountabilityEnums.add(AccountabilityTypeEnum.GEOGRAPHIC);

        if (!StringUtils.isEmpty(this.getState()) && this.getState().equalsIgnoreCase("true")) {
            return new ArrayList(getActiveSubUnits(parentUnit, currentDate, accountabilityEnums));
        } else if (!StringUtils.isEmpty(this.getState()) && this.getState().equalsIgnoreCase("false")) {
            return new ArrayList(getInactiveSubUnits(parentUnit, currentDate, accountabilityEnums));
        } else {
            return new ArrayList(parentUnit.getSubUnits(accountabilityEnums));
        }
    }

    private static List<Unit> getActiveSubUnits(Unit unit, YearMonthDay currentDate,
            List<AccountabilityTypeEnum> accountabilityTypeEnums) {
        return getSubUnitsByState(unit, currentDate, accountabilityTypeEnums, true);
    }

    private static List<Unit> getInactiveSubUnits(Unit unit, YearMonthDay currentDate,
            List<AccountabilityTypeEnum> accountabilityTypeEnums) {
        return getSubUnitsByState(unit, currentDate, accountabilityTypeEnums, false);
    }

    private static List<Unit> getSubUnitsByState(Unit unit, YearMonthDay currentDate,
            List<AccountabilityTypeEnum> accountabilityTypeEnums, boolean state) {
        List<Unit> allSubUnits = new ArrayList<Unit>();
        for (Unit subUnit : unit.getSubUnits(accountabilityTypeEnums)) {
            if (subUnit.isActive(currentDate) == state) {
                allSubUnits.add(subUnit);
            }
        }
        return allSubUnits;
    }

    private void putImage(Unit parentUnit, Unit parentUnitParent, StringBuilder buffer, HttpServletRequest request) {
        buffer.append("<img ").append("src='").append(request.getContextPath())
                .append((getExpanded() != null
                        && Boolean.valueOf(getExpanded()) ? "/images/toggle_minus10.gif" : "/images/toggle_plus10.gif"))
                .append("' id=\"").append(parentUnit.getExternalId())
                .append((parentUnitParent != null) ? parentUnitParent.getExternalId() : "").append("\" ")
                .append("indexed='true' onClick=\"").append("check(document.getElementById('").append("aa")
                .append(parentUnit.getExternalId()).append((parentUnitParent != null) ? parentUnitParent.getExternalId() : "")
                .append("'),document.getElementById('").append(parentUnit.getExternalId())
                .append((parentUnitParent != null) ? parentUnitParent.getExternalId() : "").append("'));return false;")
                .append("\"> ");
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getInitialUnit() {
        return initialUnit;
    }

    public void setInitialUnit(String initialUnit) {
        this.initialUnit = initialUnit;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUnitParamName() {
        return unitParamName;
    }

    public void setUnitParamName(String unitParamName) {
        this.unitParamName = unitParamName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getExpanded() {
        return expanded;
    }

    public void setExpanded(String expanded) {
        this.expanded = expanded;
    }
}
