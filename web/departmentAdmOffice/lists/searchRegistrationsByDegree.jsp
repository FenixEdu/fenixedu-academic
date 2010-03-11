<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<em><bean:message key="label.departmentAdmOffice"/></em>
<h2><bean:message key="label.studentsListByDegree" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<p>
		<span class="error0"><!-- Error messages go here --><bean:write name="message" /></span>
	</p>
</html:messages>

<fr:form action="/studentsListByDegree.do" id="searchForm">
<html:hidden property="method" value="searchByDegree"/>
<html:hidden property="extendedInfo" value="false"/>
	<fr:edit id="searchParametersBean" name="searchParametersBean" visible="false"/>
	<fr:edit id="chosenDegree" name="searchParametersBean" schema="SearchStudentsByDegreeParametersBean.edit.degreeAndRegistrationInfo">
		<fr:destination name="postBack" path="/studentsListByDegree.do?method=postBack"/>
		<fr:destination name="invalid" path="/studentsListByDegree.do?method=prepareByDegree"/>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop025 thmiddle"/>
	        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
			<fr:property name="requiredMarkShown" value="true" />
			<fr:property name="requiredMessageShown" value="false" />
		</fr:layout>
	</fr:edit>

	<fr:edit id="chosenParameters" name="searchParametersBean" schema="SearchStudentsByDegreeParametersBean.edit.parameters">
		<fr:layout name="tabular-row">
			<fr:property name="classes" value="tdtop ulnomargin"/>
		</fr:layout>
	</fr:edit>
	<p class="mtop1">
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
			<bean:message key="button.search" bundle="ACADEMIC_OFFICE_RESOURCES"/>
		</html:submit>
	</p>

<logic:present name="studentCurricularPlanList">
	<bean:size id="studentCurricularPlanListSize" name="studentCurricularPlanList" />
	<p class="mtop2">
		 <bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.studentCurricularPlan.lists.total" arg0="<%= studentCurricularPlanListSize.toString() %>"/> 
	</p>
	<logic:greaterThan name="studentCurricularPlanListSize" value="0">
		<p class="mtop15 mbottom15">
		<a href="javascript:var form = document.getElementById('searchForm');form.extendedInfo.value='false';form.method.value='exportInfoToExcel';form.submit();form.method.value='searchByDegree'">
			<html:image border="0" src="<%= request.getContextPath() + "/images/excel.gif"%>" altKey="excel" bundle="IMAGE_RESOURCES"></html:image>
			<bean:message key="link.lists.xlsFileToDownload" bundle="ACADEMIC_OFFICE_RESOURCES"/>
			</a>
		</p>
		<p class="mtop15 mbottom15">
		<a href="javascript:var form = document.getElementById('searchForm');form.extendedInfo.value='true';form.method.value='exportInfoToExcel';form.submit();form.method.value='searchByDegree'">
			<html:image border="0" src="<%= request.getContextPath() + "/images/excel.gif"%>" altKey="excel" bundle="IMAGE_RESOURCES"></html:image>
			<bean:message key="link.lists.xlsFileToDownload.extended.info" bundle="ACADEMIC_OFFICE_RESOURCES"/>
			</a>
		</p>
	</logic:greaterThan>
	<fr:view schema="departmentAdmOffice.studentCurricularPlanList.view" name="studentCurricularPlanList">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thright thlight thcenter tdcenter"/>
		</fr:layout>	
	</fr:view>
</logic:present>

</fr:form>
