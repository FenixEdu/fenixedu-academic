<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.util.TipoCurso" %>

<p><span class="error"><html:errors/></span></p>

<logic:present name="infoCurriculum" >

<bean:define id="infoCurricularCourse" name="infoCurriculum" property="infoCurricularCourse" />
<bean:define id="infoDegree" name="infoCurriculum" property="infoCurricularCourse.infoDegreeCurricularPlan.infoDegree" />
<bean:define id="degreeCurricularPlanID" name="infoCurriculum" property="infoCurricularCourse.infoDegreeCurricularPlan.idInternal" />

<bean:define id="institutionUrl" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/></bean:define>
<div class="breadcumbs"><a href="<%= institutionUrl %>"><bean:message key="institution.name.abbreviation" bundle="GLOBAL_RESOURCES"/></a> >
		<bean:define id="degreeType" name="infoDegree" property="tipoCurso" />	
		<logic:equal name="degreeType" value="<%= TipoCurso.MESTRADO_OBJ.toString() %>">
			<%--<html:link page="<%= "/showDegrees.do?method=master&executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) %>" >--%>
			 	Ensino Mestrados
	 		 <%--</html:link>--%>
		</logic:equal>
		<logic:equal name="degreeType" value="<%= TipoCurso.LICENCIATURA_OBJ.toString() %>">
			<%--<html:link page="<%= "/showDegrees.do?method=nonMaster&executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) %>" >--%>
				Ensino Licenciaturas
			<%--</html:link>--%>
		</logic:equal>
		&gt;&nbsp;
		<%--<html:link page="<%= "/showDegreeSite.do?method=showDescription&amp;inEnglish=true&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;degreeID=" + request.getAttribute("degreeID").toString() + "&amp;executionDegreeID="  +  request.getAttribute("executionDegreeID") %>">--%>
			<bean:write name="infoDegree" property="sigla" />
		<%--</html:link>--%>
		&gt;&nbsp;
		<%--<html:link page="<%= "/showDegreeSite.do?method=showCurricularPlan&amp;inEnglish=true&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;degreeID=" +  request.getAttribute("degreeID") + "&amp;executionDegreeID="  +  request.getAttribute("executionDegreeID") %>" >
			<bean:message key="label.curricularPlan.en"/>
		<%--</html:link>--%>
		&gt;&nbsp;
		<%--<html:link page="<%= "/showDegreeCurricularPlan.do?method=showCurricularPlan&amp;inEnglish=true&amp;degreeID=" + request.getAttribute("degreeID") + "&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID").toString() + "&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) %>" >--%>
		<bean:message key="label.curriculum.en"/>
		<%--</html:link>--%>
	&gt;&nbsp;
	<bean:write name="infoCurricularCourse" property="name" />
	
</div>	

<!-- PÁGINA EM INGLÊS -->
<div class="version">
	<span class="px10">
		<html:link page="<%= "/showCourseSite.do?method=showCurricularCourseSite&amp;curricularCourseID=" +  pageContext.findAttribute("curricularCourseID") + "&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;degreeID=" +  request.getAttribute("degreeID") + "&amp;degreeCurricularPlanID=" + pageContext.getAttribute("degreeCurricularPlanID") + "&amp;executionDegreeID="  +  request.getAttribute("executionDegreeID")%>" >
			<bean:message key="label.version.portuguese" />
		</html:link>
		<img src="<%= request.getContextPath() %>/images/portugal-flag.gif" alt="Icon: Portuguese version!" width="16" height="12" />
	</span>
</div> 
<div class="clear"></div> 

<h1><bean:write name="infoDegree" property="tipoCurso" />&nbsp;<bean:write name="infoDegree" property="nome" /></h1>

<h2><span class="greytxt"><bean:write name="infoCurricularCourse" property="name" /></span></h2>
<br />
<!-- EXECUTION COURSES LINK  -->
<bean:size id="listSize" name="infoCurricularCourse" property="infoAssociatedExecutionCourses" />
<logic:greaterThan name="listSize" value="0">
<div class="col_right">
  <table class="box" cellspacing="0">
		<tr>
			<td class="box_header"><strong><bean:message key="label.courses.en" /></strong></td>
		</tr>
		<tr>
			<td class="box_cell">
				<logic:iterate id="infoExecutionCourse" name="infoCurricularCourse" property="infoAssociatedExecutionCourses">				
					<logic:notEqual name="infoExecutionCourse" property="infoExecutionPeriod.infoExecutionYear.state.stateCode" value="NO">
						<bean:define id="executionCourseID" name="infoExecutionCourse" property="idInternal" />
						<p>
						<html:link page="<%= "/viewSiteExecutionCourse.do?method=firstPage&amp;objectCode=" + pageContext.findAttribute("executionCourseID").toString() %>" target="_blank" >
							<bean:write name="infoExecutionCourse" property="nome" />&nbsp;(<bean:write name="infoExecutionCourse" property="infoExecutionPeriod.infoExecutionYear.year"/>&nbsp;-&nbsp;<bean:write name="infoExecutionCourse" property="infoExecutionPeriod.name"/>)
						</html:link>
						<br />
						</p>
					</logic:notEqual>
				</logic:iterate>
			</td>
		</tr>  
  </table>
</div>
</logic:greaterThan>
<!-- 	CURRICULAR COURSE SCOPES  -->
<h2><img alt="" height="12" src="<%= request.getContextPath() %>/images/icon_arrow.gif" width="12" />&nbsp;<bean:message key="label.scope.en" />	</h2>
<p>
	<ul>
		<logic:iterate id="infoCurricularCourseScope" name="infoCurricularCourse" property="infoScopes">
			<li>
				<logic:notEmpty name="infoCurricularCourseScope" property="infoBranch.name">
					<logic:notEqual name="infoCurricularCourseScope" property="infoBranch.name" value="">	
	 					<bean:write name="infoCurricularCourseScope" property="infoBranch.name"/>&nbsp;
	 				</logic:notEqual>
				</logic:notEmpty>
				
				<logic:notEmpty name="infoCurricularCourseScope" property="infoCurricularSemester.infoCurricularYear.year">
					<logic:notEqual name="infoCurricularCourseScope" property="infoCurricularSemester.infoCurricularYear.year" value="">					
						<bean:write name="infoCurricularCourseScope" property="infoCurricularSemester.infoCurricularYear.year"/>º&nbsp;<bean:message key="property.curricularCourse.curricularYear"/>&nbsp;
		 			</logic:notEqual>
				</logic:notEmpty>
				
				<logic:notEmpty name="infoCurricularCourseScope" property="infoCurricularSemester.semester">
					<logic:notEqual name="infoCurricularCourseScope" property="infoCurricularSemester.semester" value="">					
						<bean:write name="infoCurricularCourseScope" property="infoCurricularSemester.semester"/>º&nbsp;<bean:message key="property.curricularCourse.semester"/>&nbsp;
	 				</logic:notEqual>
				</logic:notEmpty>
			</li>
		</logic:iterate>		
	</ul>
</p>
<!-- CURRICULAR COURSE INFO -->
<logic:present name="infoCurriculum" property="generalObjectivesEn">
<logic:notEmpty name="infoCurriculum" property="generalObjectivesEn">
		<logic:notEqual name="infoCurriculum" property="generalObjectivesEn" value="">
		<h2><img alt="" height="12" src="<%= request.getContextPath() %>/images/icon_arrow.gif" width="12" />&nbsp;<bean:message key="message.generalObjectivesEn" />	</h2>
		<p>
			<bean:write name="infoCurriculum" property="generalObjectivesEn" filter="false"/>
		</p>
		</logic:notEqual>
</logic:notEmpty>
</logic:present>
<logic:present name="infoCurriculum" property="operacionalObjectivesEn">
<logic:notEmpty name="infoCurriculum" property="operacionalObjectivesEn">
		<logic:notEqual name="infoCurriculum" property="operacionalObjectivesEn" value="">
		<h2><img alt="" height="12" src="<%= request.getContextPath() %>/images/icon_arrow.gif" width="12" />&nbsp;<bean:message key="message.operacionalObjectivesEn" /></h2>
		<p>
			<bean:write name="infoCurriculum" property="operacionalObjectivesEn" filter="false"/>
		</p>
		</logic:notEqual>
</logic:notEmpty> 
</logic:present>
<br/>
<br/>
<logic:present name="infoCurriculum" property="programEn">
<logic:notEmpty name="infoCurriculum" property="programEn">
	<logic:notEqual name="infoCurriculum" property="programEn" value="">
		<h2><img alt="" height="12" src="<%= request.getContextPath() %>/images/icon_arrow.gif" width="12" />&nbsp;<bean:message key="message.programEn" /></h2>	
		<p>
			<bean:write name="infoCurriculum" property="programEn" filter="false" />
		</p>	
	</logic:notEqual>
</logic:notEmpty>
</logic:present>
<$--<br/> 
<br/> -->
</logic:present>
<logic:notPresent name="infoCurriculum" >
	<p><span class="error"><bean:message key="error.impossibleCurricularCourseInfo.en" /></span></p>
</logic:notPresent>