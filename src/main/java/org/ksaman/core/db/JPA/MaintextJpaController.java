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
import org.ksaman.core.db.entities.Parva;
import org.ksaman.core.db.entities.Ubacha;
import org.ksaman.core.db.entities.Referencetext;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.ksaman.core.db.JPA.exceptions.IllegalOrphanException;
import org.ksaman.core.db.JPA.exceptions.NonexistentEntityException;
import org.ksaman.core.db.JPA.exceptions.PreexistingEntityException;
import org.ksaman.core.db.entities.Maintext;
import org.ksaman.core.db.entities.MaintextPK;
import org.ksaman.core.db.entities.Words;

/**
 *
 * @author dgrfiv
 */
public class MaintextJpaController implements Serializable {

    public MaintextJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Maintext maintext) throws PreexistingEntityException, Exception {
        if (maintext.getMaintextPK() == null) {
            maintext.setMaintextPK(new MaintextPK());
        }
        if (maintext.getReferencetextList() == null) {
            maintext.setReferencetextList(new ArrayList<Referencetext>());
        }
        if (maintext.getWordsList() == null) {
            maintext.setWordsList(new ArrayList<Words>());
        }
        maintext.getMaintextPK().setParvaId(maintext.getParva().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Parva parva = maintext.getParva();
            if (parva != null) {
                parva = em.getReference(parva.getClass(), parva.getId());
                maintext.setParva(parva);
            }
            Ubacha ubachaId = maintext.getUbachaId();
            if (ubachaId != null) {
                ubachaId = em.getReference(ubachaId.getClass(), ubachaId.getId());
                maintext.setUbachaId(ubachaId);
            }
            List<Referencetext> attachedReferencetextList = new ArrayList<Referencetext>();
            for (Referencetext referencetextListReferencetextToAttach : maintext.getReferencetextList()) {
                referencetextListReferencetextToAttach = em.getReference(referencetextListReferencetextToAttach.getClass(), referencetextListReferencetextToAttach.getReferencetextPK());
                attachedReferencetextList.add(referencetextListReferencetextToAttach);
            }
            maintext.setReferencetextList(attachedReferencetextList);
            List<Words> attachedWordsList = new ArrayList<Words>();
            for (Words wordsListWordsToAttach : maintext.getWordsList()) {
                wordsListWordsToAttach = em.getReference(wordsListWordsToAttach.getClass(), wordsListWordsToAttach.getWordsPK());
                attachedWordsList.add(wordsListWordsToAttach);
            }
            maintext.setWordsList(attachedWordsList);
            em.persist(maintext);
            if (parva != null) {
                parva.getMaintextList().add(maintext);
                parva = em.merge(parva);
            }
            if (ubachaId != null) {
                ubachaId.getMaintextList().add(maintext);
                ubachaId = em.merge(ubachaId);
            }
            for (Referencetext referencetextListReferencetext : maintext.getReferencetextList()) {
                Maintext oldMaintextOfReferencetextListReferencetext = referencetextListReferencetext.getMaintext();
                referencetextListReferencetext.setMaintext(maintext);
                referencetextListReferencetext = em.merge(referencetextListReferencetext);
                if (oldMaintextOfReferencetextListReferencetext != null) {
                    oldMaintextOfReferencetextListReferencetext.getReferencetextList().remove(referencetextListReferencetext);
                    oldMaintextOfReferencetextListReferencetext = em.merge(oldMaintextOfReferencetextListReferencetext);
                }
            }
            for (Words wordsListWords : maintext.getWordsList()) {
                Maintext oldMaintextOfWordsListWords = wordsListWords.getMaintext();
                wordsListWords.setMaintext(maintext);
                wordsListWords = em.merge(wordsListWords);
                if (oldMaintextOfWordsListWords != null) {
                    oldMaintextOfWordsListWords.getWordsList().remove(wordsListWords);
                    oldMaintextOfWordsListWords = em.merge(oldMaintextOfWordsListWords);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMaintext(maintext.getMaintextPK()) != null) {
                throw new PreexistingEntityException("Maintext " + maintext + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Maintext maintext) throws IllegalOrphanException, NonexistentEntityException, Exception {
        maintext.getMaintextPK().setParvaId(maintext.getParva().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Maintext persistentMaintext = em.find(Maintext.class, maintext.getMaintextPK());
            Parva parvaOld = persistentMaintext.getParva();
            Parva parvaNew = maintext.getParva();
            Ubacha ubachaIdOld = persistentMaintext.getUbachaId();
            Ubacha ubachaIdNew = maintext.getUbachaId();
            List<Referencetext> referencetextListOld = persistentMaintext.getReferencetextList();
            List<Referencetext> referencetextListNew = maintext.getReferencetextList();
            List<Words> wordsListOld = persistentMaintext.getWordsList();
            List<Words> wordsListNew = maintext.getWordsList();
            List<String> illegalOrphanMessages = null;
            for (Referencetext referencetextListOldReferencetext : referencetextListOld) {
                if (!referencetextListNew.contains(referencetextListOldReferencetext)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Referencetext " + referencetextListOldReferencetext + " since its maintext field is not nullable.");
                }
            }
            for (Words wordsListOldWords : wordsListOld) {
                if (!wordsListNew.contains(wordsListOldWords)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Words " + wordsListOldWords + " since its maintext field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (parvaNew != null) {
                parvaNew = em.getReference(parvaNew.getClass(), parvaNew.getId());
                maintext.setParva(parvaNew);
            }
            if (ubachaIdNew != null) {
                ubachaIdNew = em.getReference(ubachaIdNew.getClass(), ubachaIdNew.getId());
                maintext.setUbachaId(ubachaIdNew);
            }
            List<Referencetext> attachedReferencetextListNew = new ArrayList<Referencetext>();
            for (Referencetext referencetextListNewReferencetextToAttach : referencetextListNew) {
                referencetextListNewReferencetextToAttach = em.getReference(referencetextListNewReferencetextToAttach.getClass(), referencetextListNewReferencetextToAttach.getReferencetextPK());
                attachedReferencetextListNew.add(referencetextListNewReferencetextToAttach);
            }
            referencetextListNew = attachedReferencetextListNew;
            maintext.setReferencetextList(referencetextListNew);
            List<Words> attachedWordsListNew = new ArrayList<Words>();
            for (Words wordsListNewWordsToAttach : wordsListNew) {
                wordsListNewWordsToAttach = em.getReference(wordsListNewWordsToAttach.getClass(), wordsListNewWordsToAttach.getWordsPK());
                attachedWordsListNew.add(wordsListNewWordsToAttach);
            }
            wordsListNew = attachedWordsListNew;
            maintext.setWordsList(wordsListNew);
            maintext = em.merge(maintext);
            if (parvaOld != null && !parvaOld.equals(parvaNew)) {
                parvaOld.getMaintextList().remove(maintext);
                parvaOld = em.merge(parvaOld);
            }
            if (parvaNew != null && !parvaNew.equals(parvaOld)) {
                parvaNew.getMaintextList().add(maintext);
                parvaNew = em.merge(parvaNew);
            }
            if (ubachaIdOld != null && !ubachaIdOld.equals(ubachaIdNew)) {
                ubachaIdOld.getMaintextList().remove(maintext);
                ubachaIdOld = em.merge(ubachaIdOld);
            }
            if (ubachaIdNew != null && !ubachaIdNew.equals(ubachaIdOld)) {
                ubachaIdNew.getMaintextList().add(maintext);
                ubachaIdNew = em.merge(ubachaIdNew);
            }
            for (Referencetext referencetextListNewReferencetext : referencetextListNew) {
                if (!referencetextListOld.contains(referencetextListNewReferencetext)) {
                    Maintext oldMaintextOfReferencetextListNewReferencetext = referencetextListNewReferencetext.getMaintext();
                    referencetextListNewReferencetext.setMaintext(maintext);
                    referencetextListNewReferencetext = em.merge(referencetextListNewReferencetext);
                    if (oldMaintextOfReferencetextListNewReferencetext != null && !oldMaintextOfReferencetextListNewReferencetext.equals(maintext)) {
                        oldMaintextOfReferencetextListNewReferencetext.getReferencetextList().remove(referencetextListNewReferencetext);
                        oldMaintextOfReferencetextListNewReferencetext = em.merge(oldMaintextOfReferencetextListNewReferencetext);
                    }
                }
            }
            for (Words wordsListNewWords : wordsListNew) {
                if (!wordsListOld.contains(wordsListNewWords)) {
                    Maintext oldMaintextOfWordsListNewWords = wordsListNewWords.getMaintext();
                    wordsListNewWords.setMaintext(maintext);
                    wordsListNewWords = em.merge(wordsListNewWords);
                    if (oldMaintextOfWordsListNewWords != null && !oldMaintextOfWordsListNewWords.equals(maintext)) {
                        oldMaintextOfWordsListNewWords.getWordsList().remove(wordsListNewWords);
                        oldMaintextOfWordsListNewWords = em.merge(oldMaintextOfWordsListNewWords);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                MaintextPK id = maintext.getMaintextPK();
                if (findMaintext(id) == null) {
                    throw new NonexistentEntityException("The maintext with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(MaintextPK id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Maintext maintext;
            try {
                maintext = em.getReference(Maintext.class, id);
                maintext.getMaintextPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The maintext with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Referencetext> referencetextListOrphanCheck = maintext.getReferencetextList();
            for (Referencetext referencetextListOrphanCheckReferencetext : referencetextListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Maintext (" + maintext + ") cannot be destroyed since the Referencetext " + referencetextListOrphanCheckReferencetext + " in its referencetextList field has a non-nullable maintext field.");
            }
            List<Words> wordsListOrphanCheck = maintext.getWordsList();
            for (Words wordsListOrphanCheckWords : wordsListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Maintext (" + maintext + ") cannot be destroyed since the Words " + wordsListOrphanCheckWords + " in its wordsList field has a non-nullable maintext field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Parva parva = maintext.getParva();
            if (parva != null) {
                parva.getMaintextList().remove(maintext);
                parva = em.merge(parva);
            }
            Ubacha ubachaId = maintext.getUbachaId();
            if (ubachaId != null) {
                ubachaId.getMaintextList().remove(maintext);
                ubachaId = em.merge(ubachaId);
            }
            em.remove(maintext);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Maintext> findMaintextEntities() {
        return findMaintextEntities(true, -1, -1);
    }

    public List<Maintext> findMaintextEntities(int maxResults, int firstResult) {
        return findMaintextEntities(false, maxResults, firstResult);
    }

    private List<Maintext> findMaintextEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Maintext.class));
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

    public Maintext findMaintext(MaintextPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Maintext.class, id);
        } finally {
            em.close();
        }
    }

    public int getMaintextCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Maintext> rt = cq.from(Maintext.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
