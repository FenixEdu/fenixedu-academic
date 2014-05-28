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
package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.util.Config;
import com.google.common.base.Strings;

@Mapping(path = "/jcaptcha", module = "publico")
public class KaptchaAction extends Action {

    private static final Config config;
    private static final Producer kaptchaProducer;

    private static final String KAPTCHA_SESSION_KEY = "KAPTCHA_SESSION_KEY";

    static {
        Properties props = new Properties();
        props.put("kaptcha.border", "no");
        props.put("kaptcha.textproducer.font.color", "black");
        props.put("kaptcha.textproducer.char.space", "5");
        props.put("kaptcha.textproducer.char.length", "7");
        config = new Config(props);

        kaptchaProducer = config.getProducerImpl();
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        response.setHeader("Cache-Control", "no-store, no-cache");

        // return a jpeg
        response.setContentType("image/jpeg");

        // create the text for the image
        String capText = kaptchaProducer.createText();

        // create the image with the text
        BufferedImage bi = kaptchaProducer.createImage(capText);

        try (ServletOutputStream out = response.getOutputStream()) {

            // write the data out
            ImageIO.write(bi, "jpg", out);

            request.getSession().setAttribute(KAPTCHA_SESSION_KEY, new KaptchaSession(capText));
            return null;
        }
    }

    private static final class KaptchaSession implements Serializable {
        private static final long serialVersionUID = -1029174615006000407L;
        private final String value;
        private final DateTime creation;

        private KaptchaSession(String value) {
            this.value = value;
            this.creation = DateTime.now();
        }

        @Override
        public String toString() {
            return "!HIDDEN!";
        }
    }

    public static boolean validateResponse(HttpSession session, String value) {
        if (session == null || Strings.isNullOrEmpty(value)) {
            return false;
        }
        KaptchaSession key = (KaptchaSession) session.getAttribute(KAPTCHA_SESSION_KEY);
        return key != null && key.value.equals(value) && key.creation.plusMinutes(5).isAfterNow();
    }

}
