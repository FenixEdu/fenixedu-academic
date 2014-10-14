package pt.ist.fenix.filters;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.FenixIstConfiguration;

import org.fenixedu.bennu.core.util.CoreConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

@WebFilter("/downloadFile/*")
public class FileDownloadLegacyRedirectFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(FileDownloadLegacyRedirectFilter.class);

    private static final String SERVLET_PATH = "/downloadFile/";

    private static final Map<String, String> mappings = new HashMap<>();

    @Override
    public void init(FilterConfig config) {
        String appUrl = CoreConfiguration.getConfiguration().applicationUrl();
        String mapLocation = FenixIstConfiguration.getConfiguration().legacyFilesRedirectMapLocation();
        if (!Strings.isNullOrEmpty(mapLocation)) {
            try {
                JsonElement map = new JsonParser().parse(new FileReader(mapLocation));
                for (JsonElement element : map.getAsJsonArray()) {
                    JsonObject mapping = element.getAsJsonObject();
                    String legacy = mapping.get("legacy").getAsString();
                    if (mapping.has("internalRedirect")) {
                        mappings.put(legacy, appUrl + SERVLET_PATH + mapping.get("internalRedirect").getAsString());
                    } else if (mapping.has("externalRedirect")) {
                        mappings.put(legacy, mapping.get("externalRedirect").getAsString());
                    } else {
                        logger.warn("Unknown file download legacy mapping format: {}", element.toString());
                    }
                }
            } catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
                logger.info("Could not read {} legacy file map location", mapLocation);
            }
        }
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
            throws IOException, ServletException {
        String legacy = getOidFromUrl(((HttpServletRequest) request).getRequestURI());
        if (mappings.containsKey(legacy)) {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
            httpResponse.setHeader("Location", mappings.get(legacy));
            return;
        }
        chain.doFilter(request, response);
    }

    public final static String getOidFromUrl(String url) {
        try {
            String[] parts = url.substring(url.indexOf(SERVLET_PATH)).replace(SERVLET_PATH, "").split("\\/");
            if (parts.length == 0) {
                return null;
            }
            return parts[0];
        } catch (Exception e) {
            return null;
        }
    }
}
