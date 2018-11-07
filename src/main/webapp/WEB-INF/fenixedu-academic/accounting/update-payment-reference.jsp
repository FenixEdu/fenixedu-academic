<%--

    Copyright © 2018 Instituto Superior Técnico

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

<spring:url value="/accounting-management/paymentReference" var="paymentReferenceUrl" />

<script type="text/javascript">

    function updatePaymentReference(date) {
        const url = '${paymentReferenceUrl}';
        const paymentMethod = $("option:selected", $("#paymentMethod")).val();

        $.ajax({
            url: url,
            type: "GET",
            data: {
                date: date,
                paymentMethod: paymentMethod
            },
            headers: {
                "X-CSRF-TOKEN": $("input[name='_csrf']").val()
            },
            success: function(data) {
                $("#paymentReference").val(data);
            },
            error: function(data) {
                alert(data.statusText);
            }
        });
    }

</script>