<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><%@page import="net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.domain.degree.DegreeType" %>

<logic:present name="infoDegreeCurricularPlan">
	<bean:define id="degreeCurricularPlanID" name="infoDegreeCurricularPlan" property="idInternal" />

<bean:define id="institutionUrl" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/></bean:define>
<div class="breadcumbs mvert0"><a href="<%= institutionUrl %>"><bean:message key="institution.name.abbreviation" bundle="GLOBAL_RESOURCES"/></a>
<bean:define id="institutionUrlTeaching" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/><bean:message key="link.institution" bundle="GLOBAL_RESOURCES"/></bean:define>
&nbsp;&gt;&nbsp;<a href="<%= institutionUrlTeaching %>"><bean:message key="public.degree.information.label.education" bundle="PUBLIC_DEGREE_INFORMATION" /></a>
		<bean:define id="degreeType" name="infoDegreeCurricularPlan" property="infoDegree.tipoCurso" />	
		&nbsp;&gt;&nbsp;		
		<html:link page="<%= "/showDegreeSite.do?method=showDescription&amp;degreeID=" + request.getAttribute("degreeID").toString() %>">
			<bean:write name="infoDegreeCurricularPlan" property="infoDegree.sigla" />
		</html:link>
		&nbsp;&gt;&nbsp;
		<html:link page="<%= "/showDegreeSite.do?method=showCurricularPlan&amp;degreeID=" + request.getAttribute("degreeID").toString() + "&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID").toString()%>" >

		<bean:write name="infoDegreeCurricularPlan" property="name" />
	</html:link>
	&nbsp;&gt;&nbsp;<bean:message bundle="PUBLIC_DEGREE_INFORMATION"  key="public.degree.information.label.curriculum"/>		
</div>	

	<h1>
		<bean:message bundle="ENUMERATION_RESOURCES"
			name="infoDegreeCurricularPlan" property="infoDegree.tipoCurso.name" />
		<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.in" />
		<bean:write name="infoDegreeCurricularPlan" property="infoDegree.nome" />
	</h1>

	<!-- CURRICULAR PLAN -->
	<h2 class="greytxt">
		<bean:message key="public.degree.information.label.curricularPlan"  bundle="PUBLIC_DEGREE_INFORMATION" />
		<bean:write name="infoDegreeCurricularPlan" property="name"/>
		<logic:notEmpty name="infoDegreeCurricularPlan" property="initialDate">
			<logic:empty name="infoDegreeCurricularPlan" property="endDate">
				(<bean:message bundle="PUBLIC_DEGREE_INFORMATION"  key="public.degree.information.label.since" />
				<bean:define id="initialDate" name="infoDegreeCurricularPlan" property="initialDate" />
				<%= initialDate.toString().substring(initialDate.toString().lastIndexOf(" ")+1) %>)
			</logic:empty>
			<logic:notEmpty name="infoDegreeCurricularPlan" property="endDate">
				(<bean:message bundle="PUBLIC_DEGREE_INFORMATION"  key="public.degree.information.label.of" />
				<bean:define id="initialDate" name="infoDegreeCurricularPlan" property="initialDate" />
				<%= initialDate.toString().substring(initialDate.toString().lastIndexOf(" ")+1) %>
				<bean:message bundle="PUBLIC_DEGREE_INFORMATION"  key="public.degree.information.label.to" />
				<bean:define id="endDate" name="infoDegreeCurricularPlan" property="endDate" />	
				<%= endDate.toString().substring(endDate.toString().lastIndexOf(" ")) %>)
			</logic:notEmpty>
		</logic:notEmpty>	
	</h2>

	<html:form action="/prepareConsultCurricularPlanNew.do" method="get" >
		<html:hidden alt="<%= SessionConstants.EXECUTION_PERIOD_OID %>" property="<%= SessionConstants.EXECUTION_PERIOD_OID %>" value="<%= ""+request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID)%>" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeID" property="degreeID" value="<%= ""+request.getAttribute("degreeID")%>" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanID" property="degreeCurricularPlanID" value="<%= pageContext.findAttribute("degreeCurricularPlanID").toString() %>" />	
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.index" property="index" value="<%= ""+ request.getAttribute("index")%>"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="select"/>
		<html:hidden alt="<%= SessionConstants.EXECUTION_DEGREE%>" property="<%= SessionConstants.EXECUTION_DEGREE%>" value="<%= "" + request.getAttribute(SessionConstants.EXECUTION_DEGREE)%>"/>		
		
		<table border="0" cellspacing="2" cellpadding="0">			
			<tr>
				<td><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.executionYear"/>:</td>
				<td>
					<html:select bundle="HTMLALT_RESOURCES" property="indice" size="1" onchange="this.form.submit();">
						<html:options property="value" labelProperty="label" collection="<%= SessionConstants.LABELLIST_EXECUTIONPERIOD%>"/>
						<bean:define id="indiceID" name="chooseContextDegreeForm" property="indice"/>
					</html:select>
					<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
						<bean:message key="button.submit"/>
					</html:submit>
				</td>
			</tr>
			<tr>
				<td><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.curricularYear"/>:</td>
				<td>
					<html:select bundle="HTMLALT_RESOURCES" property="curYear" size="1" onchange="this.form.submit();">
						<html:options collection="curricularYearList" property="value" labelProperty="label"/>
					</html:select>			
					<html:submit styleId="javascriptButtonID2" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
						<bean:message key="button.submit"/>
					</html:submit>	
				</td>
			</tr>
		</table>
	</html:form> 
	
	<br/>
	
	<em><span class="error"><!-- w3c complient--><html:errors/></span></em>
	
	<logic:present name="allActiveCurricularCourseScopes">
	<logic:notEmpty name="allActiveCurricularCourseScopes">

	<logic:iterate id="curricularCourseScopeElemList" name="allActiveCurricularCourseScopes" length="1">
		<logic:iterate id="curricularCourseScopeElem" name="curricularCourseScopeElemList" type="net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope" length="1">
			<bean:define id="currentYear" name="curricularCourseScopeElem" property="infoCurricularSemester.infoCurricularYear.year"/>
		</logic:iterate>
		<logic:iterate id="curricularCourseScopeElem" name="curricularCourseScopeElemList" type="net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope" length="1">
			<bean:define id="currentCode" name="curricularCourseScopeElem" property="infoBranch.code"/>
		</logic:iterate>
	</logic:iterate>

	<table class="tab_lay" cellspacing="0" cellpadding="5">
		<tr>
			<% if (pageContext.findAttribute("indiceID").toString().equals("44")){%>
				<th colspan="11" scope="col">
				<logic:equal name="<%= org.apache.struts.Globals.LOCALE_KEY %>" property="language" value="en">
					<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.year"/>&nbsp;<bean:write name="currentYear"/>
				</logic:equal>
				<logic:equal name="<%= org.apache.struts.Globals.LOCALE_KEY %>" property="language" value="pt">
					<bean:write name="currentYear"/>&ordm;&nbsp;<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.year"/>
				</logic:equal>
				</th>
			<%}else{ %>
				<th colspan="11" scope="col">
				<logic:equal name="<%= org.apache.struts.Globals.LOCALE_KEY %>" property="language" value="en">
					<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.year"/>&nbsp;<bean:write name="currentYear"/>
				</logic:equal>
				<logic:equal name="<%= org.apache.struts.Globals.LOCALE_KEY %>" property="language" value="pt">
					<bean:write name="currentYear"/>&ordm;&nbsp;<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.year"/>
				</logic:equal>
				</th>
			<%}%>
		</tr>
		<tr>
			<% if (pageContext.findAttribute("indiceID").toString().equals("44")){%>		
				<th colspan="11" scope="col">
					 <center><!--Branch name goes here--><bean:write name="curricularCourseScopeElem" property="infoBranch.name"/></center>
				</th>
			<%}else{ %>
				<th colspan="11" scope="col">
					 <center><!--Branch name goes here--><bean:write name="curricularCourseScopeElem" property="infoBranch.name"/></center>
				</th>
			<%}%>
		</tr>
		<tr>						
			<% if (pageContext.findAttribute("indiceID").toString().equals("44")){%>				
				<td colspan="7" class="subheader">&nbsp;</td>
				<td colspan="4" class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.hoursPerWeek" /></td>
			<%}else{ %>
				<td colspan="7" class="subheader">&nbsp;</td>
				<td colspan="4" class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.hoursPerWeek" /></td>
			<%}%>
		</tr>
		<tr>
			<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.semester.abbr"/></td>
			<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.curricularCourse"/></td>
			<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.anotation"/></td>
			<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.type"/></td>
			<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.credits"/></td>
			<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.ects"/></td>
			<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.weight"/></td>
			<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.theoretical.abbr"/></td>
			<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.pratical.abbr"/></td>
			<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.theoPrat.abbr"/></td>
			<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.laboratorial.abbr"/></td>
		</tr>
		
		<% int count=0; %>
		<logic:iterate id="curricularCourseScopeElemList" name="allActiveCurricularCourseScopes" indexId="row">
			<logic:iterate id="curricularCourseScopeElem" name="curricularCourseScopeElemList" type="net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope" length="1">
				<logic:notEqual name="curricularCourseScopeElem" property="infoBranch.code" value="<%= pageContext.findAttribute("currentCode").toString()%>">
				  <logic:equal name="curricularCourseScopeElem" property="infoCurricularSemester.infoCurricularYear.year" value="<%= pageContext.findAttribute("currentYear").toString()%>">					 
						<tr>
							<th colspan="11" scope="col">
								 <center><!--Branch name goes here--><bean:write name="curricularCourseScopeElem" property="infoBranch.name"/></center>
							</th>
						</tr>
						<bean:define id="currentCode" name="curricularCourseScopeElem" property="infoBranch.code"/>
						<bean:define id="currentYear" name="curricularCourseScopeElem" property="infoCurricularSemester.infoCurricularYear.year"/>
						<tr>
							<td colspan="7" class="subheader">&nbsp;</td>
							<td colspan="4" class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.hoursPerWeek" /></td>
						</tr>
						
						<tr>						
							<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.semester.abbr"/></td>
							<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.curricularCourse"/></td>
							<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.anotation"/></td>
							<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.type"/></td>
							<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.credits"/></td>
							<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.ects"/></td>
							<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.weight"/></td>							
							<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.theoretical.abbr"/></td>
							<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.pratical.abbr"/></td>
							<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.theoPrat.abbr"/></td>
							<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.laboratorial.abbr"/></td>
						</tr>
					</logic:equal>
					</logic:notEqual>
			        <logic:equal name="curricularCourseScopeElem" property="infoBranch.code" value="<%= pageContext.findAttribute("currentCode").toString()%>">
					<logic:notEqual name="curricularCourseScopeElem" property="infoCurricularSemester.infoCurricularYear.year" value="<%= pageContext.findAttribute("currentYear").toString()%>">
					</table>
					<br/>
					 <table class="tab_lay" cellspacing="0" cellpadding="5">
					 
					<% if (row.intValue() % 2 !=0) count=1; else count=0; %>
	
					<tr>
						<th colspan="11" scope="col">
							<logic:equal name="<%= org.apache.struts.Globals.LOCALE_KEY %>" property="language" value="en">
								<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.year"/>&nbsp;<bean:write name="curricularCourseScopeElem" property="infoCurricularSemester.infoCurricularYear.year"/>
							</logic:equal>
							<logic:equal name="<%= org.apache.struts.Globals.LOCALE_KEY %>" property="language" value="pt">
								<bean:write name="curricularCourseScopeElem" property="infoCurricularSemester.infoCurricularYear.year"/>&ordm;&nbsp;<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.year"/>
							</logic:equal>
						</th>
					</tr>
					<bean:define id="currentYear" name="curricularCourseScopeElem" property="infoCurricularSemester.infoCurricularYear.year"/>
					<bean:define id="currentCode" name="curricularCourseScopeElem" property="infoBranch.code"/>
					<tr>
						<th colspan="11" scope="col">
							 <center><!--Branch name goes here--><bean:write name="curricularCourseScopeElem" property="infoBranch.name"/></center>
						</th>
						</tr>
					<tr>
						<td colspan="7" class="subheader">&nbsp;</td>
						<td colspan="4" class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.hoursPerWeek" /></td>
					</tr>
					
					<tr>						
						<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.semester.abbr"/></td>
						<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.curricularCourse"/></td>
						<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.anotation"/></td>
						<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.type"/></td>
						<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.credits"/></td>
						<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.ects"/></td>
						<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.weight"/></td>							
						<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.theoretical.abbr"/></td>
						<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.pratical.abbr"/></td>
						<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.theoPrat.abbr"/></td>
						<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.laboratorial.abbr"/></td>
					</tr>
					</logic:notEqual>
				</logic:equal>
				<logic:notEqual name="curricularCourseScopeElem" property="infoBranch.code" value="<%= pageContext.findAttribute("currentCode").toString()%>">
					 <logic:notEqual name="curricularCourseScopeElem" property="infoCurricularSemester.infoCurricularYear.year" value="<%= pageContext.findAttribute("currentYear").toString()%>">
					
					</table>
					<br/>
					 <table class="tab_lay" cellspacing="0" cellpadding="5">
					 <% if (row.intValue() % 2 !=0) count=1; else count=0; %>

					<tr>
						<th colspan="11" scope="col">
							<logic:equal name="<%= org.apache.struts.Globals.LOCALE_KEY %>" property="language" value="en">
							<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.year"/>&nbsp;<bean:write name="curricularCourseScopeElem" property="infoCurricularSemester.infoCurricularYear.year"/>
						</logic:equal>
						<logic:equal name="<%= org.apache.struts.Globals.LOCALE_KEY %>" property="language" value="pt">
							<bean:write name="curricularCourseScopeElem" property="infoCurricularSemester.infoCurricularYear.year"/>&ordm;&nbsp;<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.year"/>
						</logic:equal>
						</th>
					</tr>
					<bean:define id="currentYear" name="curricularCourseScopeElem" property="infoCurricularSemester.infoCurricularYear.year"/>
					<bean:define id="currentCode" name="curricularCourseScopeElem" property="infoBranch.code"/>
					<tr>
						<th colspan="11" scope="col">
							 <center><!--Branch name goes here--><bean:write name="curricularCourseScopeElem" property="infoBranch.name"/></center>
						</th>
					</tr>
					<tr>
						<td colspan="7" class="subheader">&nbsp;</td>
						<td colspan="4" class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.hoursPerWeek" /></td>
					</tr>
					
					<tr>						
						<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.semester.abbr"/></td>
						<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.curricularCourse"/></td>
						<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.anotation"/></td>
						<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.type"/></td>
						<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.credits"/></td>
						<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.ects"/></td>
						<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.weight"/></td>							
						<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.theoretical.abbr"/></td>
						<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.pratical.abbr"/></td>
						<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.theoPrat.abbr"/></td>
						<td class="subheader"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.laboratorial.abbr"/></td>
					</tr>
				</logic:notEqual>
				</logic:notEqual>
			</logic:iterate>
			
			<bean:size id="numberOfScopes" name="curricularCourseScopeElemList"/>
			<% String rowColor = (row.intValue()+count) % 2 == 0 ? "bgwhite" : "bluecell" ; %>						
			<logic:iterate id="curricularCourseScopeElem" name="curricularCourseScopeElemList" type="net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope" length="1">
				<tr>
					<td class="<%= rowColor %>"><bean:write name="curricularCourseScopeElem" property="infoCurricularSemester.semester"/></td>
					<td class="<%= rowColor %>">
						<bean:define id="curricularCourseID" name="curricularCourseScopeElem" property="infoCurricularCourse.idInternal"/>
						<html:link page="<%= "/showCourseSite.do?method=showCurricularCourseSite&amp;curricularCourseID=" +  pageContext.findAttribute("curricularCourseID") + "&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;degreeID=" +  request.getAttribute("degreeID") + "&amp;degreeCurricularPlanID=" + request.getAttribute("degreeCurricularPlanID")%>" >
							<bean:write name="curricularCourseScopeElem" property="infoCurricularCourse.name" />
						</html:link>
					</td>
					<logic:notEmpty name="curricularCourseScopeElem" property="anotation" >
						<td class="<%= rowColor %>"><bean:write name="curricularCourseScopeElem" property="anotation"/></td>
					</logic:notEmpty>
					<logic:empty name="curricularCourseScopeElem" property="anotation" >
						<td class="<%= rowColor %>"></td>
					</logic:empty>
					<td class="<%= rowColor %>"><bean:message bundle="ENUMERATION_RESOURCES" name="curricularCourseScopeElem" property="infoCurricularCourse.type.name"/></td>
					<td class="<%= rowColor %>"><bean:write name="curricularCourseScopeElem" property="infoCurricularCourse.credits"/></td>
					<td class="<%= rowColor %>"><bean:write name="curricularCourseScopeElem" property="infoCurricularCourse.ectsCredits"/></td>
					<!-- this is only for 2006/2007 execution year -->
					<bean:define id="execDegree" name="exeDegree" type="net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree"/>
					<% if(execDegree.getInfoExecutionYear().getIdInternal() < 45) { %>
						<td class="<%= rowColor %>"><bean:write name="curricularCourseScopeElem" property="infoCurricularCourse.weigth"/></td>
					<% } else { %>
						<td class="<%= rowColor %>"><bean:write name="curricularCourseScopeElem" property="infoCurricularCourse.ectsCredits"/></td>
					<% } %>						
					<!-- end -->
					<td class="<%= rowColor %>"><bean:write name="curricularCourseScopeElem" property="infoCurricularCourse.theoreticalHours"/></td>
					<td class="<%= rowColor %>"><bean:write name="curricularCourseScopeElem" property="infoCurricularCourse.praticalHours"/></td>
					<td class="<%= rowColor %>"><bean:write name="curricularCourseScopeElem" property="infoCurricularCourse.theoPratHours"/></td>
					<td class="<%= rowColor %>"><bean:write name="curricularCourseScopeElem" property="infoCurricularCourse.labHours"/></td>					
				</tr>	
			</logic:iterate>
		
		</logic:iterate>
		
	</table>

	<logic:present name="executionDegree" property="anotation">
		<pre><bean:write name="executionDegree" property="anotation.content"/></pre>
	</logic:present>

</logic:notEmpty>
</logic:present>

<logic:empty name="allActiveCurricularCourseScopes">
	<em><span class="error"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.error.impossibleCurricularPlan" /></span></em>
</logic:empty>
	
</logic:present>