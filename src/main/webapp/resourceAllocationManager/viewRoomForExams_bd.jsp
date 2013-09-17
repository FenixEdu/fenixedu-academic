<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.TimeTableType" %>
<h2><bean:message key="title.view.room"/></h2>
<br />
<logic:present name="publico.infoRoom" scope="request">
	<table width="100%">
    	<tr>
      		<th class="listClasses-header"><bean:message key="property.room.name"/></th>
			<th class="listClasses-header"><bean:message key="property.room.type"/></th>
            <th class="listClasses-header"><bean:message key="property.room.building"/></th>
            <th class="listClasses-header"><bean:message key="property.room.floor"/></th>
			<th class="listClasses-header"><bean:message key="property.room.capacity.normal"/></th>
			<th class="listClasses-header"><bean:message key="property.room.capacity.exame"/></th>
      	</tr>
        <tr>
			<td class="listClasses"><bean:write name="publico.infoRoom" property="nome"/></td>
            <td class="listClasses"><bean:write name="publico.infoRoom" property="tipo"/></td>
			<td class="listClasses"><bean:write name="publico.infoRoom" property="edificio"/></td>
			<td class="listClasses"><bean:write name="publico.infoRoom" property="piso"/></td>
            <td class="listClasses"><bean:write name="publico.infoRoom" property="capacidadeNormal"/></td>
            <td class="listClasses"><bean:write name="publico.infoRoom" property="capacidadeExame"/></td>
     	</tr>
 	</table>
<br />
<br />	
<div align="center"><app:generateExamsMap name="<%= PresentationConstants.INFO_EXAMS_MAP %>" user="sop"/></div>
</logic:present>
	<logic:notPresent name="publico.infoRoom" scope="request">
		<table align="center">
			<tr>
				<td>
					<span class="error"><!-- Error messages go here --><bean:message key="message.public.notfound.room"/></span>
				</td>
			</tr>
		</table>
</logic:notPresent>