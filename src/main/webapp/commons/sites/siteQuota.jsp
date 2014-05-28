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
<%@ page language="java" %>

<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<logic:present name="siteQuota">
    <bean:define id="siteQuota" name="siteQuota" type="java.lang.Long"/>
    <bean:define id="siteUsedQuota" name="siteUsedQuota" type="java.lang.Long"/>
    <%  
        float percent = ((float) siteUsedQuota) / siteQuota; 
        request.setAttribute("siteQuotaAvailable", siteQuota - siteUsedQuota);
    %>
    
    <div style="float: right; text-align: right;">
        <bean:define id="quotaMessage">
            <bean:message key="site.quota.available" bundle="SITE_RESOURCES"/>: 
            <fr:view name="siteQuotaAvailable" layout="fileSize"/> (<%= String.format("%.1f%%", (1 - percent) * 100) %>)
        </bean:define>
        <span title="<%= quotaMessage %>">
             <fr:view name="siteUsedQuota" layout="fileSize"/> / <fr:view name="siteQuota" layout="fileSize"/>
        </span>
        <div style="margin-top: -0.5em;">
            <img class="percentImage" 
                 src="../images/percentImage.png" title="<%= quotaMessage %>" alt="<%= quotaMessage %>" 
                 style="<%= "background-position: " + String.format("%.0f", 122 * percent - 121) + "px 0pt;" %>"/>
        </div>
    </div>
    
</logic:present>