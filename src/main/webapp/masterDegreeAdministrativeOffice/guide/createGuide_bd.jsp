<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>


<!-- masterDegreeAdministrativeOffice/guide/createGuide_bd.jsp -->

<h2><bean:message key="title.masterDegree.administraiveOffice.createGuide"/></h2>
<span class="error"><!-- Error messages go here -->

<html:messages id="messages" message="true">
	<bean:write name="messages" />
</html:messages>

<html:errors/>

</span>
<br />
<logic:present name="<%= PresentationConstants.EXECUTION_DEGREE %>" scope="request">
   <table>
    <html:form action="/createGuideDispatchAction?method=requesterChosen">
    <html:hidden alt="<%=PresentationConstants.EXECUTION_DEGREE_OID %>" property="<%=PresentationConstants.EXECUTION_DEGREE_OID %>" value="<%= pageContext.findAttribute(PresentationConstants.EXECUTION_DEGREE_OID).toString()%>"/>
      <!-- Degree -->
	  <tr>
   		<td align="center" class="infoselected" colspan="2">
			<bean:define id="executionDegree" name="<%= PresentationConstants.EXECUTION_DEGREE %>" scope="request" />
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
            <e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.masterDegree.GuideRequester" bundle="ENUMERATION_RESOURCES" excludedFields="CANDIDATE"/>
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
         	<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization" excludedFields="STUDENT_CURRICULAR_PLAN_INTEGRATED_MASTER_DEGREE" bundle="ENUMERATION_RESOURCES"/>
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
       <!-- Contributor -->
       <tr>
         <td colspan="2">
     	  <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
			<fr:edit id="chooseContributorBean" name="chooseContributorBean" schema="createReceiptBean.create.notRequired">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle4" />
				</fr:layout>
			</fr:edit>
          	</td>
           </tr> 
		</table>
		<p>
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.ok" value="Seguinte" styleClass="inputbutton" property="ok"/>
			<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" value="Limpar" styleClass="inputbutton"/>
		</p>
	  </html:form>

</logic:present>