<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<h2><bean:message key="message.credits.masterDegree.title"/></h2>

<html:form action="/masterDegreeCreditsManagement">

	<html:hidden property="method" value="viewMasterDegreeCredits"/>
	
	<p class="mtop2"><bean:message key="label.credits.choose.ExecutionYear"/>:</p>
	<p>
	<html:select property="executionYearID" size="1" onchange="this.form.method.value='prepare';this.form.submit();">
		<html:options collection="executionYears" property="idInternal" labelProperty="year"/>
	</html:select>
	</p>
	
	<p class="mtop2"><bean:message key="label.credits.choose.masterDegreeCurricularPlan"/>:</p>
	<p>
	<html:select property="executionDegreeID" size="1" >
		<html:options collection="masterDegreeExecutions" property="idInternal" labelProperty="degreeCurricularPlan.name"/>
	</html:select>
	</p>
	
	<p class="mtop2">
	<html:submit styleClass="inputbutton">
		<bean:message key="visualize"/>
	</html:submit>
	</p>
</html:form>