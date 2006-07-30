<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="net.sourceforge.fenixedu.domain.degree.DegreeType" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>


<bean:define id="institutionUrl" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/></bean:define>
<div class="breadcumbs">
	<a href="<%= institutionUrl %>"><bean:message key="institution.name.abbreviation" bundle="GLOBAL_RESOURCES"/></a>
	<bean:define id="institutionUrlTeaching" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/><bean:message key="link.institution" bundle="GLOBAL_RESOURCES"/></bean:define>
	&nbsp;&gt;&nbsp;
	<a href="<%=institutionUrlTeaching%>"><bean:message  bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.education"/></a>
	<logic:present name="degree">
		&nbsp;&gt;&nbsp;
		<html:link page="<%= "/showDegreeSite.do?method=showDescription&amp;degreeID=" + request.getAttribute("degreeID").toString() %>"><bean:write name="degree" property="sigla"/></html:link>
		&nbsp;&gt;&nbsp;

		<logic:present name="<%=SessionConstants.INFO_DEGREE_CURRICULAR_PLAN%>">
			<bean:define id="infoDegreeCurricularPlan" name="<%=SessionConstants.INFO_DEGREE_CURRICULAR_PLAN%>"/>
		</logic:present>

		<html:link page="<%= "/showDegreeSite.do?method=showCurricularPlan&amp;degreeID=" + request.getAttribute("degreeID").toString() + "&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID").toString()+ "&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID)  %>" ><bean:write name="infoDegreeCurricularPlan" property="name" /></html:link>
		&nbsp;&gt;&nbsp;
		<bean:message key="public.degree.information.label.classes" bundle="PUBLIC_DEGREE_INFORMATION" /> 
	</logic:present>
</div>

<!-- COURSE NAME -->
<h1>
	<logic:equal name="degree" property="bolonhaDegree" value="true">
		<bean:message bundle="ENUMERATION_RESOURCES" name="degree" property="bolonhaDegreeType.name"/>
	</logic:equal>
	<logic:equal name="degree" property="bolonhaDegree" value="false">
		<bean:message bundle="ENUMERATION_RESOURCES" name="degree" property="tipoCurso.name"/>
	</logic:equal>
	<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.in"/>
	<logic:present name="inEnglish">
		<logic:equal name="inEnglish" value="false">
			<bean:write name="degree" property="nome"/>
		</logic:equal>
		<logic:equal name="inEnglish" value="true">
			<bean:write name="degree" property="nameEn"/>
		</logic:equal>
	</logic:present>
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
		<p><em><bean:message bundle="DEFAULT" key="error.curricularPlanHasNoExecutionDegreesInNotClosedYears"/></em></p>
	</logic:notPresent>
	<logic:present name="lista" scope="request">
		<bean:define id="listaNew" name="lista" />
		<html:form action="/chooseContextDANew.do" method="get">
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.<%SessionConstants.EXECUTION_PERIOD_OID%>" property="<%SessionConstants.EXECUTION_PERIOD_OID%>" value="<%= ""+request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID)%>" />
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
						<html:select bundle="HTMLALT_RESOURCES" altKey="select.indice" property="indice" size="1" onchange='this.form.submit();'>
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

		<logic:present name="classList">	
			<table class="tab_lay" cellspacing="0" cellpadding="0" width="50%">
				<tr>
					<th><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.property.class"/></th>
					<th><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.semester"/></th>
					<th><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.curricularYear"/></th>
				</tr>		
				<logic:iterate id="classview" name="classList" indexId="row">
					<% String rowColor = row.intValue() % 2 == 0 ? "white" : "bluecell" ; %>
					<bean:define id="classId" name="classview" property="idInternal"/>
					<tr>
					    <td class="<%= rowColor %>">	
							<html:link page="<%= "/viewClassTimeTableNew.do?executionPeriodOID="
							+ pageContext.findAttribute(SessionConstants.EXECUTION_PERIOD_OID)
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
