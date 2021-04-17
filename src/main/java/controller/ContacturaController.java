package controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import repository.ContacturaRepository;

@RestController
@RequestMapping({ "/contactura" })
public class ContacturaController {

	@Autowired
	private ContacturaRepository repository;
	
	
// lista todos os contatos - http://localhost:8090/contactura
	@GetMapping
	public List findALL(){
		return repository.findAll();
	}
	
//Pesquisar pelo ID - http://localhost:8090/contactura/{id}
		@GetMapping(value = "{id}")
		public ResponseEntity findById(@PathVariable Long id) {
			return repository.findById(id)
					.map(record -> ResponseEntity.ok().body(record))
					.orElse(ResponseEntity.notFound().build());
		}
}