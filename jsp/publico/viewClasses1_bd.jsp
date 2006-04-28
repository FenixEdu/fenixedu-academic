<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="net.sourceforge.fenixedu.domain.degree.DegreeType" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>

<bean:define id="institutionUrl" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/></bean:define>
<div class="breadcumbs"><a href="<%= institutionUrl %>"><bean:message key="institution.name.abbreviation" bundle="GLOBAL_RESOURCES"/></a> >
<bean:define id="institutionUrlTeaching" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/><bean:message key="link.institution" bundle="GLOBAL_RESOURCES"/></bean:define>
&nbsp;&gt;&nbsp;<a href="<%= institutionUrlTeaching %>"><bean:message key="public.degree.information.label.education" bundle="PUBLIC_DEGREE_INFORMATION" /></a>
	<bean:define id="degreeType" name="<%= SessionConstants.INFO_DEGREE_CURRICULAR_PLAN %>" property="infoDegree.tipoCurso" />
	<bean:define id="infoDegreeCurricularPlan" name="<%= SessionConstants.INFO_DEGREE_CURRICULAR_PLAN %>"/>
	&nbsp;&gt;&nbsp;
	<html:link page="<%= "/showDegreeSite.do?method=showDescription&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;degreeID=" + request.getAttribute("degreeID").toString()  %>">
		<bean:write name="infoDegreeCurricularPlan" property="infoDegree.sigla" />
	</html:link>
	&nbsp;&gt;&nbsp;
	<html:link page="<%= "/showDegreeSite.do?method=showCurricularPlan&amp;degreeID=" + request.getAttribute("degreeID") + "&amp;degreeCurricularPlanID=" + request.getAttribute("degreeCurricularPlanID") + "&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) %>" >
		<bean:message key="public.degree.information.label.curricularPlan"  bundle="PUBLIC_DEGREE_INFORMATION" />
	</html:link>
	&nbsp;&gt;&nbsp;<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.classes"/> 	
</div>	

<%--
<!-- PÃ?ï¿½GINA EM INGLÃŠï¿½S -->
	<div class="version">
		<span class="px10">
			<html:link page="<%= "/showDegreeSite.do?method=showCurricularPlan&amp;inEnglish=true&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;degreeID=" +  request.getAttribute("degreeID") %>" >english version</html:link> <img src="<%= request.getContextPath() %>/images/icon_uk.gif" alt="Icon: English version!" width="16" height="12" />
	</span>	
	</div>--%>

<h1>
    <bean:define id="degreeType" name="infoDegreeCurricularPlan" property="infoDegree.tipoCurso.name"/>
    <logic:equal name="degreeType" value="DEGREE" >
	    <bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.degreeType" />
	</logic:equal>    
	<logic:equal name="degreeType" value="MASTER_DEGREE" >
	    <bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.masterDegreeType" />
	</logic:equal>  
	<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.in" />
	<bean:write name="infoDegreeCurricularPlan" property="infoDegree.nome" />
</h1>

<h2 class="greytxt">
	<bean:message key="public.degree.information.label.curricularPlan"  bundle="PUBLIC_DEGREE_INFORMATION" />
	<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.of" />
	<bean:define id="initialDate" name="infoDegreeCurricularPlan" property="initialDate" />		
	<%= initialDate.toString().substring(initialDate.toString().lastIndexOf(" ")+1) %>
	<logic:notEmpty name="infoDegreeCurricularPlan" property="endDate">
		<bean:define id="endDate" name="infoDegreeCurricularPlan" property="endDate" />	
		-<%= endDate.toString().substring(endDate.toString().lastIndexOf(" ")) %>
	</logic:notEmpty>
</h2>

<logic:present name="lista" scope="request">
	<bean:define id="listaNew" name="lista" />
	<html:form action="/chooseContextDANew.do" method="GET">
		<html:hidden property="<%SessionConstants.EXECUTION_PERIOD_OID%>" value="<%= ""+request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID)%>" />
		<html:hidden property="page" value="1"/>
		<html:hidden property="method" value="nextPagePublic"/>
		<html:hidden property="degreeID" value="<%= ""+request.getAttribute("degreeID")%>" />
		<html:hidden property="degreeCurricularPlanID" value="<%= pageContext.findAttribute("degreeCurricularPlanID").toString()%>" />
		<html:hidden property="lista" value="<%= pageContext.findAttribute("listaNew").toString()%>" />
		<html:hidden property="nextPage" value="classSearch"/>
		<html:hidden property="inputPage" value="chooseContext"/>
		

		<table border="0" cellspacing="0" cellpadding="0">
			<tr>
			    <td><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.property.executionPeriod"/>:</td>
			    <td>
					<html:select property="indice" size="1" onchange='this.form.submit();'>
						<logic:notEmpty name="chooseSearchContextForm" property="indice" >
							<bean:define id="ind" name="chooseSearchContextForm" property="indice" />	
						</logic:notEmpty>
						<html:options property="value" labelProperty="label" collection="lista"/>
					</html:select>
			    </td>
			</tr>
		</table>
	</html:form> 
</logic:present>

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
				<html:link page="<%= "/viewClassTimeTableNew.do?executionPeriodOID=" + pageContext.findAttribute(SessionConstants.EXECUTION_PERIOD_OID)+ "&amp;classId="+pageContext.findAttribute("classId") + "&amp;nameDegreeCurricularPlan=" +pageContext.findAttribute("nameDegreeCurricularPlan")+ "&amp;degreeInitials=" +pageContext.findAttribute("degreeInitials")+ "&amp;degreeID=" + request.getAttribute("degreeID") + "&amp;degreeCurricularPlanID=" + request.getAttribute("degreeCurricularPlanID")  %>" paramId="className" paramName="classview" paramProperty="nome">
					<jsp:getProperty name="classview" property="nome"/>
				</html:link>
			</td>
			<td class="<%= rowColor %>"><bean:write name="classview" property="infoExecutionPeriod.semester"/></td>
			<td class="<%= rowColor %>"><jsp:getProperty name="classview" property="anoCurricular"/></td>
		</tr>	
	</logic:iterate>
</table>
</logic:present>

<logic:notPresent name="classview"  >	
	<table align="center" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td><span class="error"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.message.public.notfound.classes"/></span></td>
		</tr>
	</table>
</logic:notPresent>
