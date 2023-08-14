package com.codev.api.resource;

import com.codev.domain.dto.form.TechnologyDTOForm;
import com.codev.domain.dto.view.TechnologyDTOView;
import com.codev.domain.service.TechnologyService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("technologies")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Technology")
public class TechnologyResource {

    @Inject
    TechnologyService technologyService;

    @RolesAllowed({"ADMIN", "USER"})
    @GET
    public Response findAllTechnologies() {
        List<TechnologyDTOView> technologies = technologyService.findAllTechnologies()
            .stream().map(TechnologyDTOView::new).toList();
        
        return Response.ok(technologies).build();
    }

    @RolesAllowed({"ADMIN"})
    @POST
    public Response createTechnology(@Valid TechnologyDTOForm technologyDTOForm) {
        return Response.ok(new TechnologyDTOView(technologyService.createTechnology(technologyDTOForm))).build();
    }

    @RolesAllowed({"ADMIN"})
    @PUT
    @Path("/{technologyId}")
    public Response updateTechnology(
            @PathParam("technologyId") UUID technologyId,
            @Valid TechnologyDTOForm technologyDTOForm
    ) {
        return Response.ok(new TechnologyDTOView(technologyService.updateTechnology(technologyId, technologyDTOForm))).build();
    }

    @RolesAllowed({"ADMIN"})
    @DELETE
    @Path("/{technologyId}")
    public Response deleteTechnology(@PathParam("technologyId") UUID technologyId) {
        technologyService.deleteTechnology(technologyId);
        return Response.ok().build();
    }

}