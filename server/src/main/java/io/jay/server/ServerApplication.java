package io.jay.server;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

interface CustomerJpaRepository extends JpaRepository<Customer, Long> {

}

@SpringBootApplication
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

}

@Entity
@Table(name = "customer")
@NoArgsConstructor
@Data
class Customer {
    @Id
    @GeneratedValue
    private Long id;
    private String name;

    public Customer(String name) {
        this.name = name;
    }
}

@Controller
@ResponseBody
@RequestMapping("/customers")
class CustomerController {
    private final CustomerJpaRepository repository;

    public CustomerController(CustomerJpaRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public Customer add(@RequestBody String name) {
        return repository.save(new Customer(name));
    }

    @GetMapping
    public List<Customer> all() {
        return repository.findAll();
    }

    @DeleteMapping
    public boolean delete(@RequestBody Long id) {

        var customerOptional = repository.findById(id);
        if (customerOptional.isEmpty()) {
            return false;
        }
        repository.delete(customerOptional.get());
        return true;
    }
}
