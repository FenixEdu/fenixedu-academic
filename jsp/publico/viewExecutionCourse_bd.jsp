<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="java.util.Calendar" %>
<logic:notPresent name="<%= SessionConstants.EXECUTION_COURSE_KEY %>" scope="session">
<table align="center"  cellpadding='0' cellspacing='0'>
			<tr align="center">
				<td>
					<span class="error"> <bean:message key="message.public.notfound.executionCourse"/> </span>
				</td>
			</tr>
		</table>
</logic:notPresent>
<logic:present name="<%= SessionConstants.EXECUTION_COURSE_KEY %>" scope="session">
 	<logic:present name="<%= SessionConstants.INFO_SITE %>" property="initialStatement">
	<table align="center" cellspacing="0" width="90%">
        <tr>
          <td class="citation">
            <p><bean:write name="<%= SessionConstants.INFO_SITE %>" property="initialStatement" /></p>
          </td>
        </tr>
      </table>		
      <br/>
      <br/>
	</logic:present>	
 <logic:present name="<%= SessionConstants.LAST_ANNOUNCEMENT %>" scope="session">		
        <table id="anuncios" align="center" cellspacing="0" width="90%">
          	<tr>
            	<td  class="ultAnuncioAviso"> 
            		<img alt="" border="0"  src="<%= request.getContextPath() %>/images/icon_warning.gif"  />
            		<bean:message key="message.lastAnnouncement"/> 
             	</td>      
           </tr>
           <tr>
           		<td class="ultAnuncio">
           			<img alt="" border="0"  src="<%= request.getContextPath() %>/images/icon_anuncio.gif"  />
           			<html:link  page="/accessAnnouncements.do">
            		<bean:write name="<%= SessionConstants.LAST_ANNOUNCEMENT %>" property="information"/>
            		</html:link>	
           		</td>
           </tr>
           <tr>
           		<td class="ultAnuncio-date"><bean:message key="message.createdOn"/>
           			<bean:write name="<%= SessionConstants.LAST_ANNOUNCEMENT %>" property="creationDate"/>
           			<br/>		
           			<bean:message key="message.modifiedOn"/>
           			<bean:write name="<%= SessionConstants.LAST_ANNOUNCEMENT %>" property="lastModifiedDate"/></td>
           </tr>           
        </table>
<br/>
<br/>
<br/>
<br/>
<br/>
  </logic:present>
     <logic:present name="<%=SessionConstants.INFO_SITE%>" property="alternativeSite">	
     <logic:notEmpty 	name="<%=SessionConstants.INFO_SITE%>" property="alternativeSite">	
     	<h2><bean:message key="message.siteAddress" /></h2>
	<bean:define id="alternativeSite" name="<%=SessionConstants.INFO_SITE%>" property="alternativeSite"/>
	<html:link href="<%=(String)pageContext.findAttribute("alternativeSite") %>" target="_blank">
			<bean:write name="alternativeSite" />
	</html:link>
			<br/>
			<br/>
</logic:notEmpty>			
</logic:present>
     <logic:present name="<%= SessionConstants.INFO_SITE %>" property="introduction">
     <logic:notEmpty name="<%= SessionConstants.INFO_SITE %>" property="introduction">
     	
	<h2><bean:message key="message.introduction" /></h2>
      <p><bean:write name="<%= SessionConstants.INFO_SITE %>" property="introduction" filter="false" /></p>
      <br/>
      <br/>
      </logic:notEmpty>
	</logic:present>	
</logic:present>		

<logic:notPresent name="<%= SessionConstants.RESPONSIBLE_TEACHERS_LIST %>">
	<span class="error">
         <bean:message key="message.teachers.not.available" />
	</span>
	<br/>
	<br/>
	<html:link href="mailto:suporte@dot.ist.utl.pt">suporte@dot.ist.utl.pt</html:link>
</logic:notPresent>
<logic:present name="<%= SessionConstants.RESPONSIBLE_TEACHERS_LIST %>" >
<logic:notEmpty name="<%= SessionConstants.RESPONSIBLE_TEACHERS_LIST %>" >	
<table>
	<tr>
		<th>
			<h2><bean:message key="label.responsableProfessor"/></h2>	
		</th>
	</tr>	

	<logic:iterate id="infoResponsableTeacher" name="<%= SessionConstants.RESPONSIBLE_TEACHERS_LIST %>">
		<tr>
			<td>
				<bean:write name="infoResponsableTeacher" property="infoPerson.nome" /> 
			</td>
		</tr>
	</logic:iterate>	
</table>
</BR>
</logic:notEmpty>
</logic:present>
<logic:present name="<%= SessionConstants.TEACHERS_LIST %>" >
<logic:notEmpty name="<%= SessionConstants.TEACHERS_LIST %>" >	
<table>
	<tr>
		<th>
			<h2><bean:message key="label.professorShips"/></h2>	
		</th>
	</tr>	

	<logic:iterate id="infoTeacher" name="<%= SessionConstants.TEACHERS_LIST %>">
		<tr>
			<td>
				<bean:write name="infoTeacher" property="infoPerson.nome" /> 
			</td>
		</tr>
	</logic:iterate>	
</table>
</logic:notEmpty>
</logic:present>