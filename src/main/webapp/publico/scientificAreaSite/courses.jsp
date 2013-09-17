<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:xhtml/>

<h1 class="mbottom03 cnone"><fr:view name="site" property="unitNameWithAcronym"/></h1>
<h2 class="mtop15"><bean:message key="property.courses"/></h2>



<bean:define id="scientificAreaUnit" name="unit" type="net.sourceforge.fenixedu.domain.organizationalStructure.ScientificAreaUnit"/>


<bean:define id="departmentId" value="<%= String.valueOf(scientificAreaUnit.getDepartmentUnit().getExternalId()) %>" type="java.lang.String"/>

<logic:iterate id="courseGroupUnit" name="courseGroupUnits">

	<p>
	<h2 class="mtop1 mbottom0 greytxt"><strong><fr:view name="courseGroupUnit" property="name"/></strong></h2>
	<fr:view name="courseGroupUnit" property="competenceCourses">
	
		<fr:layout>
			<fr:property name="eachLayout" value="values"/>
			<fr:property name="eachSchema" value="view.competence.courses"/>
		</fr:layout>
		<fr:destination name="view.competence.course" path="<%= "/department/showCompetenceCourse.faces?action=ccm&competenceCourseID=${externalId}&selectedDepartmentUnitID=" + departmentId %>"/>
	</fr:view>
	</p>
</logic:iterate>