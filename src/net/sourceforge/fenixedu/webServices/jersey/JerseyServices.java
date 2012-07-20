package net.sourceforge.fenixedu.webServices.jersey;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.json.simple.JSONObject;

@Path("/services")
public class JerseyServices {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("helloworld")
    public String helloworld(@QueryParam("__username__")
    String __username__, @QueryParam("__password__") String __password__) {
	JSONObject obj = new JSONObject();
	obj.put("msg", "hello world!");
	obj.put("x", __username__);
	obj.put("y", __password__);
	return obj.toJSONString();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("hellofenix")
    public String hellofenix() {
	return "Hello!";
    }

}
