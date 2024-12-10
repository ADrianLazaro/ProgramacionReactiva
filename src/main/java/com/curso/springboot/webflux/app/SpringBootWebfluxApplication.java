package com.curso.springboot.webflux.app;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

import com.curso.springboot.webflux.app.models.dao.ProductoDao;
import com.curso.springboot.webflux.app.models.documents.Producto;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class SpringBootWebfluxApplication implements CommandLineRunner {
	
	@Autowired
	private ProductoDao dao;
	
	@Autowired
	private ReactiveMongoTemplate mongoTemplate;
	
	private static final Logger log = LoggerFactory.getLogger(SpringBootWebfluxApplication.class);
	 
	public static void main(String[] args) {
		SpringApplication.run(SpringBootWebfluxApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception{
		
		mongoTemplate.dropCollection("productos").subscribe();
		
		Flux.just(new Producto("Tv Panasonic", 213.2),
				new Producto("Tv Samsung", 432.45),
				new Producto("Radio Sony", 120.99),
				new Producto("Licuadora Oster", 79.50),
				new Producto("Cocina Electrolux", 399.99),
				new Producto("Refrigerador Whirlpool", 799.00),
				new Producto("Aire Acondicionado LG", 599.99),
				new Producto("Microondas Sharp", 149.89),
				new Producto("Lavarropas Bosch", 350.75)
				)
		.flatMap(producto -> {
			producto.setCreateAt(new Date());
			return dao.save(producto);
		})
		.subscribe(producto -> log.info("Insert: "+ producto.getId()+ " " + producto.getNombre()));
	}
	
}
