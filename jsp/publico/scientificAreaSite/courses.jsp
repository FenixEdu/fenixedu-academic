<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html:xhtml/>

<h1 class="mbottom03 cnone"><fr:view name="site" property="unitNameWithAcronym"/></h1>
<h2 class="mtop15"><bean:message key="property.courses"/></h2>



<bean:define id="scientificAreaUnit" name="unit" type="net.sourceforge.fenixedu.domain.organizationalStructure.ScientificAreaUnit"/>


<bean:define id="departmentId" value="<%= String.valueOf(scientificAreaUnit.getDepartmentUnit().getIdInternal()) %>" type="java.lang.String"/>

<logic:iterate id="courseGroupUnit" name="courseGroupUnits">

	<p>
	<h2 class="mtop1 mbottom0 greytxt"><strong><fr:view name="courseGroupUnit" property="name"/></strong></h2>
	<fr:view name="courseGroupUnit" property="competenceCourses">
	
		<fr:layout>
			<fr:property name="eachLayout" value="values"/>
			<fr:property name="eachSchema" value="view.competence.courses"/>
		</fr:layout>
		<fr:destination name="view.competence.course" path="<%= "/department/showCompetenceCourse.faces?action=ccm&competenceCourseID=${idInternal}&selectedDepartmentUnitID=" + departmentId %>"/>
	</fr:view>
	</p>
</logic:iterate>