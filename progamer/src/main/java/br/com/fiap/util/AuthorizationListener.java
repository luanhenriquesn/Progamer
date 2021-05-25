package br.com.fiap.util;

import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import br.com.fiap.model.User;

//Como o authorizationlistener é um phaselistener, deve implementar a intercace PhaseListener
//event representa a phase que estou atualmente.

public class AuthorizationListener implements PhaseListener{
	
	private static final long serialVersionUID = 1L;

	
	//Colocaremos o AuthorizationListener depois da fase de RestoreView
	//Assim, conseguiremos saber a página que ele está tentando ir, e interceptar se for preciso.
	@Override
	public void afterPhase(PhaseEvent event) {
		
		FacesContext context = FacesContext.getCurrentInstance();
		
		//Esta variável está representando o nome da página que queremos acessar.
		String page = context.getViewRoot().getViewId();
		//Se estivermos tentando acessar a página de Login, a verificação não precisa ser realizada, então encerramos o método.
		if (page.equals("/login.xhtml")) return;
		
		//Pegando o objeto usuário que está no contexto da sessão - mapeado pela chave "user"
		User user = (User) context.getExternalContext().getSessionMap().get("user");
		
		//Se tiver usuário, não precisamos redirecionar nem nada, então encerra este método.
		if (user != null) return;
		
		//Se não tiver usuário, iremos redirecionar paga a página de login com o NavigationHandler.
		//Pegando  o Navigation Handler da aplicação:
		NavigationHandler navigationHandler = context.getApplication().getNavigationHandler();
		//Redirecionando, passando como parametros: O contexto, de onde eu venho(não importa), e para onde quero ir.
		navigationHandler.handleNavigation(context, null, "login?faces-redirect=true");
	
	}
	
	@Override
	public void beforePhase(PhaseEvent event) {
		
	}

	
	//Neste método definimos, através do retorno, em qual fase queremos que o beforePhase e afterPhase sejam executados.
	@Override
	public PhaseId getPhaseId() {
		return PhaseId.RESTORE_VIEW;
	}
	
	
	
}
