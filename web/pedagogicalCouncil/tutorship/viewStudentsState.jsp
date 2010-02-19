<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<!-- showStudentPerformanceGrid.jsp -->

<em><bean:message key="pedagogical.council" bundle="PEDAGOGICAL_COUNCIL" /></em>
<h2><bean:message key="title.tutorship.student.lowPerformance" bundle="PEDAGOGICAL_COUNCIL" /></h2>

<fr:form action="<%= "/studentLowPerformance.do"%>" id="prescriptionForm">
	
	<html:hidden property="method" value="viewStudentsState"/>
  	<fr:edit  id="prescriptionBean" name="prescriptionBean" schema="tutorship.tutorate.student.lowPerformance.prescriptionBean" >
  	
  	</fr:edit>

<logic:present name="studentlowPerformanceBeans"> 
	<p>
		<a href="javascript:var form = document.getElementById('prescriptionForm');form.method.value='downloadStudentsLowPerformanceList';form.submit();form.method.value='viewStudentsState'">
			<html:image border="0" src="<%= request.getContextPath() + "/images/excel.gif"%>" altKey="excel" bundle="IMAGE_RESOURCES"></html:image>
			<bean:message key="link.tutorship.student.lowPerformance.donwload" bundle="PEDAGOGICAL_COUNCIL"/>
		</a>
	</p>
</logic:present>

</fr:form>
<logic:present name="studentlowPerformanceBeans" >
	 	<fr:view name="studentlowPerformanceBeans" schema="tutorship.tutorate.student.lowPerformance" layout="tabular">
	 	<fr:layout> <fr:property name="classes" value="tstyle1 thlight mtop0 mbottom15 tdcenter"/>
	 	</fr:layout> 
	    </fr:view>
</logic:present>