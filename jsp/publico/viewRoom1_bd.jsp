<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.TimeTableType" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState" %>
<%@ page import="net.sourceforge.fenixedu.domain.degree.DegreeType" %>


<bean:define id="component" name="siteView" property="component"/>
<bean:define id="infoRoom" name="component" property="infoRoom" />
<bean:define id="lessonList" name="component" property="infoLessons" />

<br/> 
<logic:present name="infoDegreeCurricularPlan">
    <br/>
			<bean:define id="degreeType" name="infoDegreeCurricularPlan" property="infoDegree.tipoCurso" />	
			<bean:define id="degreeCurricularPlanID" name="infoDegreeCurricularPlan" property="idInternal" />
			<bean:define id="institutionUrl" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/></bean:define>
				<div class="breadcumbs"><a href="<%= institutionUrl %>"><bean:message key="institution.name.abbreviation" bundle="GLOBAL_RESOURCES"/></a>
				<bean:define id="degreeType" name="infoDegreeCurricularPlan" property="infoDegree.tipoCurso" />	
				<logic:equal name="degreeType" value="<%= DegreeType.MASTER_DEGREE.toString() %>">
					 <html:link page="<%= "/showDegrees.do?method=master&executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) %>" >Ensino Mestrados</html:link>
				</logic:equal>
				<logic:equal name="degreeType" value="<%= DegreeType.DEGREE.toString() %>">
					<html:link page="<%= "/showDegrees.do?method=nonMaster&executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) %>" >Ensino Licenciaturas</html:link>		
				</logic:equal>
				&gt;&nbsp;
				<html:link page="<%= "/showDegreeSite.do?method=showDescription&amp;degreeID=" + request.getAttribute("degreeID").toString()%>">
					<bean:write name="infoDegreeCurricularPlan" property="infoDegree.sigla" />
				</html:link>&gt;&nbsp;
				<html:link page="<%= "/showDegreeSite.do?method=showCurricularPlan&amp;degreeID=" + request.getAttribute("degreeID") + "&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID").toString() + "&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;executionDegreeID="  + "&amp;index=" + request.getAttribute("index")  %>" >
					<bean:write name="infoDegreeCurricularPlan" property="name" />
				</html:link>&gt;&nbsp; 
				<html:link page="<%= "/chooseContextDANew.do?method=nextPagePublic&nextPage=classSearch&inputPage=chooseContext&executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID)+ "&amp;degreeID=" + request.getAttribute("degreeID")+ "&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID").toString() %>" >
					<bean:message  key="public.degree.information.label.classes"  bundle="PUBLIC_DEGREE_INFORMATION" />
				</html:link>&gt;&nbsp; 
				<%= request.getAttribute("sigla").toString() %>
		</div>	
	
		<div class="clear"></div> 
		<h1><bean:write name="infoDegreeCurricularPlan" property="infoDegree.tipoCurso" />&nbsp;<bean:write name="infoDegreeCurricularPlan" property="infoDegree.nome" /></h1>
		
		<h2>
		<span class="greytxt">
			<bean:message key="public.degree.information.label.curricularPlan"  bundle="PUBLIC_DEGREE_INFORMATION" />
			<bean:message key="label.of" />
			<bean:define id="initialDate" name="infoDegreeCurricularPlan" property="initialDate" />		
			<%= initialDate.toString().substring(initialDate.toString().lastIndexOf(" ")+1) %>
			<logic:notEmpty name="infoDegreeCurricularPlan" property="endDate">
				<bean:define id="endDate" name="infoDegreeCurricularPlan" property="endDate" />	
				-<%= endDate.toString().substring(endDate.toString().lastIndexOf(" ")) %>
			</logic:notEmpty>
		</span>
		</h2>
		<br />
	

	<logic:present name="infoRoom" >
	<div id="invisible"><h2><bean:message key="title.info.room"/></h2></div>
       	<div id="invisible"><table class="invisible" width="90%">
                <tr>
                    <th class="listClasses-header"><bean:message key="property.room.name" /></th>
                    <th class="listClasses-header"><bean:message key="property.room.type" /></th>
                    <th class="listClasses-header"><bean:message key="property.room.building" /></th>
                    <th class="listClasses-header"><bean:message key="property.room.floor" /></th>
					<th class="listClasses-header"><bean:message key="property.room.capacity.normal" /></th>
					<th class="listClasses-header"><bean:message key="property.room.capacity.exame" /></th>
                </tr>
                <tr>
                    <td class="listClasses"><bean:write name="infoRoom" property="nome" /></td>
                    <td class="listClasses"><bean:write name="infoRoom" property="tipo" /></td>
                    <td class="listClasses"><bean:write name="infoRoom" property="edificio" /></td>
					<td class="listClasses"><bean:write name="infoRoom" property="piso" /></td>
                    <td class="listClasses"><bean:write name="infoRoom" property="capacidadeNormal" /></td>
                    <td class="listClasses"><bean:write name="infoRoom" property="capacidadeExame" /></td>
                </tr>
            </table>
		</div>
		<br />
		<br />

	</logic:present>
	
	<logic:notPresent name="infoRoom" >
		<table align="center">
			<tr>
				<td>
					<span class="error"><bean:message key="message.public.notfound.room"/></span>
				</td>
			</tr>
		</table>
	</logic:notPresent>
</logic:present>





