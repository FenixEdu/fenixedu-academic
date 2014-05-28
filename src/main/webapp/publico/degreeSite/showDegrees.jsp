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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ page import="net.sourceforge.fenixedu.domain.degree.DegreeType" %>

<p><span class="error"><!-- Error messages go here --><html:errors /></span></p>

<logic:present name="degreesList">

	<logic:iterate id="infoDegree" name="degreesList"  length="1">		
			<bean:define id="degreeType" name="infoDegree" property="tipoCurso" />	
	</logic:iterate>
				
<bean:define id="institutionUrl" type="java.lang.String"><%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %></bean:define>
<div class="breadcumbs mvert0"><a href="<%= institutionUrl %>"><%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%></a>  > Ensino</div>
				<div class="version"><span class="px10"><a href="#">english version</a> <img src="<%= request.getContextPath() %>/images/icon_uk.gif" alt="<bean:message key="icon_uk" bundle="IMAGE_RESOURCES" />" width="16" height="12" /></span></div> 

				<h1><bean:message key="public.degree.information.label.education" bundle="PUBLIC_DEGREE_INFORMATION" /></h1>
				<p class="greytxt">
					<logic:equal name="degreeType" value="<%= DegreeType.MASTER_DEGREE.toString() %>">
						<bean:message key="text.masterDegree" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>" />
					</logic:equal>
					<logic:equal name="degreeType" value="<%= DegreeType.DEGREE.toString() %>">
						<bean:message key="text.nonMasterDegree" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>" />
					</logic:equal>				
				</p>
			  
				<table width="100%"  border="0" cellspacing="0" summary="Esta tabela contém links para as licenciaturas, mestrados, doutoramentos e pós-graduações existentes no IST">
				  <tr>
				    <td colspan="2" class="bottom_border">
						<h2>
							<logic:equal name="degreeType" value="DEGREE" >
								<bean:message key="title.degrees" />
							</logic:equal>
							<logic:equal name="degreeType" value="MASTER_DEGREE" >
								<bean:message key="title.masterDegrees" />
							</logic:equal>
						</h2>
					</td>
				  </tr>
				  <tr>
				    <td width="50%" valign="top">
							<ul> <%-- class="treemenu" --%>
	
								<logic:iterate id="infoDegree" name="degreesList" indexId="index">		
								<bean:define id="degreeID" name="infoDegree" property="externalId" />
								
									<li>
										<html:link page="<%= "/showDegreeSite.do?method=showDescription&amp;degreeID=" + pageContext.findAttribute("degreeID").toString()+ "&amp;index=" + pageContext.findAttribute("index").toString() %>" >
											<bean:write name="infoDegree" property="degree.presentationName" />&nbsp;(<bean:write name="infoDegree" property="sigla"/>)
										</html:link>
									</li>
								</logic:iterate>
							</ul>			    
				    </td>
				  </tr>
				</table>

</logic:present>

<logic:notPresent name="degreesList">
	<p><h1><bean:message key="error.impossibleDegreeList" /></h1></p>
</logic:notPresent>


