<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<%@page import="net.sourceforge.fenixedu.util.FenixDigestUtils"%>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message bundle="DEGREE_OFFICE_RESOURCES" key="label.confirmMarkSheet"/></h2>

<fr:view name="markSheet" schema="degreeAdministrativeOffice.markSheet.view">
	<fr:layout name="tabular" >
		<fr:property name="classes" value="tstyle4 thlight thright"/>
        <fr:property name="columnClasses" value=",,"/>
	</fr:layout>
</fr:view>


<p class="mtop1 mbottom05"><bean:message bundle="DEGREE_OFFICE_RESOURCES" key="label.markSheet.students.capitalized"/>:</p>
<fr:view name="markSheet" property="enrolmentEvaluations" schema="markSheet.view.evaluation">
	<fr:layout name="tabular" >
		<fr:property name="classes" value="tstyle4 thlight mtop05"/>
        <fr:property name="columnClasses" value=",,"/>
	</fr:layout>
</fr:view>

<bean:define id="mark" name="markSheet" type="net.sourceforge.fenixedu.domain.MarkSheet"/>
<bean:define id="checksum" value="<%= FenixDigestUtils.getPrettyCheckSum(mark.getCheckSum())%>"/>


<p>
	<span class="highlight1"><bean:message bundle="DEGREE_OFFICE_RESOURCES" key="label.checksum"/></strong> : <bean:write name="checksum"/></span>
</p>

<p class="mtop15">
	<bean:message bundle="DEGREE_OFFICE_RESOURCES" key="message.markSheet.confirm"/>
</p>


<html:form action="/markSheetManagement.do">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="confirmMarkSheet"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.epID" property="epID" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.dID" property="dID" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.dcpID" property="dcpID" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.ccID" property="ccID"  />	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.msID" property="msID" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.tn" property="tn" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.ed" property="ed"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.mss" property="mss" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.mst" property="mst" />
	<p>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message bundle="DEGREE_OFFICE_RESOURCES" key="label.confirm"/></html:submit>
		<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" styleClass="inputbutton" onclick="this.form.method.value='prepareSearchMarkSheetFilled';this.form.submit();"><bean:message bundle="DEGREE_OFFICE_RESOURCES" key="label.back"/></html:cancel>
	</p>
</html:form>
