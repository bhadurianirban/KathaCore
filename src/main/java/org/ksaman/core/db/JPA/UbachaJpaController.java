/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ksaman.core.db.JPA;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.ksaman.core.db.entities.Maintext;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.ksaman.core.db.JPA.exceptions.IllegalOrphanException;
import org.ksaman.core.db.JPA.exceptions.NonexistentEntityException;
import org.ksaman.core.db.JPA.exceptions.PreexistingEntityException;
import org.ksaman.core.db.entities.Ubacha;

/**
 *
 * @author dgrfiv
 */
public class UbachaJpaController implements Serializable {

    public UbachaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Ubacha ubacha) throws PreexistingEntityException, Exception {
        if (ubacha.getMaintextList() == null) {
            ubacha.setMaintextList(new ArrayList<Maintext>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Maintext> attachedMaintextList = new ArrayList<Maintext>();
            for (Maintext maintextListMaintextToAttach : ubacha.getMaintextList()) {
                maintextListMaintextToAttach = em.getReference(maintextListMaintextToAttach.getClass(), maintextListMaintextToAttach.getMaintextPK());
                attachedMaintextList.add(maintextListMaintextToAttach);
            }
            ubacha.setMaintextList(attachedMaintextList);
            em.persist(ubacha);
            for (Maintext maintextListMaintext : ubacha.getMaintextList()) {
                Ubacha oldUbachaIdOfMaintextListMaintext = maintextListMaintext.getUbachaId();
                maintextListMaintext.setUbachaId(ubacha);
                maintextListMaintext = em.merge(maintextListMaintext);
                if (oldUbachaIdOfMaintextListMaintext != null) {
                    oldUbachaIdOfMaintextListMaintext.getMaintextList().remove(maintextListMaintext);
                    oldUbachaIdOfMaintextListMaintext = em.merge(oldUbachaIdOfMaintextListMaintext);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUbacha(ubacha.getId()) != null) {
                throw new PreexistingEntityException("Ubacha " + ubacha + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Ubacha ubacha) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ubacha persistentUbacha = em.find(Ubacha.class, ubacha.getId());
            List<Maintext> maintextListOld = persistentUbacha.getMaintextList();
            List<Maintext> maintextListNew = ubacha.getMaintextList();
            List<String> illegalOrphanMessages = null;
            for (Maintext maintextListOldMaintext : maintextListOld) {
                if (!maintextListNew.contains(maintextListOldMaintext)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Maintext " + maintextListOldMaintext + " since its ubachaId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Maintext> attachedMaintextListNew = new ArrayList<Maintext>();
            for (Maintext maintextListNewMaintextToAttach : maintextListNew) {
                maintextListNewMaintextToAttach = em.getReference(maintextListNewMaintextToAttach.getClass(), maintextListNewMaintextToAttach.getMaintextPK());
                attachedMaintextListNew.add(maintextListNewMaintextToAttach);
            }
            maintextListNew = attachedMaintextListNew;
            ubacha.setMaintextList(maintextListNew);
            ubacha = em.merge(ubacha);
            for (Maintext maintextListNewMaintext : maintextListNew) {
                if (!maintextListOld.contains(maintextListNewMaintext)) {
                    Ubacha oldUbachaIdOfMaintextListNewMaintext = maintextListNewMaintext.getUbachaId();
                    maintextListNewMaintext.setUbachaId(ubacha);
                    maintextListNewMaintext = em.merge(maintextListNewMaintext);
                    if (oldUbachaIdOfMaintextListNewMaintext != null && !oldUbachaIdOfMaintextListNewMaintext.equals(ubacha)) {
                        oldUbachaIdOfMaintextListNewMaintext.getMaintextList().remove(maintextListNewMaintext);
                        oldUbachaIdOfMaintextListNewMaintext = em.merge(oldUbachaIdOfMaintextListNewMaintext);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = ubacha.getId();
                if (findUbacha(id) == null) {
                    throw new NonexistentEntityException("The ubacha with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ubacha ubacha;
            try {
                ubacha = em.getReference(Ubacha.class, id);
                ubacha.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ubacha with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Maintext> maintextListOrphanCheck = ubacha.getMaintextList();
            for (Maintext maintextListOrphanCheckMaintext : maintextListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Ubacha (" + ubacha + ") cannot be destroyed since the Maintext " + maintextListOrphanCheckMaintext + " in its maintextList field has a non-nullable ubachaId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(ubacha);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Ubacha> findUbachaEntities() {
        return findUbachaEntities(true, -1, -1);
    }

    public List<Ubacha> findUbachaEntities(int maxResults, int firstResult) {
        return findUbachaEntities(false, maxResults, firstResult);
    }

    private List<Ubacha> findUbachaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Ubacha.class));
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

    public Ubacha findUbacha(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Ubacha.class, id);
        } finally {
            em.close();
        }
    }

    public int getUbachaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Ubacha> rt = cq.from(Ubacha.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
