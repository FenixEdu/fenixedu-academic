<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="label.studentsListByDegree" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<p>
		<span class="error0"><!-- Error messages go here --><bean:write name="message" /></span>
	</p>
</html:messages>

<fr:form action="<%= "/studentsListByDegree.do" %>">
<html:hidden property="method" value="searchByDegree"/>
	<fr:edit name="searchParametersBean" schema="student.list.searchByDegree.chooseDegree" id="chooseDegree">
		<fr:destination name="postBack" path="/studentsListByDegree.do?method=postBack"/>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
		</fr:layout>
	</fr:edit>
	
	<logic:notEmpty name="searchParametersBean" property="degree">
		<fr:edit id="chooseParameters"
			name="searchParametersBean"
			schema="student.list.searchByDegree.parameters">
			<fr:layout name="tabular-row" />
		</fr:edit>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick="this.form.method.value='searchByDegree';this.form.submit();">
		<bean:message key="button.submit" bundle="ACADEMIC_OFFICE_RESOURCES"/>
		</html:submit>		
	</logic:notEmpty>

<logic:present name="studentCurricularPlanList">

	<bean:size id="studentCurricularPlanListSize" name="studentCurricularPlanList" />
	

	<p class="mtop2">
		 <bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.studentCurricularPlan.lists.total"/> <%= studentCurricularPlanListSize %>
	</p>
	<fr:view schema="studentCurricularPlanList.view" name="studentCurricularPlanList">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thright thlight thcenter"/>
		</fr:layout>	
	</fr:view>
	<logic:greaterThan name="studentCurricularPlanListSize" value="0">
		<p class="mtop15 mbottom15">	
			<html:image border="0" src="<%= request.getContextPath() + "/images/excel.gif"%>" altKey="excel" bundle="IMAGE_RESOURCES" onclick="this.form.method.value='exportInfoToExcel';this.form.submit();return true;"></html:image>	
				<bean:message key="link.lists.xlsFileToDownload" bundle="ACADEMIC_OFFICE_RESOURCES"/>	
		</p>
	</logic:greaterThan>
</logic:present>

</fr:form>
