package io.jay.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@SpringBootApplication
public class ClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
    }
}

@Configuration
class CustomerHttpProxyConfiguration {

    @Bean
    public CustomerHttpClient client(WebClient.Builder builder) {
        var wc = builder.baseUrl("http://localhost:8080/customers").build();
        return HttpServiceProxyFactory
                .builder()
                .clientAdapter(WebClientAdapter.forClient(wc))
                .build()
                .createClient(CustomerHttpClient.class);
    }
}

@Controller
class EdgeController {

    private final CustomerHttpClient httpClient;

    EdgeController(CustomerHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @QueryMapping
    public Flux<Customer> customers() {
        return httpClient.all();
    }

    @MutationMapping
    public Mono<Customer> add(@Argument String name) {
        return httpClient.save(name);
    }

    @MutationMapping
    public Mono<Boolean> delete(@Argument Long id) {
        return httpClient.delete(id);
    }
}

interface CustomerHttpClient {
    @GetExchange
    Flux<Customer> all();

    @PostExchange
    Mono<Customer> save(@RequestBody String name);

    @DeleteExchange
    Mono<Boolean> delete(@RequestBody Long id);
}

record Customer(Long id, String name) {

}
