package com.semjournals.rest.resources;

import com.google.gson.Gson;
import com.semjournals.adapter.AccountAdapter;
import com.semjournals.data.dao.AccountDAO;
import com.semjournals.data.dao.RoleDAO;
import com.semjournals.model.Account;
import com.semjournals.model.Role;
import com.semjournals.service.AccountService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@Path("/accounts/")
public class AccountsResource extends AbstractResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listAccounts() {
        if (!userUtil.isCurrentUserAdmin()) {
            return status(Response.Status.FORBIDDEN);
        }

        List<AccountAdapter> accountAdapterList = new ArrayList<>();
        List<Account> accountList = new AccountService().list();
        accountList.forEach(account -> {
            AccountAdapter accountAdapter = new AccountAdapter(account);
            accountAdapterList.add(accountAdapter);
        });

        Gson gson = new Gson();
        return ok(gson.toJson(accountAdapterList));
    }

    @GET
    @Path("/{accountId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAccountById(final @PathParam("accountId") String accountId) {
        if (!userUtil.isCurrentUserAdmin() && !accountId.equals(userUtil.getCurrentAccount().getId())) {
            return status(Response.Status.FORBIDDEN);
        }

        AccountAdapter accountAdapter = new AccountAdapter(new AccountService().get(accountId));

        Gson gson = new Gson();
        return ok(gson.toJson(accountAdapter));
    }

    @GET
    @Path("/self")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSelfAccount() {
        Account account = userUtil.getCurrentAccount();

        AccountAdapter accountAdapter = new AccountAdapter(account);

        Gson gson = new Gson();
        return ok(gson.toJson(accountAdapter));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createAccount(AccountAdapter createdAccountAdapter) {
        if (!userUtil.isCurrentUserAdmin()) {
            return status(Response.Status.FORBIDDEN);
        }

        AccountAdapter accountAdapter;
        try {
            accountAdapter = new AccountAdapter(new AccountService().create(createdAccountAdapter));
        } catch (UnsupportedEncodingException e) {
            return status(Response.Status.INTERNAL_SERVER_ERROR);
        }

        Gson gson = new Gson();
        return ok(gson.toJson(accountAdapter));
    }

    @PUT
    @Path("/{accountId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateAccount(final @PathParam("accountId") String accountId, AccountAdapter updateAccountAdapter) {
        if (!userUtil.isCurrentUserAdmin() && !accountId.equals(userUtil.getCurrentAccount().getId())) {
            return status(Response.Status.FORBIDDEN);
        }

        updateAccountAdapter.setId(accountId);
        AccountAdapter accountAdapter = new AccountAdapter(new AccountService().update(updateAccountAdapter));

        Gson gson = new Gson();
        return ok(gson.toJson(accountAdapter));
    }

    @DELETE
    @Path("/{accountId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteAccount(final @PathParam("accountId") String accountId) {
        if (!userUtil.isCurrentUserAdmin() && !accountId.equals(userUtil.getCurrentAccount().getId())) {
            return status(Response.Status.FORBIDDEN);
        }

        new AccountService().delete(accountId);

        return ok();
    }

    @PUT
    @Path("/{accountId}/active")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response activateAccount(final @PathParam("accountId") String accountId) {
        if (!userUtil.isCurrentUserAdmin()) {
            return status(Response.Status.FORBIDDEN);
        }

        AccountService service = new AccountService();
        Account account = service.get(accountId);
        account.setActive(true);

        account = service.update(new AccountAdapter(account));
        AccountAdapter accountAdapter = new AccountAdapter(account);

        Gson gson = new Gson();
        return ok(gson.toJson(accountAdapter));
    }

    @DELETE
    @Path("/{accountId}/active")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deactivateAccount(final @PathParam("accountId") String accountId) {
        if (!userUtil.isCurrentUserAdmin()) {
            return status(Response.Status.FORBIDDEN);
        }

        AccountService service = new AccountService();
        Account account = service.get(accountId);
        account.setActive(false);

        account = service.update(new AccountAdapter(account));
        AccountAdapter accountAdapter = new AccountAdapter(account);

        Gson gson = new Gson();
        return ok(gson.toJson(accountAdapter));
    }

    @PUT
    @Path("/{accountId}/admin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addAdminRole(final @PathParam("accountId") String accountId) {
        if (!userUtil.isCurrentUserAdmin()) {
            return status(Response.Status.FORBIDDEN);
        }

        Role role = new RoleDAO().get("name", "admin");

        AccountService service = new AccountService();
        Account account = service.get(accountId);
        account.setRole(role);

        account = service.update(new AccountAdapter(account));
        AccountAdapter accountAdapter = new AccountAdapter(account);

        Gson gson = new Gson();
        return ok(gson.toJson(accountAdapter));
    }

    @DELETE
    @Path("/{accountId}/admin")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeAdminRole(final @PathParam("accountId") String accountId) {
        if (!userUtil.isCurrentUserAdmin()) {
            return status(Response.Status.FORBIDDEN);
        }

        Role role = new RoleDAO().get("name", "user");

        AccountService service = new AccountService();
        Account account = service.get(accountId);
        account.setRole(role);

        account = service.update(new AccountAdapter(account));
        AccountAdapter accountAdapter = new AccountAdapter(account);

        Gson gson = new Gson();
        return ok(gson.toJson(accountAdapter));
    }
}