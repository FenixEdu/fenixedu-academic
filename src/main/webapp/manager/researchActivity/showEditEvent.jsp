<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>


<h2><bean:message key="title.event.edit" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h2>

<br />
<br />
<div class="forminline dinline">
	<fr:form action="/editEvent.do?method=prepare">
		<fr:edit schema="event.edit" id="edit" name="pageContainerBean" property="selected" type="net.sourceforge.fenixedu.domain.research.activity.ResearchEvent">
			<fr:destination name="invalid" path="/editEvent.do?method=invalid"/>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright thmiddle mtop05"/>
				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			</fr:layout>
		</fr:edit>
		<fr:edit id="pageContainerBean" name="pageContainerBean" visible="false"/>
		<html:submit><bean:message key="submit" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></html:submit>
	</fr:form>
	<fr:form action="/editEvent.do?method=back">
		<fr:edit id="back" name="pageContainerBean" visible="false"/>
		<html:submit><bean:message key="return" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></html:submit>
	</fr:form>
</div>