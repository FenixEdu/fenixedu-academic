<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.action.Action" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="DataBeans.InfoCurricularCourseScope" %>

<logic:present name="jspTitle">
	<h2><bean:write name="jspTitle" /></h2>
	<br />
</logic:present>
<span class="error"><html:errors/></span>
<logic:present name="executionYear" >
	<bean:message key="label.masterDegree.administrativeOffice.executionYear"/>:<bean:write name="executionYear" />
	<br />
</logic:present>
<logic:present name="degree" >
	(MUDAR PARA NOME EM VEZ DE SIGLA) -> 
	<bean:message key="label.masterDegree.administrativeOffice.masterDegree"/>:<bean:write name="degree" />
	<br />
</logic:present>

<logic:present name="curricularCourses">
	<h2><bean:message key="title.masterDegree.administrativeOffice.chooseCurricularCourseToStudyPlan" /></h2>
	<bean:define id="path" type="java.lang.String" scope="request" property="path" name="<%= Action.MAPPING_KEY %>" />
	<table>

	<html:form action="/displayCourseListToStudyPlan?method=chooseCurricularCourses">
   	  	<html:hidden property="page" value="1"/> 
   	  	<html:hidden property="candidateID"/> 
		<!-- CurricularCourse -->
		<tr><td>Créditos Atribuídos <html:text property="attributedCredits" value="" size="2"/></td></tr>

		<logic:iterate id="curricularCourseElem" name="curricularCourses"  indexId="index">
		   	<bean:define id="curricularCourse" name="curricularCourseElem" property="name"/>
			<tr>
				<td>
				<bean:define id="offset" value="0"/>
					<bean:size id="ccsSize" name="curricularCourseElem" property="infoScopes" />
					<logic:iterate id="curricularCourseScope" name="curricularCourseElem"  indexId="scopeID" property="infoScopes" 
							  offset="0" length="1">
        						<logic:equal name="curricularCourseScope" property="infoBranch.name"  value='<%= new String("") %>'>
        							<bean:define id="offset" value="1"/>
            						<html:multibox property="selection">
                						<bean:write name="curricularCourseScope" property="idInternal"/>
            						</html:multibox>
        						</logic:equal>
        						<logic:notEqual name="curricularCourseScope" property="infoBranch.name"  value='<%= new String("") %>'>
        							<bean:define id="offset" value="0"/>
        						</logic:notEqual>
        						<strong><bean:write name="curricularCourseElem" property="name"/></strong><br />
					</logic:iterate>
					
				    <blockquote>
            			<logic:iterate id="curricularCourseScope" name="curricularCourseElem"  indexId="scopeID" property="infoScopes" 
            						    offset="<%= new String(offset) %>" length="<%= String.valueOf(ccsSize.intValue() - Integer.parseInt(offset)) %>">
                						<html:multibox property="selection">
                    						<bean:write name="curricularCourseScope" property="idInternal"/>
                						</html:multibox>
               							<bean:write name="curricularCourseScope" property="infoBranch.name"/> <br/>
						</logic:iterate>	
				    </blockquote>
				</td>
			</tr>
		</logic:iterate>
	</table>		
<br />
		
<html:submit value="Submeter" styleClass="inputbutton" property="ok"/>
</html:form>
</logic:present>
