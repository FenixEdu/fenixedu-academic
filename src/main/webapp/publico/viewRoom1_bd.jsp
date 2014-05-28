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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.TimeTableType" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ page import="net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState" %>
<%@ page import="net.sourceforge.fenixedu.domain.degree.DegreeType" %>


<bean:define id="component" name="siteView" property="component"/>
<bean:define id="infoRoom" name="component" property="infoRoom" />
<bean:define id="lessonList" name="component" property="infoLessons" />

<br/> 
<logic:present name="infoDegreeCurricularPlan">
    <br/>
			<bean:define id="degreeType" name="infoDegreeCurricularPlan" property="infoDegree.degreeType" />	
			<bean:define id="degreeCurricularPlanID" name="infoDegreeCurricularPlan" property="externalId" />
			<bean:define id="institutionUrl" type="java.lang.String"><%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %></bean:define>
				<div class="breadcumbs mvert0"><a href="<%= institutionUrl %>"><%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%></a>
				<bean:define id="degreeType" name="infoDegreeCurricularPlan" property="infoDegree.degreeType" />	
				<logic:equal name="degreeType" value="<%= DegreeType.MASTER_DEGREE.toString() %>">
					 <html:link page="<%= "/showDegrees.do?method=master&executionPeriodOID=" + request.getAttribute(PresentationConstants.EXECUTION_PERIOD_OID) %>" >Ensino Mestrados</html:link>
				</logic:equal>
				<logic:equal name="degreeType" value="<%= DegreeType.DEGREE.toString() %>">
					<html:link page="<%= "/showDegrees.do?method=nonMaster&executionPeriodOID=" + request.getAttribute(PresentationConstants.EXECUTION_PERIOD_OID) %>" >Ensino Licenciaturas</html:link>		
				</logic:equal>
				&gt;&nbsp;
				<html:link page="<%= "/showDegreeSite.do?method=showDescription&amp;degreeID=" + request.getAttribute("degreeID").toString()%>">
					<bean:write name="infoDegreeCurricularPlan" property="infoDegree.sigla" />
				</html:link>&gt;&nbsp;
				<html:link page="<%= "/showDegreeSite.do?method=showCurricularPlan&amp;degreeID=" + request.getAttribute("degreeID") + "&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID").toString() + "&amp;executionPeriodOID=" + request.getAttribute(PresentationConstants.EXECUTION_PERIOD_OID) + "&amp;executionDegreeID="  + "&amp;index=" + request.getAttribute("index")  %>" >
					<bean:write name="infoDegreeCurricularPlan" property="name" />
				</html:link>&gt;&nbsp; 
				<html:link page="<%= "/chooseContextDANew.do?method=nextPagePublic&nextPage=classSearch&inputPage=chooseContext&executionPeriodOID=" + request.getAttribute(PresentationConstants.EXECUTION_PERIOD_OID)+ "&amp;degreeID=" + request.getAttribute("degreeID")+ "&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID").toString() %>" >
					<bean:message  key="public.degree.information.label.classes"  bundle="PUBLIC_DEGREE_INFORMATION" />
				</html:link>&gt;&nbsp; 
				<%= request.getAttribute("sigla").toString() %>
		</div>	
	
		<div class="clear"></div> 
		<h1><bean:write name="infoDegreeCurricularPlan" property="infoDegree.degreeType" />&nbsp;<bean:write name="infoDegreeCurricularPlan" property="infoDegree.nome" /></h1>
		
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
					<span class="error"><!-- Error messages go here --><bean:message key="message.public.notfound.room"/></span>
				</td>
			</tr>
		</table>
	</logic:notPresent>
</logic:present>





