<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.domain.degree.DegreeType" %>

<logic:present name="infoCurriculum" >

<bean:define id="infoCurricularCourse" name="infoCurriculum" property="infoCurricularCourse" />
<bean:define id="infoDegree" name="infoCurriculum" property="infoCurricularCourse.infoDegreeCurricularPlan.infoDegree" />
<bean:define id="degreeCurricularPlanID" name="infoCurriculum" property="infoCurricularCourse.infoDegreeCurricularPlan.idInternal" />

<bean:define id="institutionUrl" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/></bean:define>
<div class="breadcumbs mvert0"><a href="<%= institutionUrl %>"><bean:message key="institution.name.abbreviation" bundle="GLOBAL_RESOURCES"/></a> 
<bean:define id="institutionUrlTeaching" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/><bean:message key="link.institution" bundle="GLOBAL_RESOURCES"/></bean:define>
&nbsp;&gt;&nbsp;<a href="<%= institutionUrlTeaching %>"><bean:message key="public.degree.information.label.education" bundle="PUBLIC_DEGREE_INFORMATION" /></a>
	<bean:define id="degreeType" name="infoDegree" property="tipoCurso" />	
	&nbsp;&gt;&nbsp;
	<html:link page="<%= "/showDegreeSite.do?method=showDescription&amp;degreeID=" + request.getAttribute("degreeID").toString()  %>">
	<!-- &amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "-->
		<bean:write name="infoDegree" property="sigla" />
	</html:link>
	&nbsp;&gt;&nbsp;
	<html:link page="<%= "/showDegreeSite.do?method=showCurricularPlan&amp;degreeID=" +  request.getAttribute("degreeID") + "&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID").toString() %>" >
	<!-- &amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) + " -->
		<bean:write name="infoCurricularCourse" property="infoDegreeCurricularPlan.name" />
	</html:link>
	&nbsp;&gt;&nbsp;
	<html:link page="<%= "/showDegreeCurricularPlanNew.do?method=showCurricularPlan&amp;degreeID=" + request.getAttribute("degreeID") + "&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID").toString() + "&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) %>" >
		<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.curriculum"/>
	</html:link>
	&nbsp;&gt;&nbsp;<bean:write name="infoCurricularCourse" property="name" />	
</div>	

<h1>
	<bean:define id="degreeType" name="infoDegree" property="tipoCurso.name"/>
    <logic:equal name="degreeType" value="DEGREE" >
	    <bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.degreeType" />
	</logic:equal>    
	<logic:equal name="degreeType" value="MASTER_DEGREE" >
	    <bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.masterDegreeType" />
	</logic:equal>  
	<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.in" />
	<bean:write name="infoDegree" property="nome" />
</h1>

<!-- CURRICULAR PLAN -->
<h2 class="greytxt">
	<bean:message key="public.degree.information.label.curricularPlan"  bundle="PUBLIC_DEGREE_INFORMATION" />
	<bean:write name="infoCurricularCourse" property="infoDegreeCurricularPlan.name"/>
	<logic:notEmpty name="infoCurricularCourse" property="infoDegreeCurricularPlan.initialDate">
		<logic:empty name="infoCurricularCourse" property="infoDegreeCurricularPlan.endDate">
			(<bean:message bundle="PUBLIC_DEGREE_INFORMATION"  key="public.degree.information.label.since" />
			<bean:define id="initialDate" name="infoCurricularCourse" property="infoDegreeCurricularPlan.initialDate" />
			<%= initialDate.toString().substring(initialDate.toString().lastIndexOf(" ")+1) %>)
		</logic:empty>
		<logic:notEmpty name="infoCurricularCourse" property="infoDegreeCurricularPlan.endDate">
			(<bean:message bundle="PUBLIC_DEGREE_INFORMATION"  key="public.degree.information.label.of" />
			<bean:define id="initialDate" name="infoCurricularCourse" property="infoDegreeCurricularPlan.initialDate" />
			<%= initialDate.toString().substring(initialDate.toString().lastIndexOf(" ")+1) %>
			<bean:message bundle="PUBLIC_DEGREE_INFORMATION"  key="public.degree.information.label.to" />
			<bean:define id="endDate" name="infoCurricularCourse" property="infoDegreeCurricularPlan.endDate" />	
			<%= endDate.toString().substring(endDate.toString().lastIndexOf(" ")) %>)
		</logic:notEmpty>
	</logic:notEmpty>	
</h2>


<h2 class="greytxt" style="margin-top: 1.5em;">
	<bean:write name="infoCurricularCourse" property="name"/>
	<logic:notEmpty name="infoCurricularCourse" property="acronym">
		&nbsp;(<bean:write name="infoCurricularCourse" property="acronym"/>)
	</logic:notEmpty>
</h2>

<!-- EXECUTION COURSES LINK  -->
<logic:present name="infoCurricularCourse" property="infoAssociatedExecutionCourses" />
	<bean:size id="listSize" name="infoCurricularCourse" property="infoAssociatedExecutionCourses" />
	<logic:greaterThan name="listSize" value="0">
		<div class="col_right">
		  <table class="box" cellspacing="0">
				<tr>
					<td class="box_header"><strong><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.courses" /></strong></td>
				</tr>
				<logic:iterate id="infoExecutionCourse" name="infoCurricularCourse" property="infoAssociatedExecutionCourses">
					<logic:notEqual name="infoExecutionCourse" property="infoExecutionPeriod.state.stateCode" value="NO">
						<tr>
							<td class="box_cell">
								<bean:define id="executionCourseID" name="infoExecutionCourse" property="idInternal" />
								<html:link page="<%= "/executionCourse.do?method=firstPage&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID").toString() %>">
									<bean:write name="infoExecutionCourse" property="nome" />&nbsp;(<bean:write name="infoExecutionCourse" property="infoExecutionPeriod.infoExecutionYear.year"/>&nbsp;-&nbsp;<bean:write name="infoExecutionCourse" property="infoExecutionPeriod.name"/>)
								</html:link>
							</td>
						</tr>
					</logic:notEqual>
				</logic:iterate>
		  </table>
		</div>
	</logic:greaterThan>
</logic:present>

<p><span class="error"><!-- Error messages go here --><html:errors /></span></p>

<!-- 	CURRICULAR COURSE SCOPES  -->
<logic:present name="infoCurricularCourse" property="infoScopes">
	<h2 class='arrow_bullet'>
		<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.scope" />	
	</h2>
	<logic:iterate id="infoCurricularCourseScope" name="infoCurricularCourse" property="infoScopes">
			<logic:notEmpty name="infoCurricularCourseScope" property="infoBranch.name">
				<logic:notEqual name="infoCurricularCourseScope" property="infoBranch.name" value="">	
					<p>
						<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.group"/>: 
						<bean:write name="infoCurricularCourseScope" property="infoBranch.name"/>
					</p>
	 			</logic:notEqual>
			</logic:notEmpty>
					
			<logic:notEmpty name="infoCurricularCourseScope" property="infoCurricularSemester.infoCurricularYear.year">
				<logic:notEqual name="infoCurricularCourseScope" property="infoCurricularSemester.infoCurricularYear.year" value="">
					<p>
						<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.curricular.period"/>: 
						<bean:write name="infoCurricularCourseScope" property="infoCurricularSemester.infoCurricularYear.year"/>&nbsp;<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.year"/>
						<logic:notEmpty name="infoCurricularCourseScope" property="infoCurricularSemester.semester">
							<logic:notEqual name="infoCurricularCourseScope" property="infoCurricularSemester.semester" value="">	
								<bean:write name="infoCurricularCourseScope" property="infoCurricularSemester.semester"/>&nbsp;<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.semester"/>
				 			</logic:notEqual>
						</logic:notEmpty>
					</p>						
				</logic:notEqual>
			</logic:notEmpty>
	</logic:iterate>
</logic:present>

<!-- CURRICULAR COURSE INFO -->
<logic:present name="infoCurriculum" property="generalObjectives">
	<logic:notEmpty name="infoCurriculum" property="generalObjectives">
		<logic:notEqual name="infoCurriculum" property="generalObjectives" value="">
			<h2 class='arrow_bullet' />&nbsp;<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.generalObjectives" />	</h2>
			<p><bean:write name="infoCurriculum" property="generalObjectives" filter="false"/></p>
		</logic:notEqual>
	</logic:notEmpty>
</logic:present>

<logic:present name="infoCurriculum" property="operacionalObjectives">
<logic:notEmpty name="infoCurriculum" property="operacionalObjectives">
	<logic:notEqual name="infoCurriculum" property="operacionalObjectives" value="">
		<h2 class='arrow_bullet' />&nbsp;<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.operacionalObjectives" /></h2>
		<p><bean:write name="infoCurriculum" property="operacionalObjectives" filter="false"/></p>
	</logic:notEqual>
</logic:notEmpty> 
</logic:present>

<logic:present name="infoCurriculum" property="program">
<logic:notEmpty name="infoCurriculum" property="program">
	<logic:notEqual name="infoCurriculum" property="program" value="">
		<h2 class='arrow_bullet' />&nbsp;<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.program" /></h2>	
		<p><bean:write name="infoCurriculum" property="program" filter="false" /></p>	
	</logic:notEqual>
</logic:notEmpty>
</logic:present>


<logic:notPresent name="infoCurriculum" >
	<p><span class="error"><!-- Error messages go here --><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.error.impossibleCurricularCourseInfo" /></span></p>
</logic:notPresent>