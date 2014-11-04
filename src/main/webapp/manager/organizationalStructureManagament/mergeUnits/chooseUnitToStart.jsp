<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/units" prefix="un" %>
<html:xhtml/>

<h2><bean:message key="title.units.merge" bundle="MANAGER_RESOURCES"/></h2>

<logic:present role="role(MANAGER)">
	
	<logic:notEmpty name="externalInstitutionUnit">
		
		<script language="JavaScript">
			function check(e,v)
			{	
				var contextPath = '<%= request.getContextPath() %>';	
				if (e.style.display == "none")
				  {
				  e.style.display = "";
				  v.src = contextPath + '/images/toggle_minus10.gif';
				  }
				else
				  {
				  e.style.display = "none";
				  v.src = contextPath + '/images/toggle_plus10.gif';
				  }
			}
		</script>
	
		<p class="mtop3 mbottom2"><strong><bean:message key="label.choose.unit" bundle="MANAGER_RESOURCES"/></strong></p>	
		<un:tree initialUnit="externalInstitutionUnit" unitParamName="unitID" path="/manager/unitsMerge.do?method=seeChoosedUnit" state="true"/>
		
	</logic:notEmpty>
	
</logic:present>

