<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/><%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %><%@ taglib uri="/WEB-INF/app.tld" prefix="app" %><%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %><%@ page import="net.sourceforge.fenixedu.domain.degree.DegreeType" %><bean:define id="institutionUrl" type="java.lang.String">
	<bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/>
</bean:define>
<div class="breadcumbs">
	<a href="<%= institutionUrl %>">
		<bean:message key="institution.name.abbreviation" bundle="GLOBAL_RESOURCES"/>
	</a>
	<bean:define id="institutionUrlTeaching" type="java.lang.String">
		<bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/>
		<bean:message key="link.institution" bundle="GLOBAL_RESOURCES"/>
	</bean:define>
	&nbsp;&gt;&nbsp;
	<a href="<%=institutionUrlTeaching%>">
		<bean:message  bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.education"/>
	</a>
	<logic:present name="degree">
		&nbsp;&gt;&nbsp;
		<html:link page="<%= "/showDegreeSite.do?method=showDescription&amp;degreeID=" + request.getAttribute("degreeID").toString() %>">
			<bean:write name="degree" property="sigla"/>
		</html:link>
		<logic:present name="infoDegreeCurricularPlan" >
			&nbsp;&gt;&nbsp;
			<html:link page="<%= "/showDegreeSite.do?method=showCurricularPlan&amp;degreeID=" + request.getAttribute("degreeID").toString() + "&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID").toString()+ "&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID)  %>" >
				<bean:write name="infoDegreeCurricularPlan" property="name" />
			</html:link>
			&nbsp;&gt;&nbsp;
			<bean:message  key="public.degree.information.label.exams" bundle="PUBLIC_DEGREE_INFORMATION" />
		</logic:present>
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

<logic:notPresent name="infoDegreeCurricularPlan" >
	<p><em><bean:message bundle="DEFAULT" key="error.impossibleExecutionDegreeList"/></em></p>
</logic:notPresent>

<logic:present name="infoDegreeCurricularPlan" >

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
		<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.exams" />
	</h2>

	<logic:notPresent name="lista" scope="request">
		<p><em><bean:message bundle="DEFAULT" key="error.curricularPlanHasNoExecutionDegreesInNotClosedYears"/></em></p>
	</logic:notPresent>
	<logic:present name="lista" scope="request">
		<bean:define id="listaNew" name="lista" />
		<html:form action="/chooseExamsMapContextDANew.do" method="GET">
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.<%SessionConstants.EXECUTION_PERIOD_OID%>" property="<%SessionConstants.EXECUTION_PERIOD_OID%>" value="<%= ""+request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID)%>" />
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="choose"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeID" property="degreeID" value="<%= ""+request.getAttribute("degreeID")%>" />
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanID" property="degreeCurricularPlanID" value="<%= pageContext.findAttribute("degreeCurricularPlanID").toString()%>" />
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.lista" property="lista" value="<%= pageContext.findAttribute("listaNew").toString()%>" />
			
			<table border="0" cellspacing="0" cellpadding="0">
				<tr>
				    <td><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.property.executionPeriod"/>:</td>
				    <td>
						<html:select bundle="HTMLALT_RESOURCES" altKey="select.indice" property="indice" size="1" onchange="this.form.submit();">
							<html:options property="value" labelProperty="label" collection="lista"/>
						</html:select>
				    </td>
				    <td>
						<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
							<bean:message key="button.submit"/>
						</html:submit>
					</td>
				</tr>
			</table>
		</html:form>

		<logic:notPresent name="<%=SessionConstants.INFO_EXAMS_MAP%>">
			<p><em><bean:message bundle="DEFAULT" key="error.curricularPlanHasNoExecutionDegreesInGivenPeriod"/></em></p>
		</logic:notPresent>
		<logic:present name="<%=SessionConstants.INFO_EXAMS_MAP%>">
			<div>
				<app:generateNewExamsMap name="<%= SessionConstants.INFO_EXAMS_MAP %>" user="public" mapType=" "/>
			</div>
		</logic:present>
	</logic:present>

</logic:present>
