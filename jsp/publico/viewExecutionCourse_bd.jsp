<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="java.util.Calendar" %>
<logic:notPresent name="exeCode" >
<table align="center"  cellpadding='0' cellspacing='0'>
			<tr align="center">
				<td>
					<span class="error"> <bean:message key="message.public.notfound.executionCourse"/> </span>
				</td>
			</tr>
		</table>
</logic:notPresent>
<logic:present name="exeCode" >
 	<logic:present name="initStat" >
	<table align="center" cellspacing="0" width="90%">
        <tr>
          <td class="citation">
            <p><bean:write name="initStat" /></p>
          </td>
        </tr>
      </table>		
      <br/>
      <br/>
	</logic:present>	
 <logic:present name="lastAnnouncement" >		
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
           			<html:link  page="<%="/accessAnnouncements.do"+"?exeCode=" + pageContext.findAttribute("exeCode") + "&amp;ePName=" + pageContext.findAttribute("ePName") + "&amp;eYName=" + pageContext.findAttribute("eYName") %>">
            		<bean:write name="lastAnnouncement" property="title"/>:
            		</html:link>	
            		<br>
            		<center><bean:write name="lastAnnouncement" property="information" filter="false"/></center>
           		</td>
           </tr>
           <tr>		
           		<td class="ultAnuncio-date"><bean:message key="message.createdOn"/>
           			<bean:write name="lastAnnouncement" property="creationDateFormatted"/>
           			<br/>		
           			<bean:message key="message.modifiedOn"/>
           			<bean:write name="lastAnnouncement" property="lastModifiedDateFormatted"/></td>
           </tr>           
        </table>
<br/>
<br/>
<br/>
<br/>
<br/>
  </logic:present>
     <logic:present name="altSite" >	
     <logic:notEmpty 	name="altSite" >	
     	<h2><bean:message key="message.siteAddress" /></h2>
	
	<html:link href="<%=(String)pageContext.findAttribute("altSite") %>" target="_blank">
			<bean:write name="altSite" />
	</html:link>
			<br/>
			<br/>
</logic:notEmpty>			
</logic:present>
     <logic:present name="intro">
     <logic:notEmpty name="intro">
     	
	<h2><bean:message key="message.introduction" /></h2>
      <p><bean:write name="intro" filter="false" /></p>
      <br/>
      <br/>
      </logic:notEmpty>
	</logic:present>	
</logic:present>		

<logic:notPresent name="resTeacherList">
	<span class="error">
         <bean:message key="message.teachers.not.available" />
	</span>
	<br/>
	<br/>
	<html:link href="mailto:suporte@dot.ist.utl.pt">suporte@dot.ist.utl.pt</html:link>
</logic:notPresent>
<logic:present name="resTeacherList" >
<logic:notEmpty name="resTeacherList" >	
<table>
	<tr>
		<th>
			<h2><bean:message key="label.responsableProfessor"/></h2>	
		</th>
	</tr>	

	<logic:iterate id="infoResponsableTeacher" name="resTeacherList">
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
<logic:present name="lecTeacherList" >
<logic:notEmpty name="lecTeacherList" >	
<table>
	<tr>
		<th>
			<h2><bean:message key="label.professorShips"/></h2>	
		</th>
	</tr>	

	<logic:iterate id="infoTeacher" name="lecTeacherList">
		<tr>
			<td>
				<bean:write name="infoTeacher" property="infoPerson.nome" /> 
			</td>
		</tr>
	</logic:iterate>	
</table>
</logic:notEmpty>
</logic:present>