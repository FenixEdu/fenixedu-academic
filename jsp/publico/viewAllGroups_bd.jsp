
<%@ page language="java" %>

<%@ page import="java.lang.String" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>



<%--
<%@ page import="DataBeans.InfoShiftWithAssociatedInfoClassesAndInfoLessons"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="ServidorApresentacao.TagLib.sop.v3.TimeTableType" %>
<%@ page import="DataBeans.InfoShiftWithAssociatedInfoClassesAndInfoLessons"%>
<%@ page import="DataBeans.InfoLesson"%>
<%@ page import="java.util.Calendar" %>


<logic:present name="siteView" property="component">
	<bean:define id="component" name="siteView" property="component" />
	<logic:empty name="component" property="infoSiteGroupsByShiftList">
	<h2><bean:message key="message.infoSiteGroupsByShiftList.not.available" /></h2>
	</logic:empty>
<table border="0" style="text-align: left;">
<tr>
<td>	
  <tbody>
    <logic:iterate id="infoSiteGroupsByShift" name="component" property="infoSiteGroupsByShiftList" >
    
  <table align="center" width="95%" cellspacing='1' cellpadding='1'>
					
	<bean:define id="infoShift" name="infoSiteGroupsByShift" property="infoShift"/>
			
	<logic:iterate id="infoShift" name="infoShift" indexId="infoShiftIndex">	
     
        	    
	<logic:iterate id="infoLesson" name="infoShift" property="infoLessons" length="1" indexId="infoLessonIndex">
     <% Integer iH = new Integer(((InfoLesson) infoLesson).getInicio().get(Calendar.HOUR_OF_DAY)); %>
     <% Integer iM = new Integer(((InfoLesson) infoLesson).getInicio().get(Calendar.MINUTE)); %>
     <% Integer fH = new Integer(((InfoLesson) infoLesson).getFim().get(Calendar.HOUR_OF_DAY)); %>
     <% Integer fM = new Integer(((InfoLesson) infoLesson).getFim().get(Calendar.MINUTE)); %>
	<tr >
		<td  class="listClasses" rowspan="<%=((InfoShiftWithAssociatedInfoClassesAndInfoLessons) infoShift).getInfoLessons().size() %>">
				<bean:write name="infoShift" property="infoShift.nome"/>
		</td>
		<td class="listClasses">
				<bean:write name="infoLesson" property="diaSemana"/> &nbsp;
		</td>
		<td class="listClasses">
				<%= iH.toString()%> : <%= iM.toString()%><% if (iM.intValue() == 0) { %>0<% } %>
		</td>
		<td class="listClasses">
				<%= fH.toString()%> : <%= fM.toString()%><% if (fM.intValue() == 0) { %>0<% } %>								
		</td>
							
	</tr>
	</logic:iterate>
					

	<logic:iterate id="infoLesson" name="infoShift" property="infoLessons" offset="1">
    <% Integer iH = new Integer(((InfoLesson) infoLesson).getInicio().get(Calendar.HOUR_OF_DAY)); %>
    <% Integer iM = new Integer(((InfoLesson) infoLesson).getInicio().get(Calendar.MINUTE)); %>
    <% Integer fH = new Integer(((InfoLesson) infoLesson).getFim().get(Calendar.HOUR_OF_DAY)); %>
    <% Integer fM = new Integer(((InfoLesson) infoLesson).getFim().get(Calendar.MINUTE)); %>
	<tr >
		<td class="listClasses">
			<bean:write name="infoLesson" property="diaSemana"/> &nbsp;
		</td>
		<td class="listClasses">
			<%= iH.toString()%> : <%= iM.toString()%><% if (iM.intValue() == 0) { %>0<% } %>
		</td>
		<td class="listClasses">
			<%= fH.toString()%> : <%= fM.toString()%><% if (fM.intValue() == 0) { %>0<% } %>
		</td>
				
	</tr>
	</logic:iterate>

	</logic:iterate>
	</tbody> 
</table>			
	       
        
 	 </logic:iterate>
         </td>
         </tr>
        
        </tbody>
</table>
</logic:present>

<logic:notPresent name="siteView" property="component">
<h4>
<bean:message key="message.infoSiteGroupsByShiftList.not.available" />
</h4>
</logic:notPresent>--%>



<logic:present name="siteView" property="component">
	<bean:define id="component" name="siteView" property="component" />
	<logic:empty name="component" property="infoSiteGroupsByShiftList">
	<h2><bean:message key="message.infoSiteGroupsByShiftList.not.available" /></h2>
	</logic:empty>
<table border="0" style="text-align: left;">
	<tbody>
     <logic:iterate id="infoSiteGroupsByShift" name="component" property="infoSiteGroupsByShiftList" >
     <tr>
      <td>
        <br>
          <h2>
           <logic:empty name="infoSiteGroupsByShift" property="infoShift">
			<h2><bean:message key="message.no.infoShift" /></h2>
			</logic:empty>
			
			<logic:notEmpty name="infoSiteGroupsByShift" property="infoShift">
			<bean:define id="infoShift" name="infoSiteGroupsByShift" property="infoShift"/>	
			<bean:write name="infoShift" property="nome"/>
             </logic:notEmpty>
             
			
               <logic:iterate id="infoStudentGroup" name="infoSiteGroupsByShift" property="infoStudentGroupsList" >
        		<tr>
          		<td>
             	<br>
               	<li><html:link page="<%= "/viewSite.do" + "?method=viewStudentGroupInformationAction&amp;objectCode=" + pageContext.findAttribute("objectCode")  + "&amp;executionPeriodOID=" + pageContext.findAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;groupProperties=" + pageContext.findAttribute("groupProperties") %>" paramId="studentGroupCode" paramName="infoStudentGroup" paramProperty="idInternal">
						<bean:message key="label.groupWord"/>
                		<bean:write name="infoStudentGroup" property="groupNumber"/>	</h2>
					</html:link></li>
               
                
             	</td>
                </tr>

            </logic:iterate>
        </tbody>
                
                
                
                    </td>
                </tr>

            </logic:iterate>
        </tbody>
</table>
</logic:present>

<logic:notPresent name="siteView" property="component">
<h4>
<bean:message key="message.infoSiteGroupsByShiftList.not.available" />
</h4>
</logic:notPresent>