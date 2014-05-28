<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<tiles:importAttribute name="creditLineDTO" ignore="true"/>
<logic:present name="creditLineDTO">

		<td>
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" pattern="###.##">
				<bean:write name="creditLineDTO" property="teachingDegreeCredits"/>
			</fmt:formatNumber>
		</td>			
		<td>
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" pattern="###.##">
				<bean:write name="creditLineDTO" property="supportLessonHours"/>
			</fmt:formatNumber>h
		</td>
		<td>
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" pattern="###.##">
				<bean:write name="creditLineDTO" property="masterDegreeCredits"/>
			</fmt:formatNumber>
		</td>
		<td>
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" pattern="###.##">
				<bean:write name="creditLineDTO" property="tfcAdviseCredits"/>
			</fmt:formatNumber>
		</td>
		<td>
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" pattern="###.##">
				<bean:write name="creditLineDTO" property="thesesCredits"/>
			</fmt:formatNumber>
		</td>
		<td>
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" pattern="###.##">
				<bean:write name="creditLineDTO" property="institutionWorkingHours"/>
			</fmt:formatNumber>h				
		</td>
		<td>
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" pattern="###.##">
				<bean:write name="creditLineDTO" property="otherCredits"/>
			</fmt:formatNumber>
		</td>
		<td>
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" pattern="###.##">
				<bean:write name="creditLineDTO" property="managementCredits"/>
			</fmt:formatNumber>
		</td>
		<td>
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" pattern="###.##">
				<bean:write name="creditLineDTO" property="serviceExemptionCredits"/>
			</fmt:formatNumber>
		</td>
			
</logic:present>

