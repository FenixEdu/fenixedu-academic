<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="label.notNeedToEnrol.enrolment" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<fr:form action="/notNeedToEnrolEnrolments.do?method=chooseNotNeedToEnrol">
	<fr:edit id="showNotNeedToEnrol" name="bean" 
		type="net.sourceforge.fenixedu.dataTransferObject.PageContainerBean">
		<fr:layout name="pages">
			<fr:property name="classes" value="tstyle1 thlight"/>
			<fr:property name="paged" value="false"/>
			<fr:property name="subSchema" value="notNeedToEnroll.view.notNeedToEnroll"/>
			<fr:property name="buttonLabel" value="button.edit"/>
		</fr:layout>
	</fr:edit>
</fr:form>
