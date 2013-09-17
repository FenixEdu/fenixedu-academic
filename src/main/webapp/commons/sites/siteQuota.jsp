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