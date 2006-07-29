<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%--
<%@ page import="org.apache.struts.Globals" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>
<%@ page import="net.sourceforge.fenixedu.util.Data" %>
--%>
<logic:present name="siteView">
<bean:define id="curricularCourses" name="siteView" property="component" type="net.sourceforge.fenixedu.dataTransferObject.InfoSiteAssociatedCurricularCourses"/>
    <span class="error"><!-- Error messages go here --><html:errors /></span>

    <table>        
    	<logic:iterate id="curricularCourse" name="curricularCourses" property="associatedCurricularCourses" indexId="index" > 
    		<logic:equal name="index" value="0">
    			<h2><bean:write name="curricularCourse" property="name" /><p></p></h2>
				<tr>
		    		<td>
						<html:link page="<%= "/studentsByCurricularCourse.do?method=prepare&amp;objectCode=" + pageContext.findAttribute("objectCode") %>">   				
							<bean:message key="label.allStudents" />
						</html:link>
			   		</td>
		   		</tr>    		
    		</logic:equal>	
    		<logic:present name="curricularCourse" property="infoScopes">    		
	    	<logic:iterate id="scope" name="curricularCourse" property="infoScopes">
	    		<tr>
	    			<td>
						<!--<bean:define id="scopeCode" name="scope" property="idInternal"/>-->
						<bean:define id="scopeCode" name="curricularCourse" property="idInternal"/>
						<bean:define id="ano" name="scope" property="infoCurricularSemester.infoCurricularYear.year" />
						<bean:define id="semestre" name="scope" property="infoCurricularSemester.semester" />
						
						<html:link page="<%= "/studentsByCurricularCourse.do?method=prepare&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;scopeCode=" + scopeCode %>">   				
							<bean:write name="curricularCourse" property="infoDegreeCurricularPlan.infoDegree.sigla"/>
							<logic:notEqual name="scope" property="infoBranch.name" value="">
								<bean:message key="property.curricularCourse.branch" />
								<bean:write name="scope" property="infoBranch.name"/>&nbsp;
							</logic:notEqual>						
							<bean:message key="label.year" arg0="<%= String.valueOf(ano) %>"/>
							<bean:message key="label.period" arg0="<%= String.valueOf(semestre) %>"/>
	    				</html:link>
	    			</td>
	    		</tr>
			</logic:iterate> 
			</logic:present>    		
    	</logic:iterate>
    </table>    
</logic:present>