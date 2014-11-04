/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.webServices.jersey.beans.publico;

import java.util.List;

public class FenixExternalApplications {

    private String name;
    private String description;
    private String siteUrl;
    private FenixAuthor author;
    private byte[] logo;
    private List<FenixScopes> scopes;
    private int authorizations;

    public FenixExternalApplications(String name, String description, String siteUrl, FenixAuthor author, byte[] logo,
            List<FenixScopes> scopes, int authorizations) {
        this.name = name;
        this.description = description;
        this.siteUrl = siteUrl;
        this.author = author;
        this.logo = logo;
        this.scopes = scopes;
        this.authorizations = authorizations;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSiteUrl() {
        return siteUrl;
    }

    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    public FenixAuthor getAuthor() {
        return author;
    }

    public void setAuthor(FenixAuthor author) {
        this.author = author;
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public List<FenixScopes> getScopes() {
        return scopes;
    }

    public void setScopes(List<FenixScopes> scopes) {
        this.scopes = scopes;
    }

    public int getAuthorizations() {
        return authorizations;
    }

    public void setAuthorizations(int authorizations) {
        this.authorizations = authorizations;
    }

    public static class FenixAuthor {

        private String name;
        private String id;

        public FenixAuthor(String name, String id) {
            this.name = name;
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    public static class FenixScopes {

        private String name;

        public FenixScopes(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
