package br.ajlembalagens.service;

import br.ajlembalagens.entity.Product;
import br.ajlembalagens.repository.ProductRepository;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakeException;
import io.quarkus.hibernate.orm.panache.PanacheQuery;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class ProductService{

    @Inject
    private ProductRepository repository;

    public Product update(Long id, Product product){
        Product productEntity = repository.findById(id);

        if(productEntity == null){
            throw new WebSocketClientHandshakeException("Produto com id " + id + "não encontrado :(");
        }

        productEntity.setProductName(product.getProductName());
        productEntity.setProductCode(product.getProductCode());
        productEntity.setDescription(product.getDescription());
        productEntity.setPrice(product.getPrice());

        return productEntity;
    }

    public Product getProductById(Long id){
        Product productEntity = repository.findById(id);
        return productEntity;
    }


    /**
            * This method is main purpose to show simple "Business" example
     * @param product
     * @return
             */
    public boolean isFoodNameIsNotEmpty(Product product) {
        return product.getProductName().isEmpty();

    }

    public boolean isExistsProductCode(String code, Product product){

        PanacheQuery<Product> query = repository.findAll();
        List<Product> productSearch = query.list();

        for(Product p: productSearch ){
            if(p.getProductCode().equals(product.getProductCode())){
               return true;
            }
        }
        return false;
    }

    public Product delete(Long id){
        Product productEntity = repository.findById(id);
        return productEntity;
    }
}
