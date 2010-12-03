<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID" />
<h2><bean:message bundle="COORDINATOR_RESOURCES" key="title.analysisByExecutionYears" /></h2>

<div class="infoop2">
	<bean:message key="label.coordinator.analyticTools.disclaimer1" bundle="COORDINATOR_RESOURCES"/><br/><br/>
	<bean:message key="label.coordinator.analyticTools.disclaimer2" bundle="COORDINATOR_RESOURCES"/><br/><br/>
</div>
<html:link page="<%= "/xYear.do?method=showYearInformation&degreeCurricularPlanID=" + degreeCurricularPlanID.toString() %>" ><bean:message key="link.continue" bundle="COORDINATOR_RESOURCES"/></html:link>