<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2><bean:message key="label.autoEvaluation"/></h2>

<fr:form action="/teacherExpectationAutoAvaliation.do?method=show">
<bean:message key="label.common.chooseExecutionYear"/>: <fr:edit id="executionYear" name="bean" slot="executionYear"> 
		<fr:layout name="menu-select-postback">
			<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.OpenExecutionYearsProvider"/>
			<fr:property name="format" value="${year}"/>
			<fr:destination name="postback" path="/teacherExpectationAutoAvaliation.do?method=show"/>
		</fr:layout>
	</fr:edit>	
</fr:form>

<p>
<logic:present name="expectation">
	<logic:notEmpty name="expectation" property="autoEvaluation">
		<strong><bean:message key="label.autoEvaluation"/></strong>: <p>
		<fr:view name="expectation" property="autoEvaluation" layout="html"/>
		</p>
	</logic:notEmpty> 
	<logic:empty name="expectation" property="autoEvaluation">
    	<bean:message key="label.noAutoEvaluationsForYear" />
	</logic:empty>
	<p>
	<logic:equal name="expectation" property="allowedToEditAutoEvaluation" value="true">
		<bean:define id="executionYearId" name="bean" property="executionYear.idInternal"/>
		<html:link page="<%= "/teacherExpectationAutoAvaliation.do?method=prepareEdit&amp;executionYearId=" + executionYearId%>">
			<bean:message key="label.edit" />
		</html:link>
	</logic:equal>
	</p>
</logic:present>

<logic:notPresent name="expectation"> 
		<bean:message key="label.personalExpectationsManagement.noExpectationsDefined" />
</logic:notPresent>
</p>