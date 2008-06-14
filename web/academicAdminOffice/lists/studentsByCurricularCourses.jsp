<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>


<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<p>
		<span class="error0"><!-- Error messages go here --><bean:write name="message" /></span>
	</p>
</html:messages>
<fr:form action='<%= "/studentsListByCurricularCourse.do" %>'>

<html:hidden property="method" value="searchByCurricularCourse"/>
<bean:define id="searchBean" name="searchBean"/>
<bean:define id="semester" name="semester"/>
<bean:define id="curricularYear" name="curricularYear" property="year"/>
<bean:define id="year" name="year"/>
<bean:define id="curricularCourseCode" name="searchBean" property="curricularCourse.idInternal"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.searchBean" name="searchBean" property="searchBean" value="<%= searchBean.toString() %>"  />
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.semester" property="semester" value="<%= semester.toString() %>"  />
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.curricularYear" property="curricularYear" value="<%= curricularYear.toString() %>"  />
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.curricularCourseCode" property="curricularCourseCode" value="<%= curricularCourseCode.toString() %>"  />
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.curricularCourseCode" property="year" value="<%= year.toString() %>"  />

<h2>
	<bean:write name="searchBean" property="curricularCourse.name"/> 
	(<bean:write name="searchBean" property="degreeCurricularPlan.name"/>)
</h2>

	<p class="mtop15 mbottom1">
		<em class="highlight5">
			<bean:write name="searchBean" property="executionYear.year" /> - <bean:message key="label.period" arg0="<%=year.toString()%>" arg1="<%=semester.toString()%>"  bundle="ACADEMIC_OFFICE_RESOURCES"/>
		</em>
	</p>
	
<logic:present name="enrolmentList">
	
	<bean:size id="enrolmentListSize" name="enrolmentList" />

	<p class="mtop2">
		<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.studentCurricularPlan.lists.total"/>: <%= enrolmentListSize %>
	</p>

	<fr:view schema="enrolmentStudentsList.view" name="enrolmentList">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thright thlight thcenter"/>
		</fr:layout>	
	</fr:view>
	
	<logic:greaterThan name="enrolmentListSize" value="0">
		<p class="mtop15 mbottom15">
			<html:image border="0" src='<%= request.getContextPath() + "/images/excel.gif"%>' altKey="excel" bundle="IMAGE_RESOURCES" onclick="this.form.method.value='exportInfoToExcel';this.form.submit();return true;"></html:image>	
			<bean:message key="link.lists.xlsFileToDownload" bundle="ACADEMIC_OFFICE_RESOURCES"/>	
		</p>
	</logic:greaterThan>
	
</logic:present>
</fr:form>
