package com.codev.api.resource;

import com.codev.domain.dto.form.TechnologyDTOForm;
import com.codev.domain.dto.view.TechnologyDTOView;
import com.codev.domain.service.TechnologyService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("technologies")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Technology")
public class TechnologyResource {

    @Inject
    TechnologyService technologyService;

    @POST
    @Path("/create-technology")
    public Response createTechnology(TechnologyDTOForm technologyDTOForm) {
        return Response.ok(new TechnologyDTOView(technologyService.createTechnology(technologyDTOForm))).build();
    }

    @PUT
    @Path("/{technologyId}/update-technology")
    public Response updateTechnology(
            @PathParam("technologyId") Long technologyId,
            TechnologyDTOForm technologyDTOForm
    ) {
        return Response.ok(new TechnologyDTOView(technologyService.updateTechnology(technologyId, technologyDTOForm))).build();
    }

    @DELETE
    @Path("/{technologyId}/delete-technology")
    public Response deleteTechnology(@PathParam("technologyId") Long technologyId) {
        technologyService.deleteTechnology(technologyId);
        return Response.ok().build();
        //todo: adicionar cascade
    }

}