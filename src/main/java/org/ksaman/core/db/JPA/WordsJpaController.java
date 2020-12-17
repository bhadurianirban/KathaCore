/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ksaman.core.db.JPA;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.ksaman.core.db.JPA.exceptions.NonexistentEntityException;
import org.ksaman.core.db.JPA.exceptions.PreexistingEntityException;
import org.ksaman.core.db.entities.Maintext;
import org.ksaman.core.db.entities.Words;
import org.ksaman.core.db.entities.WordsPK;

/**
 *
 * @author dgrfiv
 */
public class WordsJpaController implements Serializable {

    public WordsJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Words words) throws PreexistingEntityException, Exception {
        if (words.getWordsPK() == null) {
            words.setWordsPK(new WordsPK());
        }
        words.getWordsPK().setMaintextShlokanum(words.getMaintext().getMaintextPK().getShlokanum());
        words.getWordsPK().setMaintextShlokaline(words.getMaintext().getMaintextPK().getShlokaline());
        words.getWordsPK().setMaintextAdhyayid(words.getMaintext().getMaintextPK().getAdhyayid());
        words.getWordsPK().setMaintextParvaId(words.getMaintext().getMaintextPK().getParvaId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Maintext maintext = words.getMaintext();
            if (maintext != null) {
                maintext = em.getReference(maintext.getClass(), maintext.getMaintextPK());
                words.setMaintext(maintext);
            }
            em.persist(words);
            if (maintext != null) {
                maintext.getWordsList().add(words);
                maintext = em.merge(maintext);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findWords(words.getWordsPK()) != null) {
                throw new PreexistingEntityException("Words " + words + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Words words) throws NonexistentEntityException, Exception {
        words.getWordsPK().setMaintextShlokanum(words.getMaintext().getMaintextPK().getShlokanum());
        words.getWordsPK().setMaintextShlokaline(words.getMaintext().getMaintextPK().getShlokaline());
        words.getWordsPK().setMaintextAdhyayid(words.getMaintext().getMaintextPK().getAdhyayid());
        words.getWordsPK().setMaintextParvaId(words.getMaintext().getMaintextPK().getParvaId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Words persistentWords = em.find(Words.class, words.getWordsPK());
            Maintext maintextOld = persistentWords.getMaintext();
            Maintext maintextNew = words.getMaintext();
            if (maintextNew != null) {
                maintextNew = em.getReference(maintextNew.getClass(), maintextNew.getMaintextPK());
                words.setMaintext(maintextNew);
            }
            words = em.merge(words);
            if (maintextOld != null && !maintextOld.equals(maintextNew)) {
                maintextOld.getWordsList().remove(words);
                maintextOld = em.merge(maintextOld);
            }
            if (maintextNew != null && !maintextNew.equals(maintextOld)) {
                maintextNew.getWordsList().add(words);
                maintextNew = em.merge(maintextNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                WordsPK id = words.getWordsPK();
                if (findWords(id) == null) {
                    throw new NonexistentEntityException("The words with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(WordsPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Words words;
            try {
                words = em.getReference(Words.class, id);
                words.getWordsPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The words with id " + id + " no longer exists.", enfe);
            }
            Maintext maintext = words.getMaintext();
            if (maintext != null) {
                maintext.getWordsList().remove(words);
                maintext = em.merge(maintext);
            }
            em.remove(words);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Words> findWordsEntities() {
        return findWordsEntities(true, -1, -1);
    }

    public List<Words> findWordsEntities(int maxResults, int firstResult) {
        return findWordsEntities(false, maxResults, firstResult);
    }

    private List<Words> findWordsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Words.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Words findWords(WordsPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Words.class, id);
        } finally {
            em.close();
        }
    }

    public int getWordsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Words> rt = cq.from(Words.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
