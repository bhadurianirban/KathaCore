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
import org.ksaman.core.db.entities.Parva;

/**
 *
 * @author dgrfiv
 */
public class ParvaJpaController implements Serializable {

    public ParvaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Parva parva) throws PreexistingEntityException, Exception {
        if (parva.getMaintextList() == null) {
            parva.setMaintextList(new ArrayList<Maintext>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Maintext> attachedMaintextList = new ArrayList<Maintext>();
            for (Maintext maintextListMaintextToAttach : parva.getMaintextList()) {
                maintextListMaintextToAttach = em.getReference(maintextListMaintextToAttach.getClass(), maintextListMaintextToAttach.getMaintextPK());
                attachedMaintextList.add(maintextListMaintextToAttach);
            }
            parva.setMaintextList(attachedMaintextList);
            em.persist(parva);
            for (Maintext maintextListMaintext : parva.getMaintextList()) {
                Parva oldParvaOfMaintextListMaintext = maintextListMaintext.getParva();
                maintextListMaintext.setParva(parva);
                maintextListMaintext = em.merge(maintextListMaintext);
                if (oldParvaOfMaintextListMaintext != null) {
                    oldParvaOfMaintextListMaintext.getMaintextList().remove(maintextListMaintext);
                    oldParvaOfMaintextListMaintext = em.merge(oldParvaOfMaintextListMaintext);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findParva(parva.getId()) != null) {
                throw new PreexistingEntityException("Parva " + parva + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Parva parva) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Parva persistentParva = em.find(Parva.class, parva.getId());
            List<Maintext> maintextListOld = persistentParva.getMaintextList();
            List<Maintext> maintextListNew = parva.getMaintextList();
            List<String> illegalOrphanMessages = null;
            for (Maintext maintextListOldMaintext : maintextListOld) {
                if (!maintextListNew.contains(maintextListOldMaintext)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Maintext " + maintextListOldMaintext + " since its parva field is not nullable.");
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
            parva.setMaintextList(maintextListNew);
            parva = em.merge(parva);
            for (Maintext maintextListNewMaintext : maintextListNew) {
                if (!maintextListOld.contains(maintextListNewMaintext)) {
                    Parva oldParvaOfMaintextListNewMaintext = maintextListNewMaintext.getParva();
                    maintextListNewMaintext.setParva(parva);
                    maintextListNewMaintext = em.merge(maintextListNewMaintext);
                    if (oldParvaOfMaintextListNewMaintext != null && !oldParvaOfMaintextListNewMaintext.equals(parva)) {
                        oldParvaOfMaintextListNewMaintext.getMaintextList().remove(maintextListNewMaintext);
                        oldParvaOfMaintextListNewMaintext = em.merge(oldParvaOfMaintextListNewMaintext);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = parva.getId();
                if (findParva(id) == null) {
                    throw new NonexistentEntityException("The parva with id " + id + " no longer exists.");
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
            Parva parva;
            try {
                parva = em.getReference(Parva.class, id);
                parva.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The parva with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Maintext> maintextListOrphanCheck = parva.getMaintextList();
            for (Maintext maintextListOrphanCheckMaintext : maintextListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Parva (" + parva + ") cannot be destroyed since the Maintext " + maintextListOrphanCheckMaintext + " in its maintextList field has a non-nullable parva field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(parva);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Parva> findParvaEntities() {
        return findParvaEntities(true, -1, -1);
    }

    public List<Parva> findParvaEntities(int maxResults, int firstResult) {
        return findParvaEntities(false, maxResults, firstResult);
    }

    private List<Parva> findParvaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Parva.class));
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

    public Parva findParva(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Parva.class, id);
        } finally {
            em.close();
        }
    }

    public int getParvaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Parva> rt = cq.from(Parva.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
