<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="label.markSheet.edit"/> <bean:message key="label.markSheet"/></h2>
<br/>

<logic:messagesPresent message="true">
	<ul>
	<html:messages id="messages" message="true">
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


