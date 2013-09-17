<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml />

<h2><bean:message key="title.inquiries.resultsWithDescription" bundle="INQUIRIES_RESOURCES"/></h2>
    	
<logic:present name="professorship">
	<h3 class="mtop2 mbottom1"><bean:write name="professorship" property="teacher.person.name"/></h3>
</logic:present>


<logic:present name="executionCourses">
	<table class="tstyle2 thlight thleft mtop05">
	<logic:iterate id="executionCourse" name="executionCourses">
		<bean:define id="executionCourseID" name="executionCourse" property="oid" />
		<tr ><td style="background-color: #fafafa"><bean:write name="executionCourse" property="nome"/></td>
		<td style="background-color: #fafafa"><fr:view name="executionCourse" property="executionDegrees" schema="net.sourceforge.fenixedu.domain.ExecutionDegree.name">		
				<fr:layout name="tabular-list">
					<fr:property name="classes" value="tstylenone"/>
					<fr:property name="columnClasses" value="width8em,"/>
					<fr:property name="subLayout" value="values" />
					<fr:property name="subSchema" value="net.sourceforge.fenixedu.domain.ExecutionDegree.name" />
					<fr:property name="link(view)"
						value="<%="/viewInquiriesResults.do?method=selectExecutionCourse&executionCourseID="+executionCourseID%>" />
					<fr:property name="key(view)" value="label.view" />
					<fr:property name="param(view)" value="oid/executionDegreeID,degreeCurricularPlan.externalId/degreeCurricularPlanID" />
				</fr:layout>
		</fr:view></td></tr>
	</logic:iterate>
	</table>
</logic:present>
