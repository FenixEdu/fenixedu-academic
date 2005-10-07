/*
 * Copyright 2005 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.myfaces.component.html.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileUpload;


/**
 * This filters is mandatory for the use of many components.
 * It handles the Multipart requests (for file upload)
 * It's used by the components that need javascript libraries
 *
 * @author Sylvain Vieujot (latest modification by $Author$)
 * @author <a href="mailto:oliver@rossmueller.com">Oliver Rossmueller </a>
 * @version $Revision$ $Date$
 */
public class MultipartFilter implements Filter
{

    private int uploadMaxFileSize = 100 * 1024 * 1024; // 10 MB

    private int uploadThresholdSize = 1 * 1024 * 1024; // 1 MB

    private String uploadRepositoryPath = null; //standard temp directory


    public void init(FilterConfig filterConfig)
    {
        uploadMaxFileSize = resolveSize(filterConfig.getInitParameter("uploadMaxFileSize"), uploadMaxFileSize);
        uploadThresholdSize = resolveSize(filterConfig.getInitParameter("uploadThresholdSize"), uploadThresholdSize);
        uploadRepositoryPath = filterConfig.getInitParameter("uploadRepositoryPath");
    }


    private int resolveSize(String param, int defaultValue)
    {
        int numberParam = defaultValue;

        if (param != null)
        {
            param = param.toLowerCase();
            int factor = 1;
            String number = param;

            if (param.endsWith("g"))
            {
                factor = 1024 * 1024 * 1024;
                number = param.substring(0, param.length() - 1);
            } else if (param.endsWith("m"))
            {
                factor = 1024 * 1024;
                number = param.substring(0, param.length() - 1);
            } else if (param.endsWith("k"))
            {
                factor = 1024;
                number = param.substring(0, param.length() - 1);
            }

            numberParam = Integer.parseInt(number) * factor;
        }
        return numberParam;
    }


    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        if (!(response instanceof HttpServletResponse))
        {
            chain.doFilter(request, response);
            return;
        }

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        // For multipart/form-data requests
        if (FileUpload.isMultipartContent(httpRequest))
        {
            MultipartRequestWrapper multipartRequestWrapper = new MultipartRequestWrapper(httpRequest, uploadMaxFileSize, uploadThresholdSize, uploadRepositoryPath);
            multipartRequestWrapper.setAttribute("multipartRequestWrapper", multipartRequestWrapper);
            chain.doFilter(multipartRequestWrapper, response);
        } else
        {
            chain.doFilter(request, response);
        }
    }


    public void destroy()
    {
        // NoOp
    }
}
