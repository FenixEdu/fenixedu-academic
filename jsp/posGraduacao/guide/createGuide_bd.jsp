<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<h2><bean:message key="title.masterDegree.administraiveOffice.createGuide"/></h2>
<span class="error"><html:errors/></span>
<br />
<logic:present name="<%= SessionConstants.EXECUTION_DEGREE %>" scope="request">
   <table>
    <html:form action="/createGuideDispatchAction?method=requesterChosen">
    <html:hidden alt="<%=SessionConstants.EXECUTION_DEGREE_OID %>" property="<%=SessionConstants.EXECUTION_DEGREE_OID %>" value="<%= pageContext.findAttribute(SessionConstants.EXECUTION_DEGREE_OID).toString()%>"/>
      <!-- Degree -->
	  <tr>
   		<td align="center" class="infoselected" colspan="2">
			<bean:define id="executionDegree" name="<%= SessionConstants.EXECUTION_DEGREE %>" scope="request" />
   			<b><bean:message key="label.masterDegree.administrativeOffice.degree"/>: &nbsp;</b>
			<bean:write name="executionDegree" property="infoDegreeCurricularPlan.infoDegree.nome" /> -
			<bean:write name="executionDegree" property="infoDegreeCurricularPlan.name" /> -
			<bean:write name="executionDegree" property="infoExecutionYear.year" />
         </td>
      </tr>
      <tr>
      	<td>&nbsp;</td>
      </tr>
       <!-- Requester Number -->
       <tr>
         <td><bean:message key="label.masterDegree.administrativeOffice.requesterNumber"/>: </td>
         <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.number" property="number"/></td>
         </td>
       </tr>
       <!-- Requester Type -->
       <tr>
         <td><bean:message key="label.masterDegree.administrativeOffice.requesterType"/>: </td>
         <td>        
            <e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.masterDegree.GuideRequester" bundle="ENUMERATION_RESOURCES"/>
            <html:select bundle="HTMLALT_RESOURCES" altKey="select.requester" property="requester">
            	<html:option key="dropDown.Default" value=""/>
                <html:options collection="values" property="value" labelProperty="label"/>
             </html:select> 
         </td>
        </tr>
       <!-- Graduation Type -->
       	<tr>
       	 <td><bean:message key="label.masterDegree.administrativeOffice.graduationType"/>: </td>
         <td>
         	<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization" excludedFields="INTEGRATED_MASTER_DEGREE" bundle="ENUMERATION_RESOURCES"/>
            <html:select bundle="HTMLALT_RESOURCES" altKey="select.graduationType" property="graduationType">
                <html:option key="dropDown.Default" value=""/>
                <html:options collection="values" property="value" labelProperty="label"/>
             </html:select>          
         </td>
		</tr>
        
   	   <!-- Execution Degree ID-->
       <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionDegreeID" property="executionDegreeID"/>
       
       <tr>
       	<td>&nbsp;&nbsp;&nbsp;</td>
       </tr>       
       <tr>
       	<td colspan='2'><h2><bean:message key="label.masterDegree.administrativeOffice.dataContributor"/></h2></td>
       </tr>
       <logic:present name="<%= SessionConstants.UNEXISTING_CONTRIBUTOR %>">     	
	       <tr>
	       	<td colspan='2'><strong><bean:message key="error.masterDegree.administrativeOffice.nonExistingContributor"/></strong></td>
	       </tr>
		</logic:present >
       <!-- Contributor -->
       <tr>
         <td colspan="2">
     	  <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
			<fr:edit id="chooseContributorBean" name="chooseContributorBean" schema="createReceiptBean.create">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle4" />
				</fr:layout>
			</fr:edit>
          	</td>
           </tr> 
		</table>
		<p>
			<html:submit value="Seguinte" styleClass="inputbutton" property="ok"/>
			<html:reset value="Limpar" styleClass="inputbutton"/>
		</p>
	  </html:form>

</logic:present>