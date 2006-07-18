<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.domain.degree.DegreeType" %>

<p><span class="error"><html:errors/></span></p>

<logic:present name="degreesList">

	<logic:iterate id="infoDegree" name="degreesList"  length="1">		
			<bean:define id="degreeType" name="infoDegree" property="tipoCurso" />	
	</logic:iterate>
				
<bean:define id="institutionUrl" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/></bean:define>
<div class="breadcumbs"><a href="<%= institutionUrl %>"><bean:message key="institution.name.abbreviation" bundle="GLOBAL_RESOURCES"/></a>  > Ensino</div>
				<div class="version"><span class="px10"><a href="#">english version</a> <img src="<%= request.getContextPath() %>/images/icon_uk.gif" alt="<bean:message key="icon_uk" bundle="IMAGE_RESOURCES" />" width="16" height="12" /></span></div> 

				<h1><bean:message key="public.degree.information.label.education" bundle="PUBLIC_DEGREE_INFORMATION" /></h1>
				<p class="greytxt">
					<logic:equal name="degreeType" value="<%= DegreeType.MASTER_DEGREE.toString() %>">
						<bean:message key="text.masterDegree" />
					</logic:equal>
					<logic:equal name="degreeType" value="<%= DegreeType.DEGREE.toString() %>">
						<bean:message key="text.nonMasterDegree" />
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
								<bean:define id="degreeID" name="infoDegree" property="idInternal" />
								
									<li>
										<html:link page="<%= "/showDegreeSite.do?method=showDescription&amp;degreeID=" + pageContext.findAttribute("degreeID").toString()+ "&amp;index=" + pageContext.findAttribute("index").toString() %>" >
											<bean:write name="infoDegree" property="nome" />&nbsp;(<bean:write name="infoDegree" property="sigla"/>)
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


