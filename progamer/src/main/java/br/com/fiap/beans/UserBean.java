package br.com.fiap.beans;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import br.com.fiap.dao.UserDAO;
import br.com.fiap.model.User;

@Named
@RequestScoped
public class UserBean {

	private User user = new User();

	public void save() {
		new UserDAO().save(this.user);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Usuário cadastro com sucesso"));
	}
	
	//o controlador do JSF entende que sempre que um método Bean retorna uma String
	//quer acionar uma view, e então redireciona para ela.
	public String login() {
		User user = new UserDAO().exist(this.user);
		FacesContext context = FacesContext.getCurrentInstance();
		if (user != null ) {
			//colocando o ID no usuário
			context.getExternalContext().getSessionMap().put("user", user);
			return "index?faces-redirect=true";			
		} 
		
		context.getExternalContext().getFlash().setKeepMessages(true);
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login inválido", "Erro"));
		return "login?faces-redirect=true";	
	}
	
	//Método que pode ser chamado para fazer logout
	public String logout() {
		FacesContext context = FacesContext.getCurrentInstance();
		
		//Devemos remover um usuário do contexto da sessão.
		context.getExternalContext().getSessionMap().remove("user");
		
		//E redireciona para Login.
		return "login?faces-redirect=true ";
	}
	
	public List<User> getUsers() {
		return new UserDAO().getAll();
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
