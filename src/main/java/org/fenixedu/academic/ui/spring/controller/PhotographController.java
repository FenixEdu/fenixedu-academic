package org.fenixedu.academic.ui;

import java.io.IOException;
import java.io.InputStream;

import net.sourceforge.fenixedu.domain.Photograph;
import net.sourceforge.fenixedu.domain.photograph.PictureMode;

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

@Controller
@RequestMapping("/user/photo")
public class PhotographController {

    @RequestMapping(value = "{username}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> get(@PathVariable String username, @RequestParam(value = "s", required = false,
            defaultValue = "100") Integer size, @RequestHeader(value = "If-None-Match", required = false) String ifNoneMatch)
            throws IOException {

        User user = User.findByUsername(username);

        if (user != null && user.getPerson() != null) {
            final Photograph personalPhoto =
                    user.getPerson().isPhotoAvailableToCurrentUser() ? user.getPerson().getPersonalPhoto() : null;

            HttpHeaders headers = new HttpHeaders();
            String etag = "W/\"" + (personalPhoto == null ? "mm-av" : personalPhoto.getExternalId()) + "-" + size + "\"";
            headers.setETag(etag);
            headers.setExpires(DateTime.now().plusHours(12).getMillis());
            headers.setCacheControl("max-age=43200");

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

}
