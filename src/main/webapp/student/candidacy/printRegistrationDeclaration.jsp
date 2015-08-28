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
<%-- WARNING: This JSP is used to generate the first year candidate report (printAllDocuments.jsp). Beware when editing it! --%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:html xhtml="true">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="<%= request.getContextPath() %>/CSS/dotist.css" rel="stylesheet" media="screen" type="text/css" />
<link href="<%= request.getContextPath() %>/CSS/dotist_print.css" rel="stylesheet" media="print" type="text/css" />

</head>

<body class="registration" id="pagewrapper_registration">

<jsp:include page="./printRegistrationDeclarationTemplate.jsp" flush="true" />
<div class="break-before">
</div>

<jsp:include page="./printRegistrationDeclarationTemplate.jsp" flush="true" />
<div class="break-before">
</div>

<jsp:include page="./printRegistrationDeclarationTemplate.jsp" flush="true" />
<div class="break-before">
</div>

<jsp:include page="./printRegistrationDeclarationTemplate.jsp" flush="true" />
<div class="break-before">
</div>

<jsp:include page="./printRegistrationDeclarationTemplate.jsp" flush="true" />
<div class="break-before">
</div>

<jsp:include page="./printRegistrationDeclarationTemplate.jsp" flush="true" />
<div class="break-before">
</div>

<jsp:include page="./printRegistrationDeclarationTemplate.jsp" flush="true" />
<div class="break-before">
</div>

<jsp:include page="./printRegistrationDeclarationTemplate.jsp" flush="true" />


</body>

</html:html>
