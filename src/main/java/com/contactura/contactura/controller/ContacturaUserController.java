package com.contactura.contactura.controller;

import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.contactura.contactura.model.ContacturaUser;
import com.contactura.contactura.repository.ContacturaUserRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


@CrossOrigin()
@RestController
@RequestMapping({"/user"})
public class ContacturaUserController {
	
	public static Gson gson = new GsonBuilder().setPrettyPrinting().create();
	
	@Autowired
	private ContacturaUserRepository repository;
	
	@RequestMapping("/login")
	@GetMapping
	public ResponseEntity<?> login(HttpServletRequest request){
		//https://stackoverflow.com/questions/58150278/java-base64-decode
		//https://developer.mozilla.org/pt-BR/docs/Web/API/WindowOrWorkerGlobalScope/atob
		String valueDecode = new String(Base64.getDecoder().decode(request.getHeader("Authorization").split(" ")[1]));
		boolean adminTrueFalse = repository.findByUsername(valueDecode.split(":")[0]).isAdmin();
		String token = request.getHeader("Authorization").substring("Basic".length()).trim();
		return ResponseEntity.ok().body(gson.toJson(token + ":" + adminTrueFalse));
//	public ResponseEntity login(HttpServletRequest request){			
//		String token = request.getHeader("Authorization")
//				.substring("Basic".length()).trim();
//		return ResponseEntity.ok().body(gson.toJson(token));
	}
	
// Lista todos usu??rios
// http://localhost:8090/user
		@GetMapping
		public List findALL(){
			return repository.findAll();
		}
		
// Lista usu??rios por id
// http://localhost:8090/user/id
		@GetMapping(value = "{id}")
		public ResponseEntity<?> findById(@PathVariable long id) {
			return repository.findById(id)
					.map(user -> ResponseEntity.ok().body(user))
					.orElse(ResponseEntity.notFound().build());
				}
		
// Criar novo Usu??rio
// http://localhost:8090/user
		@PostMapping
		@PreAuthorize("hasRole('ADMIN')")
		public ContacturaUser create(@RequestBody ContacturaUser user){
			user.setPassword(criptografarPassword(user.getPassword()));
			return repository.save(user);
		}
	
// Atualizar usu??rio
// http://localhost:8090/user/id
		@PutMapping(value = "{id}")
		@PreAuthorize("hasRole('ADMIN')")
		public ResponseEntity<?> update(@PathVariable long id, @RequestBody ContacturaUser user){
			return repository.findById(id)
					.map(record -> {
						record.setName(user.getName());
						record.setUsername(user.getUsername());
						record.setPassword(criptografarPassword(user.getPassword()));
						record.setAdmin(false);
						ContacturaUser update = repository.save(record);
						return ResponseEntity.ok().body(update);
					}).orElse(ResponseEntity.notFound().build());
		}
		
// Deletar Usu??ro
// http://localhost:8090/user/id
		@DeleteMapping(path = {"/{id}"})
		@PreAuthorize("hasRole('ADMIN')")
		public ResponseEntity<?> delete(@PathVariable long id) {
			return repository.findById(id)
					.map(record -> {
						repository.deleteById(id);
						return ResponseEntity.ok().body(record);
						}).orElse(ResponseEntity.notFound().build());
		}
		
		private String criptografarPassword(String password) {
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			String passwordParaCriptografar = passwordEncoder.encode(password);			
			return passwordParaCriptografar;
		}

}
