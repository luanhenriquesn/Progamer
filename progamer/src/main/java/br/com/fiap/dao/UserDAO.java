package br.com.fiap.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import br.com.fiap.model.User;
import br.com.fiap.util.EntityManagerFacade;

public class UserDAO {
	
	private EntityManager manager = new EntityManagerFacade().getEntityManager();

	public void save(User user) {
		manager.getTransaction().begin();
		manager.persist(user);
		manager.getTransaction().commit();
		manager.clear();
	}
	
	public List<User> getAll(){
		String jpql = "SELECT u FROM User u";
		return manager.createQuery(jpql, User.class).getResultList();
	}
	
	public User findById(Long id) {
		return manager.find(User.class, id);
	}
	
	public void update(User user) {
		manager.getTransaction().begin();
		manager.merge(user);
		manager.flush();
		manager.getTransaction().commit();
	}
	
	public void deleteById(Long id) {
		manager.getTransaction().begin();
		
		User user = findById(id);
		
		manager.remove(user);
		manager.flush();
		manager.getTransaction().commit();
	}

	public User exist(User user) {
		String JPQL = "SELECT u FROM User u WHERE "
				+ "email=:email AND password=:password";
		TypedQuery<User> query = manager.createQuery(JPQL, User.class);
		query.setParameter("email", user.getEmail());
		query.setParameter("password", user.getPassword());
	
		//Se achar o usuário, retorna o próprio usuário (para pegarmos o ID e fazer a relação das tabelas) - Se não achar, retorna NULL.
		try {
			User result = query.getSingleResult();  //Se não retornar nada, dá exceção.
			return result;
		} catch (NoResultException e) {
			return null;
		}
		
	}

}
