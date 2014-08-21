package org.fenixedu.academic.ui;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Photograph;
import net.sourceforge.fenixedu.domain.photograph.PictureMode;

import org.fenixedu.bennu.core.domain.Avatar;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.exceptions.BennuCoreDomainException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/user/photo")
public class PhotographController {
    @RequestMapping(value = "{username}", method = RequestMethod.GET)
    public void get(@PathVariable String username,
            @RequestParam(value = "s", required = false, defaultValue = "100") Integer size, HttpServletResponse response)
            throws IOException {
        User user = User.findByUsername(username);
        if (user != null && user.getPerson() != null) {
            final Photograph personalPhoto = user.getPerson().getPersonalPhoto();
            if (personalPhoto != null && user.getPerson().isPhotoAvailableToCurrentUser()) {
                response.setContentType(personalPhoto.getOriginal().getPictureFileFormat().getMimeType());
                response.getOutputStream().write(personalPhoto.getCustomAvatar(size, size, PictureMode.ZOOM));
                return;
            }
            try (InputStream mm =
                    PhotographController.class.getClassLoader().getResourceAsStream("META-INF/resources/img/mysteryman.png")) {
                response.setContentType("image/png");
                response.getOutputStream().write(Avatar.process(mm, "image/png", size));
            }
            return;
        }
        throw BennuCoreDomainException.resourceNotFound(username);
    }
}
