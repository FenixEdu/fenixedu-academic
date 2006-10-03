/*
 * Created on Jul 29, 2006
 *
 */
package net.sourceforge.fenixedu.presentationTier.TagLib.units;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

/**
 * @author mrsp
 * 
 */
public class UnitsTree extends TagSupport {

    private String initialUnit;

    private String unitParamName;

    private String state;

    private String path;

    /**
         * @return Returns the id.
         */
    public String getId() {
	return id;
    }

    /**
         * @param id
         *                The id to set.
         */
    public void setId(String id) {
	this.id = id;
    }

    /**
         * @return Returns the initialUnit.
         */
    public String getInitialUnit() {
	return initialUnit;
    }

    /**
         * @param initialUnit
         *                The initialUnit to set.
         */
    public void setInitialUnit(String initialUnit) {
	this.initialUnit = initialUnit;
    }

    /**
         * @return Returns the path.
         */
    public String getPath() {
	return path;
    }

    /**
         * @param path
         *                The path to set.
         */
    public void setPath(String path) {
	this.path = path;
    }

    /**
         * @return Returns the unitParamName.
         */
    public String getUnitParamName() {
	return unitParamName;
    }

    /**
         * @param unitParamName
         *                The unitParamName to set.
         */
    public void setUnitParamName(String unitParamName) {
	this.unitParamName = unitParamName;
    }

    /**
         * @return Returns the state.
         */
    public String getState() {
	return state;
    }

    /**
         * @param state
         *                The state to set.
         */
    public void setState(String state) {
	this.state = state;
    }

    public int doStartTag() throws JspException {
	String tree = writeInstitutionTree();
	try {
	    pageContext.getOut().print(tree);
	} catch (IOException e) {
	    e.printStackTrace(System.out);
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

    private void getSubUnitsList(Unit parentUnit, Unit parentUnitParent, StringBuilder buffer,
	    YearMonthDay currentDate, String paramName, String path) {

	buffer.append("<li>");

	HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
	List<Unit> subUnits = getUnitSubUnits(parentUnit, currentDate);
	if (!subUnits.isEmpty()) {
	    putImage(parentUnit, parentUnitParent, buffer, request);
	}

	buffer.append("<a href=\"").append(request.getContextPath()).append(path).append("&").append(
		paramName).append("=").append(parentUnit.getIdInternal()).append("\">").append(
		parentUnit.getName()).append("</a>").append("</li>");

	if (!subUnits.isEmpty()) {
	    buffer.append("<ul class='mvert0 nobullet' id=\"").append("aa").append(
		    parentUnit.getIdInternal()).append(
		    (parentUnitParent != null) ? parentUnitParent.getIdInternal() : "").append("\" ")
		    .append("style='display:none'>\r\n");

	    Collections.sort(subUnits, Unit.UNIT_COMPARATOR_BY_NAME);
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
	if (!StringUtils.isEmpty(this.getState()) && this.getState().equalsIgnoreCase("true")) {
	    return new ArrayList(parentUnit.getActiveSubUnits(currentDate, accountabilityEnums));
	} else if (!StringUtils.isEmpty(this.getState()) && this.getState().equalsIgnoreCase("false")) {
	    return new ArrayList(parentUnit.getInactiveSubUnits(currentDate, accountabilityEnums));
	} else {
	    return new ArrayList(parentUnit.getSubUnits(accountabilityEnums));
	}
    }

    private void putImage(Unit parentUnit, Unit parentUnitParent, StringBuilder buffer,
	    HttpServletRequest request) {
	buffer.append("<img ").append("src='").append(request.getContextPath()).append(
		"/images/toggle_plus10.gif' id=\"").append(parentUnit.getIdInternal()).append(
		(parentUnitParent != null) ? parentUnitParent.getIdInternal() : "").append("\" ")
		.append("indexed='true' onClick=\"").append("check(document.getElementById('").append(
			"aa").append(parentUnit.getIdInternal()).append(
			(parentUnitParent != null) ? parentUnitParent.getIdInternal() : "").append(
			"'),document.getElementById('").append(parentUnit.getIdInternal()).append(
			(parentUnitParent != null) ? parentUnitParent.getIdInternal() : "").append(
			"'));return false;").append("\"> ");
    }

}
