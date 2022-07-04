/**
 * @author Luiz Henrique de Sousa Ribas - lhs.ribas@gmail.com
 * @linkedin https://www.linkedin.com/in/lhsribas/
 * @date 24 Mar 2022
 * @version 1
 */
package com.quarkus.clean_arch.crud.app.rest;

import com.quarkus.clean_arch.crud.app.dto.PaymentRequest;
import com.quarkus.clean_arch.crud.app.service.PaymentService;
import com.quarkus.clean_arch.crud.infra.db.model.Payment;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.xml.validation.Validator;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@Path("/v/payments")
public class PaymentResource {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentResource.class);

    @Inject
    Validator validator;
    @Inject
    private PaymentService paymentService;

    @POST
    public Uni<Response> save( final PaymentRequest request) {
        LOG.info("Create: {}", request.customer);
        return paymentService.save(request)
                .onItem().transform(response -> Response.ok(URI.create("/bin/" + response.customer)).status(Response.Status.CREATED))
                .onItem().transform(Response.ResponseBuilder::build);
    }
    @GET
    @Path("{id}")
    public Uni<Response> getById(@PathParam("id") Long id){
        LOG.info("Get by id: {}", id);
        return paymentService.getById(id)
                .onItem().transform(response -> Response.ok(response))
                .onItem().transform(Response.ResponseBuilder::build);
    }
    @GET
    public Uni<Response> getAll(){
        LOG.info("Get all ...");
        return paymentService.getAll()
                .onItem().transform(response -> Response.ok(response))
                .onItem().transform(Response.ResponseBuilder::build);
    }
    @DELETE
    @Path("{id}")
    public Uni<Response> delete(@PathParam("id") Long id){
        LOG.info("Delete by id...");
        return paymentService.delete(id)
                .onItem().transform(response -> Response.ok(response))
                .onItem().transform(Response.ResponseBuilder::build);
    }
    @PUT
    @Path("{id}")
    public Uni<Response> update(@PathParam("id") Long id, PaymentRequest request) {
        Objects.nonNull(request);
        return paymentService.update(id, request)
                .onItem().ifNotNull().transform(entity -> Response.ok(entity).build())
                .onItem().ifNull().continueWith(Response.ok().status(Response.Status.NOT_FOUND)::build);
    }
}
