package ua.procamp.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import ua.procamp.exception.AccountDaoException;
import ua.procamp.model.Account;

public class AccountDaoImpl implements AccountDao {
  private EntityManagerFactory emf;

  public AccountDaoImpl(EntityManagerFactory emf) {
    this.emf = emf;
  }

  @Override
  public void save(Account account) {
    EntityManager entityManager = emf.createEntityManager();
    entityManager.getTransaction().begin();
    try {
      entityManager.persist(account);
    } catch (Exception e) {
      throw new AccountDaoException(e.getMessage(), e);
    }
    entityManager.getTransaction().commit();
    entityManager.close();
  }

  @Override
  public Account findById(Long id) {
    EntityManager entityManager = emf.createEntityManager();
    entityManager.getTransaction().begin();
    Account account = entityManager.find(Account.class, id);
    entityManager.getTransaction().commit();
    entityManager.close();
    return account;
  }

  @Override
  public Account findByEmail(String email) {
    EntityManager entityManager = emf.createEntityManager();
    entityManager.getTransaction().begin();
    Account account = entityManager.createQuery("SELECT a FROM Account a WHERE a.email = :email", Account.class)
        .setParameter("email", email)
        .getSingleResult();
    entityManager.getTransaction().commit();
    entityManager.close();
    return account;
  }

  @Override
  public List<Account> findAll() {
    EntityManager entityManager = emf.createEntityManager();
    entityManager.getTransaction().begin();
    List<Account> accounts = entityManager.createQuery("SELECT a FROM Account a", Account.class)
        .getResultList();
    entityManager.getTransaction().commit();
    entityManager.close();
    return accounts;
  }

  @Override
  public void update(Account account) {
    EntityManager entityManager = emf.createEntityManager();
    entityManager.getTransaction().begin();
    try {
      entityManager.merge(account);
    } catch (Exception e) {
      entityManager.getTransaction().rollback();
      throw new AccountDaoException(e.getMessage(), e);
    }
    entityManager.getTransaction().commit();
    entityManager.close();
  }

  @Override
  public void remove(Account account) {
    EntityManager entityManager = emf.createEntityManager();
    entityManager.getTransaction().begin();
    Account accountPersisted = entityManager.find(Account.class, account.getId());
    entityManager.remove(accountPersisted);
    entityManager.getTransaction().commit();
    entityManager.close();
  }
}

