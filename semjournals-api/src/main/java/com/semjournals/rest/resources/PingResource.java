package com.semjournals.rest.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/ping")
public class PingResource extends AbstractResource {
 
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response check() {
        return ok();
    }

}