<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="ServidorApresentacao.TagLib.sop.v3.TimeTableType" %>
<%@ page import="DataBeans.InfoShiftWithAssociatedInfoClassesAndInfoLessons"%>
<%@ page import="DataBeans.InfoLesson"%>
<%@ page import="java.util.Calendar" %>

<logic:notPresent name="<%= SessionConstants.EXECUTION_COURSE_KEY %>" scope="session">
<table align="center"  cellpadding='0' cellspacing='0'>
			<tr align="center">
				<td>
					<font color='red'> <bean:message key="message.public.notfound.executionCourse"/> </font>
				</td>
			</tr>
		</table>
</logic:notPresent>

<logic:present name="<%= SessionConstants.EXECUTION_COURSE_KEY %>" scope="session">
<logic:present name="publico.infoCurricularCourses" scope="session">
			<table align="center" cellspacing="0" cellpadding="0" width="90%" >
						
				<logic:iterate id="curricularCourse" name="publico.infoCurricularCourses" scope="session">
					<tr class="timeTable_line" align="center">
						
						<td class="horariosHoras_first">
							<bean:write name="curricularCourse" property="infoDegreeCurricularPlan.infoDegree.sigla"/>-
							<bean:write name="curricularCourse" property="infoDegreeCurricularPlan.infoDegree.nome"/>
						</td>
						
						
					</tr>
				</logic:iterate>
			</table>	
			<br/>		
	</logic:present>		
 <logic:present name="<%= SessionConstants.INFO_SITE %>" property="initialStatement">
	<table cellspacing="0" width="90%">
        <tr>
          <td class="citation">
            <p>&quot;<bean:write name="<%= SessionConstants.INFO_SITE %>" property="initialStatement" />&quot;</p>
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
            	<bean:message key="message.lastWeekAnnouncements"/> 
             </td>
             <td  class="ultAnuncioAviso">
             </td>
             <td  class="ultAnuncioAviso">
             </td>
           
           </tr>
          
           <tr>
           	<td >
           		<img alt="" border="0"  src="<%= request.getContextPath() %>/images/icon_anuncio.gif"  />
           		<html:link  page="/accessAnnouncements.do">
            <bean:write name="<%= SessionConstants.LAST_ANNOUNCEMENT %>" property="title"/>
            	</html:link>	
           	</td>
           	<td align="center">	
           		<bean:write name="<%= SessionConstants.LAST_ANNOUNCEMENT %>" property="information"/>
           </td>
           </tr>
           <tr>
          
           	<td align="right"><bean:message key="message.createdOn"/>
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
     	<h2><bean:message key="message.siteAddress" /></h2>
	<bean:define id="alternativeSite" name="<%=SessionConstants.INFO_SITE%>" property="alternativeSite"/>
	<html:link href="<%=(String)pageContext.findAttribute("alternativeSite") %>" target="_blank">
			<bean:write name="alternativeSite" />
	</html:link>
			<br/>
			<br/>
</logic:present>
     <logic:present name="<%= SessionConstants.INFO_SITE %>" property="introduction">
	<h2><bean:message key="message.introduction" /></h2>
      <p><bean:write name="<%= SessionConstants.INFO_SITE %>" property="introduction" filter="false" /></p>
        	
      <br/>
      <br/>
	</logic:present>	

</logic:present>		
