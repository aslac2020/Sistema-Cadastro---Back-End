package br.ajlembalagens;

import br.ajlembalagens.controller.ProductController;
import br.ajlembalagens.entity.Product;
import br.ajlembalagens.repository.ProductRepository;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.List;

@Path("/products")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)

@OpenAPIDefinition(
        tags = {
                @Tag(name="widget", description="Widget operations."),
                @Tag(name="gasket", description="Operations related to gaskets")
        },
        info = @Info(
                title="Example API",
                version = "1.0.1",
                contact = @Contact(
                        name = "Api Cadastro",
                        url = "http://exampleurl.com/contact",
                        email = "techsupport@example.com"),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0.html"))
)
public class ProductResource {

    private ProductRepository repository;

    @Inject
    public ProductResource(ProductRepository repository){
        this.repository = repository;
    }

    @Inject
    private ProductController productController;

    @GET
    public Response findAll() {
        PanacheQuery<Product> query = repository.findAll();
        return Response.ok(query.list()).build();
    }

    @GET
    @Path("{id}")
    public Response findProductById(@PathParam("id") Long id){

        Product productEntity = repository.findById(id);

        if(productEntity == null){
            return Response.ok("Id do produto não existe").type(MediaType.APPLICATION_JSON_TYPE).build();
        }

        return Response.ok(productEntity).status(200).build();

    }

    @POST
    @Transactional
    public Response create(Product product){

        if(productController.isFoodNameIsNotEmpty(product)){
            return Response.ok("Favor informar nome do produto").type(MediaType.APPLICATION_JSON_TYPE).build();
        }

        PanacheQuery<Product> query = repository.findAll();
        List<Product> productSearch = query.list();

        for(Product p: productSearch ){
            if(p.getProductCode().equals(product.getProductCode())){
                return Response.status(400). entity( " Codigo do produto : " + product.getProductCode() +" já existe, favor inserir outro código").build();
            }
        }

        repository.persist(product);
        return Response.ok(product).status(201).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Response update(@PathParam("id")Long id, Product product){

        if(productController.isFoodNameIsNotEmpty(product)){
                return Response.ok("Produto não existe").type(MediaType.APPLICATION_JSON_TYPE).build();
        }
        Product productEntity = productController.update(id, product);

        return Response.ok(productEntity).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response delete(@PathParam("id")Long id) {
        Product productEntity = repository.findById(id);

        if (productEntity == null) {
            return Response.status(400).entity("Produto não encontrado :(").build();
        }

        repository.delete(productEntity);
        return Response.ok("Produto excluido com sucesso !").status(200).build();
    }

    @Provider
    public class CORSFilter implements ContainerResponseFilter {

        @Override
        public void filter(final ContainerRequestContext requestContext,
                           final ContainerResponseContext cres) throws IOException {
            cres.getHeaders().add("Access-Control-Allow-Origin", "*");
            cres.getHeaders().add("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
            cres.getHeaders().add("Access-Control-Allow-Credentials", "true");
            cres.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
            cres.getHeaders().add("Access-Control-Max-Age", "1209600");
        }
    }

}