package br.ajlembalagens.controller;

import br.ajlembalagens.entity.Product;
import br.ajlembalagens.repository.ProductRepository;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakeException;
import io.quarkus.hibernate.orm.panache.PanacheQuery;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class ProductController {

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

    public Product create(Product product){
        if(product.getProductName() == null){
            throw new WebSocketClientHandshakeException("Erro ao cadastrar produto :(");
        }
        return product;
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
        product = (Product) repository.find("select *from product");
        if(product == null){
            return false;
        }
        return true;
    }

    public Product delete(Long id){
        Product productEntity = repository.findById(id);
        if(productEntity.getId() == null) {
            return null;
        }
        return productEntity;
    }
}
