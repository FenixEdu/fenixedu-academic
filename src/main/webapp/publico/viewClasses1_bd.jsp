<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>
<%@ page import="net.sourceforge.fenixedu.domain.degree.DegreeType" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>


<bean:define id="institutionUrl" type="java.lang.String"><%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %></bean:define>
<div class="breadcumbs mvert0">
	<a href="<%= institutionUrl %>"><%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%></a>
	<bean:define id="institutionUrlTeaching" type="java.lang.String"><%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %><bean:message key="link.institution" bundle="GLOBAL_RESOURCES"/></bean:define>
	&nbsp;&gt;&nbsp;
	<a href="<%=institutionUrlTeaching%>"><bean:message  bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.education"/></a>
	<logic:present name="degree">
		&nbsp;&gt;&nbsp;
		<html:link page="<%= "/showDegreeSite.do?method=showDescription&amp;degreeID=" + request.getAttribute("degreeID").toString() %>"><bean:write name="degree" property="sigla"/></html:link>
		&nbsp;&gt;&nbsp;

		<logic:present name="<%=PresentationConstants.INFO_DEGREE_CURRICULAR_PLAN%>">
			<bean:define id="infoDegreeCurricularPlan" name="<%=PresentationConstants.INFO_DEGREE_CURRICULAR_PLAN%>"/>
		</logic:present>

		<html:link page="<%= "/showDegreeSite.do?method=showCurricularPlan&amp;degreeID=" + request.getAttribute("degreeID").toString() + "&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID").toString()+ "&amp;executionPeriodOID=" + request.getAttribute(PresentationConstants.EXECUTION_PERIOD_OID)  %>" ><bean:write name="infoDegreeCurricularPlan" property="name" /></html:link>
		&nbsp;&gt;&nbsp;
		<bean:message key="public.degree.information.label.classes" bundle="PUBLIC_DEGREE_INFORMATION" /> 
	</logic:present>
</div>

<!-- COURSE NAME -->
<h1>
	<logic:notEmpty name="degree" property="phdProgram">
		<bean:write name="degree" property="phdProgram.presentationName"/>
	</logic:notEmpty>
	<logic:empty name="degree" property="phdProgram">
		<bean:write name="degree" property="presentationName"/>
	</logic:empty>
</h1>

<logic:present name="infoDegreeCurricularPlan">

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

	<h2 class="arrow_bullet">
		<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.classes" />
	</h2>

	<logic:notPresent name="lista" scope="request">
		<p><em><bean:message bundle="DEFAULT" key="error.curricularPlanHasNoExecutionDegreesInNotClosedYears" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>"/></em></p>
	</logic:notPresent>
	<logic:present name="lista" scope="request">
		<bean:define id="listaNew" name="lista" />
		<html:form action="/chooseContextDANew.do" method="get">
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="<%="hidden." + PresentationConstants.EXECUTION_PERIOD_OID%>" property="<%=PresentationConstants.EXECUTION_PERIOD_OID%>" value="<%= ""+request.getAttribute(PresentationConstants.EXECUTION_PERIOD_OID)%>" />
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="nextPagePublic"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeID" property="degreeID" value="<%= ""+request.getAttribute("degreeID")%>" />
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanID" property="degreeCurricularPlanID" value="<%= pageContext.findAttribute("degreeCurricularPlanID").toString()%>" />
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.lista" property="lista" value="<%= pageContext.findAttribute("listaNew").toString()%>" />
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.nextPage" property="nextPage" value="classSearch"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.inputPage" property="inputPage" value="chooseContext"/>

			<table border="0" cellspacing="0" cellpadding="0">
				<tr>
				    <td><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.property.executionPeriod"/>:</td>
				    <td>
						<html:select bundle="HTMLALT_RESOURCES" property="indice" size="1" onchange='this.form.submit();'>
							<logic:notEmpty name="chooseSearchContextForm" property="indice" >
								<bean:define id="ind" name="chooseSearchContextForm" property="indice" />	
							</logic:notEmpty>
							<html:options property="value" labelProperty="label" collection="lista"/>
						</html:select>
						<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
							<bean:message key="button.submit"/>
						</html:submit>
				    </td>
				</tr>
			</table>
		</html:form> 

<br/>

		<logic:present name="classList">	
			<table class="tab_lay" cellspacing="0" cellpadding="0" width="50%">
				<tr>
					<th><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.property.class"/></th>
					<th><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.semester"/></th>
					<th><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.curricularYear"/></th>
				</tr>		
				<logic:iterate id="classview" name="classList" indexId="row">
					<% String rowColor = row.intValue() % 2 == 0 ? "bgwhite" : "bluecell" ; %>
					<bean:define id="classId" name="classview" property="externalId"/>
					<tr>
					    <td class="<%= rowColor %>">	
							<html:link page="<%= "/viewClassTimeTableNew.do?executionPeriodOID="
							+ pageContext.findAttribute(PresentationConstants.EXECUTION_PERIOD_OID)
							+ "&amp;classId="
							+ pageContext.findAttribute("classId")
							+ "&amp;degreeInitials="
							+ pageContext.findAttribute("degreeInitials")
							+ "&amp;degreeID="
							+ request.getAttribute("degreeID")
							+ "&amp;degreeCurricularPlanID="
							+ request.getAttribute("degreeCurricularPlanID")  %>" paramId="className" paramName="classview" paramProperty="nome">
								<jsp:getProperty name="classview" property="nome"/>
							</html:link>
						</td>
						<td class="<%= rowColor %>"><bean:write name="classview" property="infoExecutionPeriod.semester"/></td>
						<td class="<%= rowColor %>"><jsp:getProperty name="classview" property="anoCurricular"/></td>
					</tr>
				</logic:iterate>
			</table>
		</logic:present>
		<logic:notPresent name="classList" >
			<span class="error"><!-- Error messages go here --><em><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.message.public.notfound.classes"/></em></span>
		</logic:notPresent>

	</logic:present>
	
</logic:present>
