package br.com.fiap.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.com.fiap.model.Setup;
import br.com.fiap.util.EntityManagerFacade;

public class SetupDAO {
	
	private EntityManager manager = new EntityManagerFacade().getEntityManager();
	
	public void save(Setup setup) {
		
		manager.getTransaction().begin();
		manager.persist(setup);
		manager.getTransaction().commit();
		manager.clear();
	}

	public List<Setup> getAll() {
		//USANDO JPQL
		String jpql = "SELECT s FROM Setup s";
		return manager.createQuery(jpql, Setup.class).getResultList();
	}

	public Setup findById(Long id) {
		return manager.find(Setup.class, id);
	}

	public void update(Setup setup) {
		manager.getTransaction().begin();
		manager.merge(setup);
		manager.flush();
		manager.getTransaction().commit();
	}
	
	public void deleteById(Long id) {
		manager.getTransaction().begin();
		
		Setup setup = findById(id);
		
		manager.remove(setup);
		manager.flush();
		manager.getTransaction().commit();
		
	}
	
	public List<Setup> getUserSetups(Long userId) {
		//USANDO JPQL
		String JPQL = "SELECT s FROM Setup s WHERE "
				+ "user_id=:userId";
		TypedQuery<Setup> query = manager.createQuery(JPQL, Setup.class);
		query.setParameter("userId", userId);
		
		return  query.getResultList();  
	}

}
