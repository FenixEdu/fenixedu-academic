/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.spring.controller;

import java.io.IOException;
import java.io.InputStream;

import org.fenixedu.academic.domain.Photograph;
import org.fenixedu.academic.domain.photograph.PictureMode;
import org.fenixedu.academic.service.services.fileManager.UploadOwnPhoto;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.ContentType;
import org.fenixedu.bennu.core.domain.Avatar;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.exceptions.BennuCoreDomainException;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Strings;
import com.google.common.io.BaseEncoding;
import com.google.common.io.ByteStreams;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.twilio.sdk.verbs.Redirect;

@Controller
@RequestMapping("/user/photo")
public class PhotographController {

    public static int MAX_PHOTO_SIZE = 1048576; //1M

    @RequestMapping(value = "{username:.+}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> get(@PathVariable String username, @RequestParam(value = "s", required = false,
            defaultValue = "100") Integer size, @RequestHeader(value = "If-None-Match", required = false) String ifNoneMatch)
            throws IOException {

        if (size <= 0) {
            size = 100;
        }
        if (size > 512) {
            size = 512;
        }

        User user = User.findByUsername(username);

        if (user != null && user.getPerson() != null) {
            final Photograph personalPhoto =
                    user.getPerson().isPhotoAvailableToCurrentUser() ? user.getPerson().getPersonalPhoto() : null;

            HttpHeaders headers = new HttpHeaders();
            String etag = "W/\"" + (personalPhoto == null ? "mm-av" : personalPhoto.getExternalId()) + "-" + size + "\"";
            headers.setETag(etag);
            headers.setExpires(DateTime.now().plusWeeks(2).getMillis());
            headers.setCacheControl("max-age=1209600");

            if (etag.equals(ifNoneMatch)) {
                return new ResponseEntity<>(headers, HttpStatus.NOT_MODIFIED);
            }

            if (personalPhoto != null) {
                headers.set("Content-Type", personalPhoto.getOriginal().getPictureFileFormat().getMimeType());
                return new ResponseEntity<>(personalPhoto.getCustomAvatar(size, size, PictureMode.ZOOM), headers, HttpStatus.OK);
            } else {
                try (InputStream mm =
                        PhotographController.class.getClassLoader().getResourceAsStream("META-INF/resources/img/mysteryman.png")) {
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

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public @ResponseBody String upload(@RequestBody final PhotographForm jsonPhotographForm) {
        final JsonObject response = new JsonObject();
        String encodedPhoto = jsonPhotographForm.getEncodedPhoto();
        if (Strings.isNullOrEmpty(encodedPhoto)) {
            response.addProperty("reload", "true");
            return new GsonBuilder().create().toJson(response);
        }
        String encodedContent = encodedPhoto.split(",")[1];
        String photoContentType = encodedPhoto.split(",")[0].split(":")[1].split(";")[0];
        byte[] photoContent = BaseEncoding.base64().decode(encodedContent);
        if (photoContent.length > MAX_PHOTO_SIZE) {
            response.addProperty("error", "true");
            response.addProperty("message", BundleUtil.getString(Bundle.MANAGER, "errors.fileTooLarge"));
            return new GsonBuilder().create().toJson(response);
        }
        UploadOwnPhoto.run(photoContent, ContentType.getContentType(photoContentType));
        response.addProperty("success", "true");
        return new GsonBuilder().create().toJson(response);
    }
}
