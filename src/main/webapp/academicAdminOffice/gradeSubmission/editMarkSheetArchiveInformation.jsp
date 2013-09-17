<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.markSheet.edit"/> <bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.markSheet"/></h2>
<br/>

<logic:messagesPresent message="true">
	<ul>
	<html:messages bundle="ACADEMIC_OFFICE_RESOURCES" id="messages" message="true">
		<li><span class="error0"><bean:write name="messages" /></span></li>
	</html:messages>
	</ul>
	<br/>
</logic:messagesPresent>

<fr:view name="markSheet" schema="markSheet.view">
	<fr:layout name="tabular" >
		<fr:property name="classes" value="tstyle4"/>
        <fr:property name="columnClasses" value="listClasses,,"/>
	</fr:layout>
</fr:view>

<bean:define id="url" name="url"/>
<bean:define id="finalUrl"><%= "/markSheetManagement.do?method=prepareSearchMarkSheetFilled" + url %></bean:define>

<fr:edit id="markSheet"
		 name="markSheet"
		 type="net.sourceforge.fenixedu.domain.MarkSheet"
		 schema="markSheet.editArchiveInformation"
		 action='<%= finalUrl %>'>

	<fr:destination name="cancel" path='<%= finalUrl %>'/>
	<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4"/>
	        <fr:property name="columnClasses" value="listClasses,,"/>
	</fr:layout>
</fr:edit>


