<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="date"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<em><bean:message bundle="VIGILANCY_RESOURCES" key="label.navheader.person.vigilant"/></em>
<h2><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.editUnavailablePeriod"/></h2>

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
    	action="<%= "/vigilancy/unavailablePeriodManagement.do?method=changeUnavailablePeriodForVigilant&gid=" + request.getParameter("gid") %>" >
	    <fr:layout>
			<fr:property name="classes" value="tstyle5 thlight thright" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>
		<fr:destination name="exception" path="<%= "/vigilancy/unavailablePeriodManagement.do?oid=" + request.getParameter("oid") + "&method=editUnavailablePeriodOfVigilant"%>"/>
</fr:edit>


			    