package com.contactura.contactura.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.contactura.contactura.model.ContacturaUser;
import com.contactura.contactura.repository.ContacturaUserRepository;

@Component
public class CustomUserDetailService implements UserDetailsService {

	//Método do spring que retorna um userDetails, buscando o user atravez do repositório, recebendo a instância do repositorio do user local
	private final ContacturaUserRepository contacturaUserRepository;
	
	@Autowired
	public CustomUserDetailService(ContacturaUserRepository contacturaUserRepository){
		this.contacturaUserRepository = contacturaUserRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		ContacturaUser user = Optional.ofNullable(contacturaUserRepository.findByUsername(username))
				.orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
		
		//Lista que retornar as autorizações e permissões para cada tipo de usuario
		List<GrantedAuthority> autorityListAdmin = AuthorityUtils.createAuthorityList("ROLE_USER", "ROLE_ADMIN");
		List<GrantedAuthority> autorityListUser = AuthorityUtils.createAuthorityList("ROLE_USER");
		
		//Inserindo os dados do meu model de usuario diretamente
		//dentro do model de usuario do springSecurity, e validando as permissões de administrador ou user
		return new org.springframework.security.core.userdetails.User
				(user.getUsername(), user.getPassword(), user.isAdmin() ? autorityListAdmin : autorityListUser);
		
	}	
	
	
}
