<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<h3><bean:message key="message.credits.masterDegree.title"/></h3>

<html:form action="/masterDegreeCreditsManagement">

	<html:hidden property="method" value="viewMasterDegreeCredits"/>
	
	<bean:message key="label.credits.choose.ExecutionYear"/>:<br/>	
	<html:select property="executionYearID" size="1" onchange="this.form.method.value='prepare';this.form.submit();">
		<html:options collection="executionYears" property="idInternal" labelProperty="year"/>
	</html:select>
	
	<br/><br/><br/>
	
	<bean:message key="label.credits.choose.masterDegreeCurricularPlan"/>: <br/>
	<html:select property="executionDegreeID" size="1" >
		<html:options collection="masterDegreeExecutions" property="idInternal" labelProperty="degreeCurricularPlan.name"/>
	</html:select>
	
	<br/><br/><br/>
	
	<html:submit styleClass="inputbutton">
		<bean:message key="submit"/>
	</html:submit>
</html:form>