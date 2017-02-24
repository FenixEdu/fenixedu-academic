<%--

    Copyright © 2017 Instituto Superior Técnico

    This file is part of FenixEdu Spaces.

    FenixEdu Spaces is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Spaces is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Spaces.  If not, see <http://www.gnu.org/licenses/>.

--%>
<!DOCTYPE html> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="page-header">
    <h2>
        <spring:message code="title.manage.countries"/>
    </h2>
</div>

<div class="form-group pull-right">
    <input type="text" class="search form-control" placeholder="<spring:message code='label.search'/>">
</div>

<table class="table results">
    <thead>
        <th><spring:message code="label.country.code" /></th>
        <th><spring:message code="label.country.three.letter.code" /></th>
        <th><spring:message code="label.country.localized.name"/></th>
        <th><spring:message code="label.country.nationality"/></th>
        <th></th> 
        <tr class="warning no-result hidden">
            <td colspan="6">
                <i class="fa fa-warning"></i> <spring:message code="label.noResultsFound" />
            </td>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="country" items="${countries}">
            <tr>
                <td>
                    <c:out value="${country.code}"/>
                </td>
                <td>
                    <c:out value="${country.threeLetterCode}"/>
                </td>
                <td>
                    <c:out value="${country.localizedName.content}"/>
                </td>
                <td>
                    <c:out value="${country.countryNationality.content}"/>
                </td>
                <td>
                    <spring:url var="editCountryUrl" value="/country-management/${country.externalId}/edit" />
                    <a href="${editCountryUrl}"><spring:message code="label.edit" /></a>
                </td>
            </tr>
        </c:forEach>
    </tbody>    
</table>

<script type="text/javascript">
    $(document).ready(function() {
      $(".search").keyup(function () {
        var searchTerm = $(".search").val();
        if (searchTerm == "") {
            $('.results tbody').children('tr').removeClass('hidden');
            $('.no-result').addClass('hidden');
            return;
        }
        var listItem = $('.results tbody').children('tr');
        var searchSplit = searchTerm.replace(/ /g, "'):containsi('");
        
        $.extend($.expr[':'], {'containsi': function(elem, i, match, array){
                return (elem.textContent || elem.innerText || '').toLowerCase().indexOf((match[3] || "").toLowerCase()) >= 0;
            }
        });
        
      $(".results tbody tr").not(":containsi('" + searchSplit + "')").each(function(e){
        $(this).addClass('hidden');
      });

      $(".results tbody tr:containsi('" + searchSplit + "')").each(function(e){
        $(this).removeClass('hidden');
      });

      var jobCount = $('table.results tbody tr:not(.hidden)').length;

      if(jobCount == '0') {$('.no-result').removeClass('hidden');}
        else {$('.no-result').addClass('hidden');}
              });
});
</script>
