package com.semjournals.rest.resources;

import com.semjournals.rest.util.UserUtil;

import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

/**
 * Abstract Resource with several utility methods to be used by child resources. It ensures the responses will all
 * follow the same pattern throughout the API.
 */
public abstract class AbstractResource {
    protected UserUtil userUtil = new UserUtil();

    protected Response ok() {
        return Response.ok().cacheControl(CacheControl.valueOf("no-cache")).build();
    }

    protected Response ok(final Object entity) {
        return Response.ok(entity).cacheControl(CacheControl.valueOf("no-cache")).build();
    }

    protected Response created(final UriInfo uriInfo, final String subResource) {
        final URI uri = uriInfo.getAbsolutePathBuilder().path(subResource).build();
        return Response.created(uri).cacheControl(CacheControl.valueOf("no-cache")).build();
    }

    protected Response status(final Status status) {
        return Response.status(status).cacheControl(CacheControl.valueOf("no-cache")).build();
    }
    protected Response status(final Status status, final Object entity, final MediaType mediaType) {
        return Response.status(status).entity(entity).type(mediaType)
                .cacheControl(CacheControl.valueOf("no-cache")).build();
    }

}
