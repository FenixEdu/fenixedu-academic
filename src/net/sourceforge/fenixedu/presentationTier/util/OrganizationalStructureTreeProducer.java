/**
 * Author : Goncalo Luiz
 * Creation Date: Jun 26, 2006,4:59:15 PM
 */
package net.sourceforge.fenixedu.presentationTier.util;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.beanutils.BeanComparator;
import org.joda.time.YearMonthDay;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a><br>
 *         <br>
 *         Created on Jun 26, 2006,4:59:15 PM
 * 
 */
public class OrganizationalStructureTreeProducer {

    private String contextPath;

    private String urlToLink;

    public OrganizationalStructureTreeProducer(String contextPath, String urlToLink) {
	this.contextPath = contextPath;
	this.urlToLink = urlToLink;
    }

    public String getUnits() throws FenixFilterException, FenixServiceException, ExcepcaoPersistencia {
	final StringBuilder buffer = new StringBuilder();
	final Unit istUnit = RootDomainObject.getInstance().getInstitutionUnit();
	final YearMonthDay currentDate = new YearMonthDay();

	if (istUnit.isActive(currentDate) || !istUnit.getAllActiveSubUnits(currentDate).isEmpty()) {
	    getUnitTree(buffer, istUnit, istUnit.getActiveSubUnits(currentDate), currentDate, true);
	}
	return buffer.toString();
    }

    public void getUnitTree(StringBuilder buffer, Unit parentUnit, List<Unit> subUnits,
	    YearMonthDay currentDate, boolean active) {
	buffer.append("<ul class='padding1 nobullet'>");
	getUnitsList(parentUnit, subUnits, buffer, currentDate, active);
	buffer.append("</ul>");
    }

    private void getUnitsList(Unit parentUnit, List<Unit> subUnits, StringBuilder buffer,
	    YearMonthDay currentDate, boolean active) {

	openLITag(buffer);

	if (!subUnits.isEmpty()) {
	    putImage(parentUnit, buffer);
	}

	buffer.append("<a href=\"").append(this.contextPath).append(this.urlToLink).append("&keyUnit=")
		.append(parentUnit.getIdInternal()).append("\">").append(parentUnit.getName()).append(
			"</a>").append("</li>");

	if (!subUnits.isEmpty()) {
	    openULTag(parentUnit, buffer);
	}

	Collections.sort(subUnits, new BeanComparator("name"));

	for (Unit subUnit : subUnits) {
	    if (active) {
		getUnitsList(subUnit, subUnit.getActiveSubUnits(currentDate), buffer, currentDate,
			active);
	    } else {
		getUnitsList(subUnit, subUnit.getInactiveSubUnits(currentDate), buffer, currentDate,
			active);
	    }
	}

	if (parentUnit.hasAnySubUnits()) {
	    closeULTag(buffer);
	}
    }

    private void closeULTag(StringBuilder buffer) {
	buffer.append("</ul>");
    }

    private void openLITag(StringBuilder buffer) {
	buffer.append("<li>");
    }

    private void openULTag(Unit parentUnit, StringBuilder buffer) {
	buffer.append("<ul class='mvert0' id=\"").append("aa").append(parentUnit.getIdInternal())
		.append("\" ").append("style='display:none'>\r\n");
    }

    private void putImage(Unit parentUnit, StringBuilder buffer) {
	buffer.append("<img ").append("src='").append(this.contextPath).append(
		"/images/toggle_plus10.gif' id=\"").append(parentUnit.getIdInternal()).append("\" ")
		.append("indexed='true' onClick=\"").append("check(document.getElementById('").append(
			"aa").append(parentUnit.getIdInternal()).append("'),document.getElementById('")
		.append(parentUnit.getIdInternal()).append("'));return false;").append("\"> ");
    }
}
