package net.sourceforge.fenixedu.webServices.jersey.api;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;

public class FenixJerseyAuthScopeRequestFilter implements ContainerRequestFilter {

    @Override
    public ContainerRequest filter(ContainerRequest request) {
        String path = request.getPath();
        System.out.println(path);
        return request;
    }

}
