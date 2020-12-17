/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ksaman.core.db.DAO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.ksaman.core.db.JPA.MaintextJpaController;
import org.ksaman.core.db.entities.Maintext;
import org.ksaman.core.db.entities.MaintextPK;
import org.ksaman.core.db.entities.Parva;
import org.ksaman.core.db.entities.Ubacha;

/**
 *
 * @author dgrfiv
 */
public class MaintextDAO extends MaintextJpaController {
    
    public MaintextDAO(EntityManagerFactory emf) {
        super(emf);
    }
    
    public List<Integer> getAdhyayByParvaId(int parvaId) {
        EntityManager em = getEntityManager();
        TypedQuery<Integer> query = em.createNamedQuery("Maintext.getAdhyayByParvaId", Integer.class);
        query.setParameter("parvaId", parvaId);
        List<Integer> adhyayByParva = query.getResultList();
        return adhyayByParva;
    }
    
    public List<Maintext> getShlokaByParvaAndAdhyayId(int parvaId, int adhyayId) {
        EntityManager em = getEntityManager();
        TypedQuery<Maintext> query = em.createNamedQuery("Maintext.findShlokByParvaAndAdhyayId", Maintext.class);
        query.setParameter("parvaId", parvaId);
        query.setParameter("adhyayId", adhyayId);
        List<Maintext> shlokaList = query.getResultList();
        return shlokaList;
    }
    
    public Integer getMaxShlokaNum(int parvaId, int adhyayId) {
        EntityManager em = getEntityManager();
        TypedQuery<Integer> query = em.createNamedQuery("Maintext.findMaxShlokaNum", Integer.class);
        query.setParameter("parvaId", parvaId);
        query.setParameter("adhyayId", adhyayId);
        Integer maxShlokaNum = query.getSingleResult();
        return maxShlokaNum;
    }
    
    public Integer getMaxShlokaLine(int parvaId, int adhyayId, int shlokaNum) {
        EntityManager em = getEntityManager();
        TypedQuery<Integer> query = em.createNamedQuery("Maintext.findMaxShlokaLine", Integer.class);
        query.setParameter("parvaId", parvaId);
        query.setParameter("adhyayId", adhyayId);
        query.setParameter("shlokaNum", shlokaNum);
        Integer maxShlokaLine = query.getSingleResult();
        return maxShlokaLine;
    }
    
    public List<Maintext> getShlokaTranslation(int parvaId, int adhyayId, int shlokaNum) {
        EntityManager em = getEntityManager();
        TypedQuery<Maintext> query = em.createNamedQuery("Maintext.findShlokTranslation", Maintext.class);
        query.setParameter("parvaId", parvaId);
        query.setParameter("adhyayId", adhyayId);
        query.setParameter("shlokaNum", shlokaNum);
        List<Maintext> shlokaList = query.getResultList();
        return shlokaList;
    }
    
    public List<String> getUniqueShlokaFirstCharList() {
        EntityManager em = getEntityManager();
        TypedQuery<String> query = em.createNamedQuery("Maintext.findShlokaFirstChar", String.class);
        List<String> firstCharList = query.getResultList();
        return firstCharList;
    }
    
//    public List<Maintext> getShlokaByFirstChar(String firstChar) {
//        EntityManager em = getEntityManager();
//        TypedQuery<Maintext> query = em.createNamedQuery("Maintext.findShlokaByFirstChar", Maintext.class);
//        query.setParameter("firstChar", firstChar);
////        query.setParameter("first", first);
////        query.setParameter("pagesize", pagesize);
//        List<Maintext> shlokaList = query.getResultList();
//        return shlokaList;
//    }
    public List<Maintext> getShlokaByFirstChar(String firstChar,int first,int pagesize) {
        
        EntityManager em = getEntityManager();
        String myQuery = 
                "select m.parva_id,"
                + "m.adhyayid,"
                + "m.shlokanum,"
                + "m.shlokaline,"
                + "m.ubacha_id,"
                + "m.shlokatext,"
                + "m.firstchar,"
                + "m.endchar,"
                + "m.translatedtext, "
                + "m.lastupdatedts, "
                + "p.name, "
                + "u.name, "
                + "u.bachan "
                + "from maintext m,"
                + " parva p,"
                + " ubacha u"
                + " where firstchar = ?1 "
                + " and p.id = m.parva_id"
                + " and u.id = m.ubacha_id"
                + " order by shlokatext limit ?2,?3";
        Query query = em.createNativeQuery(myQuery);
        //System.out.println(myQuery);
        query.setParameter(1, firstChar);
        query.setParameter(2, first);
        query.setParameter(3, pagesize);
        List<Object[]> shlokaLines = query.getResultList();
        List<Maintext> maintextList = new ArrayList<>();
        for (Object[] shlokaLine:shlokaLines ) {
            Maintext maintext = new Maintext();
            MaintextPK maintextPK = new MaintextPK();
            
            int parvaId = (Integer)shlokaLine[0];
            int adhyayid = (Integer)shlokaLine[1];
            int shlokanum = (Integer)shlokaLine[2];
            int shlokaline = (Integer)shlokaLine[3];
            int ubachaId = (Integer)shlokaLine[4];
            String shlokatext = (String)shlokaLine[5];
            String firstchar = (String)shlokaLine[6];
            String endchar = (String)shlokaLine[7];
            String translatedtext = (String)shlokaLine[8];
            Date lastupdatedts = (Date)shlokaLine[9];
            String parvaName = (String)shlokaLine[10];
            String ubachaName = (String)shlokaLine[11];
            String ubachaBachan = (String)shlokaLine[12];
            
            Ubacha ubacha = new Ubacha(ubachaId);
            ubacha.setName(ubachaName);
            ubacha.setBachan(ubachaBachan);
            
            Parva parva = new Parva(parvaId);
            parva.setName(parvaName);
            
            maintextPK.setParvaId(parvaId);
            maintextPK.setAdhyayid(adhyayid);
            maintextPK.setShlokanum(shlokanum);
            maintextPK.setShlokaline(shlokaline);
            maintext.setMaintextPK(maintextPK);
            maintext.setParva(parva);
            maintext.setUbachaId(ubacha);
            maintext.setFirstchar(firstchar);
            maintext.setEndchar(endchar);
            maintext.setShlokatext(shlokatext);
            maintext.setLastupdatedts(lastupdatedts);
            maintext.setTranslatedtext(translatedtext);
            
            maintextList.add(maintext);
        }
        
        return maintextList;
    }
    
    public Long getShlokaCountByFirstChar(String firstChar) {
        EntityManager em = getEntityManager();
        TypedQuery<Long> query = em.createNamedQuery("Maintext.findShlokaCountByFirstChar", Long.class);
        query.setParameter("firstChar", firstChar);
        Long shlokaCount = query.getSingleResult();
        return shlokaCount;
    }
    
    public Long getTranslatedShlokaCount() {
        EntityManager em = getEntityManager();
        TypedQuery<Long> query = em.createNamedQuery("Maintext.countTranslatedShloka", Long.class);
        Long shlokaCount = query.getSingleResult();
        return shlokaCount;
    }
    
    public Long getNotTranslatedShlokaCount() {
        EntityManager em = getEntityManager();
        TypedQuery<Long> query = em.createNamedQuery("Maintext.countNotTranslatedShloka", Long.class);
        Long shlokaCount = query.getSingleResult();
        return shlokaCount;
    }
}
