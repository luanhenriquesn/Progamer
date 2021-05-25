package br.com.fiap.beans;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import br.com.fiap.dao.SetupDAO;
import br.com.fiap.model.Setup;
import br.com.fiap.model.User;

/* A classe bean deve ser apenas uma classe intermediária, que possui os métodos (por exemplo, salvar), 
   e a classe model deve guardar o objeto, com seus atributos, gets e sets. 
   E a classe DAO guarda os métodos de acesso ao banco de dados.
   Então a classe Bean chama a classe Model, e a classe DAO.
 
  Agora,temos uma notação mais genérica, a @Named , 
  que apenas indica que aquele objeto é gerenciado por um container qualquer.
 */


@Named
@RequestScoped
public class SetupBean {
	
	private Setup setup = new Setup();


	public void save() {
		//Pegando o usuário da sessão
		FacesContext context = FacesContext.getCurrentInstance();
		User user = (User) context.getExternalContext().getSessionMap().get("user");
		
		//Relacionando o usuário com setup e cadastrando no banco
		this.setup.setUser(user);
		new SetupDAO().save(this.setup);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Setup cadastrado com sucesso"));
		
		
		
	}
	
	public List<Setup> getSetups() {
		return new SetupDAO().getAll();
		
	}
	
	public List<Setup> getUserSetups(){
		//Pegando o usuário da sessão
		FacesContext context = FacesContext.getCurrentInstance();
		User user = (User) context.getExternalContext().getSessionMap().get("user");
		
		return new SetupDAO().getUserSetups(user.getId());
	}

	public Setup getSetup() {
		return setup;
	}

	public void setSetup(Setup setup) {
		this.setup = setup;
	}

	
	
}
