<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em><bean:message key="pedagogical.council" bundle="PEDAGOGICAL_COUNCIL" /></em>
<h2><bean:message key="label.tutorshipSummaryPeriod" bundle="APPLICATION_RESOURCES" /></h2>

<fr:messages />

<logic:notPresent name="periodBean" property="executionSemester">
	<fr:form action="/tutorshipSummaryPeriod.do">
		<html:hidden property="method" value="prepareCreate2" />

	    <fr:edit id="periodBean1" name="periodBean" schema="tutorship.summary.period">
			<fr:layout name="tabular" >
				<fr:property name="classes" value="tstyle5 thlight thmiddle thright"/>
				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			</fr:layout>
	    </fr:edit>
	    
	    <fr:edit id="periodBean" name="periodBean" visible="false" />

   		<html:submit><bean:message key="button.forward" bundle="APPLICATION_RESOURCES" /></html:submit>
	</fr:form>
</logic:notPresent>

<logic:present name="periodBean" property="executionSemester">
	<fr:form action="/tutorshipSummaryPeriod.do">
		<html:hidden property="method" value="create" />
    
    	<fr:edit id="periodBean2" name="periodBean" schema="tutorship.summary.period-full">
			<fr:layout name="tabular" >
				<fr:property name="classes" value="tstyle5 thlight thmiddle thright"/>
				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			</fr:layout>
	    </fr:edit>
	    
	    <fr:edit id="periodBean" name="periodBean" visible="false" />
	    
		<html:submit onclick="this.form.method.value='prepareCreate';this.form.submit();"><bean:message key="button.back" bundle="APPLICATION_RESOURCES"/></html:submit>
	    <html:submit><bean:message key="button.confirm" bundle="APPLICATION_RESOURCES" /></html:submit>
	</fr:form>
</logic:present>
