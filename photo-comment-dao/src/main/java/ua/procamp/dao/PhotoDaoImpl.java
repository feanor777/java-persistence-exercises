package ua.procamp.dao;

import ua.procamp.model.Photo;
import ua.procamp.model.PhotoComment;
import ua.procamp.util.EntityManagerUtil;

import javax.persistence.EntityManagerFactory;

import java.util.List;

/**
 * Please note that you should not use auto-commit mode for your implementation.
 */
public class PhotoDaoImpl implements PhotoDao {
  private EntityManagerFactory entityManagerFactory;
  private EntityManagerUtil entityManagerUtil;

  public PhotoDaoImpl(EntityManagerFactory entityManagerFactory) {
    this.entityManagerFactory = entityManagerFactory;
    entityManagerUtil = new EntityManagerUtil(this.entityManagerFactory);
  }

  @Override
  public void save(Photo photo) {
    entityManagerUtil.performWithinTx(entityManager -> entityManager.persist(photo));
  }

  @Override
  public Photo findById(long id) {
    return entityManagerUtil.performReturningWithinTx(entityManager -> entityManager.find(Photo.class, id));
  }

  @Override
  public List<Photo> findAll() {
    return entityManagerUtil.performReturningWithinTx(entityManager -> entityManager.createQuery("SELECT p FROM Photo p", Photo.class)
        .getResultList());
  }

  @Override
  public void remove(Photo photo) {
    entityManagerUtil.performWithinTx(entityManager -> entityManager.remove(entityManager.contains(photo) ? photo : entityManager.merge(photo)));
  }

  @Override
  public void addComment(long photoId, String comment) {
    entityManagerUtil.performWithinTx(entityManager -> {
      Photo photo = entityManager.find(Photo.class, photoId);
      PhotoComment photoComment = new PhotoComment();
      photoComment.setText(comment);
      photoComment.setPhoto(photo);
      entityManager.persist(photoComment);
    });
  }
}
