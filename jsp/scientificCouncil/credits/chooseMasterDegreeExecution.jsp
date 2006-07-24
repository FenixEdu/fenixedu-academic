<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<h2><bean:message key="message.credits.masterDegree.title"/></h2>

<html:form action="/masterDegreeCreditsManagement">

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="viewMasterDegreeCredits"/>
	
	<p class="mtop2"><bean:message key="label.credits.choose.ExecutionYear"/>:</p>
	<p>
		<html:select bundle="HTMLALT_RESOURCES" altKey="select.executionYearID" property="executionYearID" size="1" onchange="this.form.method.value='prepare';this.form.submit();">
			<html:options collection="executionYears" property="idInternal" labelProperty="year"/>
		</html:select>
		<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'>" escape="false"/>
	</p>
	
	<p class="mtop2"><bean:message key="label.credits.choose.masterDegreeCurricularPlan"/>:</p>
	<p>
	<html:select bundle="HTMLALT_RESOURCES" altKey="select.executionDegreeID" property="executionDegreeID" size="1" >
		<html:options collection="masterDegreeExecutions" property="idInternal" labelProperty="degreeCurricularPlan.name"/>
	</html:select>
	</p>
	
	<p class="mtop2">
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="visualize"/>
	</html:submit>
	</p>
</html:form>