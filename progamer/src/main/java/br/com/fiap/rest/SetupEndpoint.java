package br.com.fiap.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.fiap.dao.SetupDAO;
import br.com.fiap.model.Setup;

//Esta é a classe que responde todo mundo que solicitar o caminho /setups

@Path("/setups")
@Produces(MediaType.APPLICATION_JSON)    //Como TODOS OS MÉTODOS irão converter o objeto criado e devolver um JSON, podemos colocar a anotação @Produces na própria classe, invés de ter que coloca-la em todos os métodos.
public class SetupEndpoint {
	
	private SetupDAO dao = new SetupDAO();
	
	
	@GET   //Será executado se for /setups com verbo http get
	public List<Setup> index() {
		return dao.getAll();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)       //Essa notação consume um json, pega um json que está no corpo da requisição e converte para objeto Setup(pois está como parâmetro do método).
	public Response create(Setup setup){       //Vamos deixar o create com retorno RESPONSE, para ele retornar para o client o objeto que foi criado (BOA PRÁTICA).
		if (setup == null) {
			//Se o setup for nulo = Se o usuário não mandar o Setup que ele quer cadastrar, invés de dar erro, devemos construir/buildar uma resposta JSON informando o erro com o código de status http (neste caso 400, bad request, requisição foi feita errada.).
			return Response.status(Response.Status.BAD_REQUEST).build();    
		}
		//Devemos fazer o tratamento de exceções para os métodos do banco de dados, pois se der algum erro com o banco de dados, não irá aparecer uma página HTML para o usuário, e sim um JSON com a mensagem de erro.
		try {
			dao.save(setup);
			//Porém, desta forma, ficara dois retornos com tipos diferentes, então padronizamos o retorno do método como Response, e retornamos o objeto/entidade setup também pela response, quando a requisição for bem sucedida (código 201 = objeto criado).
			return Response.status(Response.Status.CREATED).entity(setup).build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GET
	@Path("{id}")  //Quando buscamos um ID específico em um endpoint, estamos mudando a url, passando um PATH PARAMETER, então adicionamos a notação @Path para informar o novo Path. Como o ID pode mudar, passamos entre chaves, dando nome para o Path Parameter.
	public Response show(@PathParam("id") Long id) {    // Notação que indica que o parâmetro do método é o mesmo do Path
		
		if (id == null) {   //Se mandarmos não mandarmos um ID, ou mandarmos uma String no lugar do ID, o java nao irá fazer a conversão e irá deixar o id como NULL, então podemos tratar quando o id for null, pois sabemos que não foi enviado um número.
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		
		Setup setup = dao.findById(id);                             //Este método não dá erro, se não achar nada, retorna null com um código OK. 
		if (setup == null) {
			return Response.status(Response.Status.NOT_FOUND).build();    //Então invés de mandar um OK, é bom alterar o código para informar que o objeto não foi encontrado. 404.
		}
		//setup.setUser(null);
		return Response.status(Response.Status.OK).entity(setup).build();
		
	}
	
	
	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update( @PathParam("id") Long id, Setup setup) {
		
		//Validação se o usuário não passou o ID no link, ou o objeto setup no body.
		if (id == null || setup == null) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		
		//validação pra ver se existe esse ID no banco
		if (dao.findById(id) == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		
		//Jogando o ID que foi informado pra dentro do objeto JSON com os novos dados.
		setup.setId(id);
		
		//E agora atualizamos os dados no banco.
		try {
			dao.update(setup);
			return Response.status(Response.Status.OK).entity(setup).build();
		
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
		
	}
	
	@DELETE
	@Path("{id}")
	public Response destroy(@PathParam("id") Long id) {
		
		//Validação pra ver se o usuário passou o ID no Link
		if (id == null) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		
		//Validação pra ver se existe esse ID no banco
		if (dao.findById(id) == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		
		//Deletando do banco e retornando pro usuário
		try {
			dao.deleteById(id);
			return Response.status(Response.Status.OK).build();
			
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
		
	}
	
	
}
