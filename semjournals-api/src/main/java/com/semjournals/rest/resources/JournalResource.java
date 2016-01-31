package com.semjournals.rest.resources;

import com.google.gson.Gson;
import com.semjournals.adapter.JournalAdapter;
import com.semjournals.model.Journal;
import com.semjournals.service.JournalService;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@Path("/journals/")
public class JournalResource extends AbstractResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listJournals() {

        List<JournalAdapter> journalAdapterList = new ArrayList<>();

        List<Journal> journalList;
        if (userUtil.isCurrentUserAdmin()) {
            journalList = new JournalService().list();
        } else {
            journalList = new JournalService().listActive();
        }

        journalList.forEach(journal -> {
            JournalAdapter journalAdapter = new JournalAdapter(journal);
            journalAdapterList.add(journalAdapter);
        });

        Gson gson = new Gson();
        return ok(gson.toJson(journalAdapterList));
    }

    @GET
    @Path("/{journalId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getJournalById(final @PathParam("journalId") String journalId) {
        JournalAdapter journalAdapter = new JournalAdapter(new JournalService().get(journalId));

        if (!journalAdapter.isActive() && !userUtil.isCurrentUserAdmin()) {
            return status(Response.Status.FORBIDDEN);
        }

        Gson gson = new Gson();
        return ok(gson.toJson(journalAdapter));
    }

    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(@FormDataParam("file") InputStream uploadedInputStream, @FormDataParam("file") FormDataContentDisposition fileDetail) {
//        String uploadedFileLocation = "d://uploaded/" + fileDetail.getFileName();
//
//        // save it
//        writeToFile(uploadedInputStream, uploadedFileLocation);
//
//        String output = "File uploaded to : " + uploadedFileLocation;
//
//        return Response.status(200).entity(output).build();
        return status(Response.Status.NOT_IMPLEMENTED);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createJournal(JournalAdapter createdJournalAdapter) {
        if (!userUtil.isCurrentUserAdmin()) {
            return status(Response.Status.FORBIDDEN);
        }

        JournalAdapter journalAdapter;
        try {
            journalAdapter = new JournalAdapter(new JournalService().create(createdJournalAdapter));
        } catch (UnsupportedEncodingException e) {
            return status(Response.Status.INTERNAL_SERVER_ERROR);
        }

        Gson gson = new Gson();
        return ok(gson.toJson(journalAdapter));
    }

    @PUT
    @Path("/{journalId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateJournal(final @PathParam("journalId") String journalId, JournalAdapter updateJournalAdapter) {
        if (!userUtil.isCurrentUserAdmin()) {
            return status(Response.Status.FORBIDDEN);
        }

        updateJournalAdapter.setId(journalId);
        JournalAdapter journalAdapter = new JournalAdapter(new JournalService().update(updateJournalAdapter));

        Gson gson = new Gson();
        return ok(gson.toJson(journalAdapter));
    }

    @DELETE
    @Path("/{journalId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteJournal(final @PathParam("journalId") String journalId) {
        if (!userUtil.isCurrentUserAdmin()) {
            return status(Response.Status.FORBIDDEN);
        }

        new JournalService().delete(journalId);

        return ok();
    }

    @PUT
    @Path("/{journalId}/active")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response activateJournal(final @PathParam("journalId") String journalId) {
        if (!userUtil.isCurrentUserAdmin()) {
            return status(Response.Status.FORBIDDEN);
        }

        JournalService service = new JournalService();
        Journal journal = service.get(journalId);
        journal.setActive(true);

        journal = service.update(new JournalAdapter(journal));
        JournalAdapter journalAdapter = new JournalAdapter(journal);

        Gson gson = new Gson();
        return ok(gson.toJson(journalAdapter));
    }

    @DELETE
    @Path("/{journalId}/active")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deactivateJournal(final @PathParam("journalId") String journalId) {
        if (!userUtil.isCurrentUserAdmin()) {
            return status(Response.Status.FORBIDDEN);
        }

        JournalService service = new JournalService();
        Journal journal = service.get(journalId);
        journal.setActive(false);

        journal = service.update(new JournalAdapter(journal));
        JournalAdapter journalAdapter = new JournalAdapter(journal);

        Gson gson = new Gson();
        return ok(gson.toJson(journalAdapter));
    }

}