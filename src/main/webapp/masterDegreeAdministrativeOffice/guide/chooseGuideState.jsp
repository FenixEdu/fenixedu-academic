<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e" %>

<h2><bean:message key="label.masterDegree.administrativeOffice.listGuideByState"/></h2>
<br />    
<span class="error"><!-- Error messages go here --><html:errors /></span>   
<br />
   <table>
    <html:form action="/guideListingByState?method=chooseState">
   	  <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
       <!-- Guide Year -->
       <tr>
         <td><bean:message key="label.masterDegree.administrativeOffice.guideYear"/>: </td>
         <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.year" property="year"/></td>
         </td>
       </tr>
       <!-- Guide State -->
       <tr>
         <td><bean:message key="label.masterDegree.administrativeOffice.guideSituation"/>: </td>
         <td>
         	<e:labelValues id="situations" enumeration="net.sourceforge.fenixedu.domain.GuideState" bundle="ENUMERATION_RESOURCES"/>
         	<html:select bundle="HTMLALT_RESOURCES" altKey="select.state" property="state">
         		<html:option key="dropDown.Default" value=""/>
                <html:options collection="situations" property="value" labelProperty="label"/>
             </html:select>
         </td>
       </tr>
   </table>
<br />
<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.ok" value="Listar" styleClass="inputbutton" property="ok"/>
</html:form>


