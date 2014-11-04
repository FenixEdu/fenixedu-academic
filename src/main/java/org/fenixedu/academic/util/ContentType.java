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
package net.sourceforge.fenixedu.util;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public enum ContentType {

    JPG, PNG;

    public String getMimeType() {

        ContentType type = valueOf(name());

        switch (type) {
        case JPG:
            return "image/jpeg";
        case PNG:
            return "image/png";
        default:
            return "*/*";
        }

    }

    public String getFileExtention() {
        return name().toLowerCase();
    }

    public static ContentType getContentType(String httpContentType) {

        String contentTypeInLowerCase = httpContentType.toLowerCase();

        if (contentTypeInLowerCase.equals("image/jpeg") || contentTypeInLowerCase.equals("image/jpg")
                || contentTypeInLowerCase.equals("image/pjpeg")) {
            return JPG;
        }
        if (contentTypeInLowerCase.equals("image/png") || contentTypeInLowerCase.equals("image/x-png")) {
            return PNG;
        }

        return null;
    }

}
