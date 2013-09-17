<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<html:xhtml/>

<em><bean:message key="label.alumni" bundle="GEP_RESOURCES"/></em>
<h2><bean:message key="title.alumni.recipients.add" bundle="GEP_RESOURCES"/></h2>

<logic:present name="errorMessage">
	<p>
		<span class="error0"><bean:write name="errorMessage"/></span>
	</p>
</logic:present>

<fr:form action="/alumni.do">	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" name="alumniForm" value="addRecipients"/>	
	
	<p><bean:message key="message.alumni.recipients.chooseGroup" bundle="GEP_RESOURCES"/>:</p>				
	<p class=" mtop15 mbottom05"><b><bean:message key="label.alumni.studentGroups" bundle="GEP_RESOURCES"/></b></p>

	<fr:edit id="createRecipient" name="createRecipient" schema="alumni.gep.email.recipient.create">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thright thlight mtop05 ulnomargin"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
		<fr:destination name="degreeTypePostback" path="/alumni.do?method=selectDegreeType"/>	
		<fr:destination name="input" path="/alumni.do?method=prepareAddRecipients"/>	
	</fr:edit>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="button.submit" bundle="APPLICATION_RESOURCES"/>
	</html:submit>
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" styleClass="inputbutton" onclick="this.form.method.value='manageRecipients'">
		<bean:message key="button.cancel" bundle="APPLICATION_RESOURCES"/>
	</html:cancel>	
</fr:form>

<fr:form action="/alumni.do">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" name="alumniForm" value="addNotUpdatedInfoRecipients"/>	

	<p class="mtop3 mbottom05"><b><bean:message key="label.alumni.alumniGroups" bundle="GEP_RESOURCES"/></b></p>

	<html:messages id="message" message="true" bundle="GEP_RESOURCES">
		<p><span class="error"><!-- Error messages go here --><bean:write name="message"/></span></p>
	</html:messages>


	<fr:edit id="notUpdatedInfoRecipient" name="notUpdatedInfoRecipient" schema="alumni.gep.email.recipient.infoNotUpdated.create">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thright thlight mtop05 ulnomargin"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
		<fr:destination name="input" path="/alumni.do?method=prepareAddRecipients"/>	
	</fr:edit>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="button.submit" bundle="APPLICATION_RESOURCES"/>
	</html:submit>
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" styleClass="inputbutton" onclick="this.form.method.value='manageRecipients'">
		<bean:message key="button.cancel" bundle="APPLICATION_RESOURCES"/>
	</html:cancel>	
</fr:form>