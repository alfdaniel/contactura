package model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Criação de cosntrutores padrões
@AllArgsConstructor
//Cria contrutor
@NoArgsConstructor
//Cria todos os Gets e sets, assim como o Hashcode, etc
@Data
//Define quando uma classe é uma entidade do banco de dados
@Entity
public class Contact {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	private String email;
	private String phone;

}
