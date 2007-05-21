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

<fr:form action="/studentsListByDegree.do?method=searchByDegree">

	
	<fr:edit id="executionDegree"
			 name="executionDegreeBean"
			 type="net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.lists.ExecutionDegreeListBean"
			 schema="choose.executionDegrees">
		<fr:destination name="degreePostBack" path="/studentsListByDegree.do?method=chooseDegreePostBack"/>
		<fr:destination name="degreeCurricularPlanPostBack" path="/studentsListByDegree.do?method=chooseDegreeCurricularPlanPostBack"/>
	
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
		</fr:layout>
	</fr:edit>
	
	<fr:edit id="chooseIngression"
		name="listInformationBean"
		type="net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.lists.ListInformationBean"
		schema="ingression.information-list">	
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
		</fr:layout>
	</fr:edit>
		
	<html:submit><bean:message key="button.submit" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit>		

</fr:form>

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
</logic:present>
