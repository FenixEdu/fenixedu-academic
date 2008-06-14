<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="date"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<em><bean:message bundle="VIGILANCY_RESOURCES" key="label.navheader.person.vigilant"/></em><br>
<h2><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.editUnavailablePeriod"/></h2><br>

<logic:messagesPresent message="true">
	<p>
		<html:messages id="messages" message="true" bundle="VIGILANCY_RESOURCES">
			<span class="error0"><bean:write name="messages"/></span>
		</html:messages>
	</p>
</logic:messagesPresent>
				   
<fr:edit
		id="editUnavailablePeriod"
		name="bean"
		schema="unavailable.create"
    	action="vigilancy/unavailablePeriodManagement.do?method=changeUnavailablePeriod">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thlight thright" />
	</fr:layout>
</fr:edit>
			    