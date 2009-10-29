<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ page import="net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.AcademicServiceRequestType" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="label.requestListByDegree" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<p>
		<span class="error0"><!-- Error messages go here --><bean:write name="message" /></span>
	</p>
</html:messages>

<fr:form action="/requestListByDegree.do" id="searchForm">
<html:hidden property="method" value="runSearchAndShowResults"/>
<fr:edit name="degreeByExecutionYearBean" id="degreeByExecutionYearBean" schema="DegreeByExecutionYearBean.edit">
	<fr:destination name="postBack" path="/requestListByDegree.do?method=postBack"/>
	<fr:destination name="invalid" path="/requestListByDegree.do?method=prepareSearch"/>
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thlight thright mtop025 thmiddle"/>
        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
	</fr:layout>
</fr:edit>

<bean:define id="documentRequestSearchBean" name="documentRequestSearchBean" type="net.sourceforge.fenixedu.dataTransferObject.serviceRequests.DocumentRequestSearchBean"/>
<fr:edit id="documentRequestSearchBean" name="documentRequestSearchBean" schema="<%= (documentRequestSearchBean.getAcademicServiceRequestType() == AcademicServiceRequestType.DOCUMENT) ? "DocumentRequestSearchBean.edit" : "AcademicServiceRequestSearchBean.edit" %>">
	<fr:destination name="postBack" path="/requestListByDegree.do?method=postBack"/>
	<fr:destination name="invalid" path="/requestListByDegree.do?method=prepareSearch"/>
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thlight thright mtop025 thmiddle"/>
        <fr:property name="columnClasses" value=",,tdclear"/>
	</fr:layout>
</fr:edit>

<p class="mtop1">
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="button.search" bundle="ACADEMIC_OFFICE_RESOURCES"/>
	</html:submit>
</p>

<logic:present name="registrationAcademicServiceRequestList">
	<bean:size id="requestListSize" name="registrationAcademicServiceRequestList" />
	<p class="mtop2">
		 <bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.requestList.total" arg0="<%= requestListSize.toString() %>"/>
	</p>
	<fr:view name="registrationAcademicServiceRequestList" schema="RegistrationAcademicServiceRequestList.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thright thlight thcenter tdcenter"/>
		</fr:layout>	
	</fr:view>
	<logic:greaterThan name="requestListSize" value="0">
		<p class="mtop15 mbottom15">
			<a href="javascript:var form = document.getElementById('searchForm');form.method.value='exportInfoToExcel';form.submit();form.method.value='runSearchAndShowResults'">
				<html:image border="0" src="<%= request.getContextPath() + "/images/excel.gif"%>" altKey="excel" bundle="IMAGE_RESOURCES"></html:image>
				<bean:message key="link.lists.xlsFileToDownload" bundle="ACADEMIC_OFFICE_RESOURCES"/>
			</a>
		</p>
	</logic:greaterThan>
</logic:present>

</fr:form>
