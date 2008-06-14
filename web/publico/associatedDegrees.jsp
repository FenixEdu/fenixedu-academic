<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>

<logic:present name="siteView" >
	<bean:define id="component" name="siteView" property="commonComponent" />
	<bean:define id="curricularCourses" name="component" property="associatedDegrees" />
	<div id="associated-degrees">
		<bean:define id="curricularCoursesFiltered" value="" />		
		<logic:iterate id="curricularCourse" name="curricularCourses">
			<bean:define id="curricularCourseSigla" name="curricularCourse" property="infoDegreeCurricularPlan.infoDegree.sigla" />
			<logic:notMatch name="curricularCoursesFiltered" value="<%= curricularCourseSigla.toString() %>" >	
				<bean:write name="curricularCourse" property="infoDegreeCurricularPlan.infoDegree.tipoCurso" />
				<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.in" /> 			
				<bean:write name="curricularCourse" property="infoDegreeCurricularPlan.infoDegree.nome" /><br />			
				<bean:define id="curricularCoursesFiltered" value="<%= curricularCoursesFiltered.toString().concat(curricularCourseSigla.toString()) %>" />
			</logic:notMatch>
		</logic:iterate>
	</div>
</logic:present>
