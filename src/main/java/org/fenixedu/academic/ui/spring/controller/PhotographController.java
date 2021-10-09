/**
 * Copyright © 2002 Instituto Superior Técnico
 * <p>
 * This file is part of FenixEdu Academic.
 * <p>
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.spring.controller;

import org.fenixedu.academic.domain.photograph.PictureMode;
import org.fenixedu.bennu.core.domain.Avatar;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.exceptions.BennuCoreDomainException;
import org.joda.time.DateTime;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.io.InputStream;

@Controller
@RequestMapping("/user/photo")
public class PhotographController {

    @RequestMapping(value = "{username:.+}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> get(final @PathVariable String username,
                                      @RequestParam(value = "s", required = false, defaultValue = "100") Integer size,
                                      final @RequestHeader(value = "If-None-Match", required = false) String ifNoneMatch) throws IOException {
        if (size <= 0) {
            size = 100;
        }
        if (size > 512) {
            size = 512;
        }

        final User user = User.findByUsername(username);
        if (user != null && user.getPerson() != null) {
            final Avatar.PhotoProvider photoProvider = user.getPerson().isPhotoAvailableToCurrentUser()
                    ? Avatar.photoProvider.apply(user) : null;

            final HttpHeaders headers = new HttpHeaders();
            final String etag = "W/\"" + (photoProvider == null ? "mm-av" : username) + "-" + size + "\"";
            headers.setETag(etag);
            headers.setExpires(DateTime.now().plusWeeks(2).getMillis());
            headers.setCacheControl("max-age=1209600");

            if (etag.equals(ifNoneMatch)) {
                return new ResponseEntity<>(headers, HttpStatus.NOT_MODIFIED);
            }

            if (photoProvider != null) {
                headers.set("Content-Type", photoProvider.getMimeType());
                return new ResponseEntity<>(photoProvider.getCustomAvatar(size, size, PictureMode.ZOOM.name()), headers, HttpStatus.OK);
            } else {
                try (final InputStream mm = PhotographController.class.getClassLoader()
                        .getResourceAsStream("META-INF/resources/img/mysteryman.png")) {
                    headers.set("Content-Type", "image/png");
                    return new ResponseEntity<>(Avatar.process(mm, "image/png", size), headers, HttpStatus.OK);
                }
            }
        }

        throw BennuCoreDomainException.resourceNotFound(username);
    }

    @RequestMapping(value = "{size}/{username:.+}")
    public ResponseEntity<byte[]> getWithSize(@PathVariable String username, @PathVariable Integer size, @RequestHeader(
            value = "If-None-Match", required = false) String ifNoneMatch) throws IOException {
        return get(username, size, ifNoneMatch);
    }
}
