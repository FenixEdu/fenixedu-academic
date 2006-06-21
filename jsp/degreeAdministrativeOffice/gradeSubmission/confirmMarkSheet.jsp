<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


<%@page import="net.sourceforge.fenixedu.util.FenixDigestUtils"%>
<h2><bean:message key="label.confirmMarkSheet"/></h2>

<fr:view name="markSheet" schema="degreeAdministrativeOffice.markSheet.view">
	<fr:layout name="tabular" >
		<fr:property name="classes" value="tstyle4"/>
        <fr:property name="columnClasses" value="listClasses,,"/>
	</fr:layout>
</fr:view>

<br/>
<bean:message key="label.markSheet.students.capitalized"/>:
<fr:view name="markSheet" property="enrolmentEvaluations" schema="markSheet.view.evaluation">
	<fr:layout name="tabular" >
		<fr:property name="classes" value="tstyle4"/>
        <fr:property name="columnClasses" value="listClasses,,"/>
	</fr:layout>
</fr:view>

<bean:define id="mark" name="markSheet" type="net.sourceforge.fenixedu.domain.MarkSheet"/>
<bean:define id="checksum" value="<%= FenixDigestUtils.getPrettyCheckSum(mark.getCheckSum())%>"/>
<strong><bean:message key="label.checksum"/></strong> : <bean:write name="checksum"/>
<br/><br/><br/>
<span class="warning0"><bean:message key="message.markSheet.confirm"/></span>
<br/><br/>
<html:form action="/markSheetManagement.do">
	<html:hidden property="method" value="confirmMarkSheet"/>
	<html:hidden property="epID" />
	<html:hidden property="dID" />
	<html:hidden property="dcpID" />
	<html:hidden property="ccID"  />	
	<html:hidden property="msID" />
	<html:hidden property="tn" />
	<html:hidden property="ed"/>
	<html:hidden property="mss" />
	<html:hidden property="mst" />		
	<html:submit styleClass="inputbutton"><bean:message key="label.confirm"/></html:submit>
	<html:cancel styleClass="inputbutton" onclick="this.form.method.value='prepareSearchMarkSheetFilled';this.form.submit();"><bean:message key="label.back"/></html:cancel>
</html:form>
