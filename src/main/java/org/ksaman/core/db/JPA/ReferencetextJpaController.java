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
import org.ksaman.core.db.entities.Referencetext;
import org.ksaman.core.db.entities.ReferencetextPK;

/**
 *
 * @author dgrfiv
 */
public class ReferencetextJpaController implements Serializable {

    public ReferencetextJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Referencetext referencetext) throws PreexistingEntityException, Exception {
        if (referencetext.getReferencetextPK() == null) {
            referencetext.setReferencetextPK(new ReferencetextPK());
        }
        referencetext.getReferencetextPK().setMaintextShlokaline(referencetext.getMaintext().getMaintextPK().getShlokaline());
        referencetext.getReferencetextPK().setMaintextShlokanum(referencetext.getMaintext().getMaintextPK().getShlokanum());
        referencetext.getReferencetextPK().setMaintextAdhyayid(referencetext.getMaintext().getMaintextPK().getAdhyayid());
        referencetext.getReferencetextPK().setMaintextParvaId(referencetext.getMaintext().getMaintextPK().getParvaId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Maintext maintext = referencetext.getMaintext();
            if (maintext != null) {
                maintext = em.getReference(maintext.getClass(), maintext.getMaintextPK());
                referencetext.setMaintext(maintext);
            }
            em.persist(referencetext);
            if (maintext != null) {
                maintext.getReferencetextList().add(referencetext);
                maintext = em.merge(maintext);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findReferencetext(referencetext.getReferencetextPK()) != null) {
                throw new PreexistingEntityException("Referencetext " + referencetext + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Referencetext referencetext) throws NonexistentEntityException, Exception {
        referencetext.getReferencetextPK().setMaintextShlokaline(referencetext.getMaintext().getMaintextPK().getShlokaline());
        referencetext.getReferencetextPK().setMaintextShlokanum(referencetext.getMaintext().getMaintextPK().getShlokanum());
        referencetext.getReferencetextPK().setMaintextAdhyayid(referencetext.getMaintext().getMaintextPK().getAdhyayid());
        referencetext.getReferencetextPK().setMaintextParvaId(referencetext.getMaintext().getMaintextPK().getParvaId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Referencetext persistentReferencetext = em.find(Referencetext.class, referencetext.getReferencetextPK());
            Maintext maintextOld = persistentReferencetext.getMaintext();
            Maintext maintextNew = referencetext.getMaintext();
            if (maintextNew != null) {
                maintextNew = em.getReference(maintextNew.getClass(), maintextNew.getMaintextPK());
                referencetext.setMaintext(maintextNew);
            }
            referencetext = em.merge(referencetext);
            if (maintextOld != null && !maintextOld.equals(maintextNew)) {
                maintextOld.getReferencetextList().remove(referencetext);
                maintextOld = em.merge(maintextOld);
            }
            if (maintextNew != null && !maintextNew.equals(maintextOld)) {
                maintextNew.getReferencetextList().add(referencetext);
                maintextNew = em.merge(maintextNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                ReferencetextPK id = referencetext.getReferencetextPK();
                if (findReferencetext(id) == null) {
                    throw new NonexistentEntityException("The referencetext with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(ReferencetextPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Referencetext referencetext;
            try {
                referencetext = em.getReference(Referencetext.class, id);
                referencetext.getReferencetextPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The referencetext with id " + id + " no longer exists.", enfe);
            }
            Maintext maintext = referencetext.getMaintext();
            if (maintext != null) {
                maintext.getReferencetextList().remove(referencetext);
                maintext = em.merge(maintext);
            }
            em.remove(referencetext);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Referencetext> findReferencetextEntities() {
        return findReferencetextEntities(true, -1, -1);
    }

    public List<Referencetext> findReferencetextEntities(int maxResults, int firstResult) {
        return findReferencetextEntities(false, maxResults, firstResult);
    }

    private List<Referencetext> findReferencetextEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Referencetext.class));
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

    public Referencetext findReferencetext(ReferencetextPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Referencetext.class, id);
        } finally {
            em.close();
        }
    }

    public int getReferencetextCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Referencetext> rt = cq.from(Referencetext.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
