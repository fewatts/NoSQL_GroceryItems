package com.api.testing.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.testing.model.GroceryItem;

import com.api.testing.repository.ItemRepository;

@RestController
@RequestMapping("/Item")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ItemController {

    @Autowired
    private ItemRepository itemRepository;

    @GetMapping("/all")
    public ResponseEntity<List<GroceryItem>> findAll() {
        return ResponseEntity.ok(itemRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroceryItem> findById(@PathVariable String id) {
        return itemRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/register")
    public ResponseEntity<GroceryItem> postCustomer(@Validated @RequestBody GroceryItem item) {
        return ResponseEntity.status(HttpStatus.CREATED).body(itemRepository.save(item));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<GroceryItem> put(@PathVariable String id, @Validated @RequestBody GroceryItem item) {
        Optional<GroceryItem> existingItemOptional = itemRepository.findById(id);

        if (existingItemOptional.isPresent()) {
            GroceryItem existingItem = existingItemOptional.get();

            existingItem.setName(item.getName()); 
            existingItem.setCategory(item.getCategory()); 
            existingItem.setQuantity(item.getQuantity()); 

            GroceryItem updatedItem = itemRepository.save(existingItem);
            return ResponseEntity.ok(updatedItem);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable String id) {
        return itemRepository.findById(id)
                .map(existingItem -> {
                    itemRepository.deleteById(id);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

}
