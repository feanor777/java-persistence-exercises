package ua.procamp.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import ua.procamp.exception.CompanyDaoException;
import ua.procamp.model.Company;

public class CompanyDaoImpl implements CompanyDao {
  private EntityManagerFactory entityManagerFactory;

  public CompanyDaoImpl(EntityManagerFactory entityManagerFactory) {
    this.entityManagerFactory = entityManagerFactory;
  }

  @Override
  public Company findByIdFetchProducts(Long id) {

    EntityManager entityManager = entityManagerFactory.createEntityManager();
    Company company;

    try {
      entityManager.getTransaction().begin();
      company = entityManager.createQuery("SELECT c FROM Company c LEFT JOIN FETCH c.products p WHERE c.id = :id", Company.class)
          .setParameter("id", id)
          .getSingleResult();
      entityManager.getTransaction().commit();
    } catch (Exception e) {
      entityManager.getTransaction().rollback();
      throw new CompanyDaoException(e.getMessage(), e);
    } finally {
      entityManager.close();
    }

    return company;
  }
}
