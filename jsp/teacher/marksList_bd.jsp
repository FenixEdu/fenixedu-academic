<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.action.Action" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>
<%@ page import="Util.Data" %>

<html:form name="/writeMarks">
<logic:present name="siteView">
<bean:define id="marksListComponent" name="siteView" property="component" type="DataBeans.InfoSiteMarks"/>
<bean:define id="commonComponent" name="siteView" property="commonComponent" type="DataBeans.InfoSiteCommon"/>

    <span class="error"><html:errors/></span>

    <table>        
		<tr>
			
			<td>
			 <h2><bean:write name="commonComponent" property="executionCourse.nome" /></h2>
		    </td>
			
			<logic:present name="marksListComponent" property="infoExam">  		
			<td>
				<h2>
				&nbsp;-&nbsp;				
				<bean:write name="marksListComponent" property="infoExam.season"/>&nbsp;
				<bean:write name="marksListComponent" property="infoExam.day"/>&nbsp;
				<bean:write name="marksListComponent" property="infoExam.beginning"/>&nbsp;
				</h2>
		   </td>
		   </logic:present>
		   
		</tr> 
		<tr>
			<td class="listClasses-header">
				<bean:message key="label.number" /> 
		   </td>
			<td class="listClasses-header">
				<bean:message key="label.name" />
		   </td>
			<logic:present name="marksListComponent" property="marksList">  						
				<td class="listClasses-header">
					<bean:message key="label.mark" />
				</td>
			</logic:present>
		</tr>    		
		
		<logic:present name="marksListComponent" property="marksList">  								
	    	<logic:iterate id="mark" name="marksListComponent" property="marksList"> 
				<tr>
					<td class="listClasses">
						<bean:write name="mark" property="infoFrequenta.aluno.number"/>&nbsp;
					</td>
					<td class="listClasses">
						<bean:write name="mark" property="infoFrequenta.aluno.infoPerson.nome"/>
					</td>											
					<td class="listClasses">
						<html:text name="mark" property="mark"/>
					</td>
				</tr>
	    	</logic:iterate>
		</logic:present>
		
    </table>    
</logic:present>
</html:form>