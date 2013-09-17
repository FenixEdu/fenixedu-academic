<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>

<logic:present name="siteView" >
	<bean:define id="component" name="siteView" property="commonComponent" />
	<bean:define id="curricularCourses" name="component" property="associatedDegrees" />
	<div id="associated-degrees">
		<bean:define id="curricularCoursesFiltered" value="" />		
		<logic:iterate id="curricularCourse" name="curricularCourses">
			<bean:define id="curricularCourseSigla" name="curricularCourse" property="infoDegreeCurricularPlan.infoDegree.sigla" />
			<logic:notMatch name="curricularCoursesFiltered" value="<%= curricularCourseSigla.toString() %>" >	
				<bean:write name="curricularCourse" property="infoDegreeCurricularPlan.infoDegree.degree.presentationName" /><br />			
				<bean:define id="curricularCoursesFiltered" value="<%= curricularCoursesFiltered.toString().concat(curricularCourseSigla.toString()) %>" />
			</logic:notMatch>
		</logic:iterate>
	</div>
</logic:present>
