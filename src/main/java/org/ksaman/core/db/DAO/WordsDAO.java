/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ksaman.core.db.DAO;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.ksaman.core.db.JPA.WordsJpaController;
import org.ksaman.core.db.entities.Maintext;
import org.ksaman.core.db.entities.MaintextPK;
import org.ksaman.core.db.entities.Parva;
import org.ksaman.core.db.entities.Ubacha;
import org.ksaman.core.db.entities.Words;
import org.ksaman.core.db.entities.WordsPK;

/**
 *
 * @author dgrfiv
 */
public class WordsDAO extends WordsJpaController {
    
    public WordsDAO(EntityManagerFactory emf) {
        super(emf);
    }
    
    public void deleteAllWordsAndChars(int parvaId, int adhyayId, int shlokaNum, int shlokaLine) {
        EntityManager em = getEntityManager();
        TypedQuery<Words> query = em.createNamedQuery("Words.deleteAllWordsAndChars", Words.class);
        query.setParameter("parvaId", parvaId);
        query.setParameter("adhyayId", adhyayId);
        query.setParameter("shlokaNum", shlokaNum);
        query.setParameter("shlokaLine", shlokaLine);
        
        em.getTransaction().begin();
        query.executeUpdate();
        em.getTransaction().commit();
        em.close();
    }
    
    public List<String> getUniqueShlokaWordsFirstCharList() {
        EntityManager em = getEntityManager();
        TypedQuery<String> query = em.createNamedQuery("Words.findAllDistinctFirstchar", String.class);
        List<String> firstCharList = query.getResultList();
        return firstCharList;
    }
    
    public Long getWordsCountByFirstChar(String firstChar) {
        EntityManager em = getEntityManager();
        TypedQuery<Long> query = em.createNamedQuery("Words.findWordsCountByFirstChar", Long.class);
        query.setParameter("firstChar", firstChar);
        Long wordsCount = query.getSingleResult();
        return wordsCount;
    }
    
    public List<Words> getWordsByFirstChar(String firstChar, int first, int pagesize) {
        
        EntityManager em = getEntityManager();
        String myQuery = "select \n"
                + "w.maintext_parva_id,\n"
                + "w.maintext_adhyayid,\n"
                + "w.maintext_shlokanum,\n"
                + "w.maintext_shlokaline,\n"
                + "w.wordtext,\n"
                + "w.firstchar,\n"
                + "u.id,\n"
                + "u.name,\n"
                + "u.bachan,\n"
                + "p.name,\n"
                + "m.shlokatext\n"
                + "from \n"
                + "words w,\n"
                + "maintext m,\n"
                + "ubacha u,\n"
                + "parva p\n"
                + "where w.firstchar = ?1 \n"
                + "and m.parva_id = w.maintext_parva_id \n"
                + "and m.adhyayid = w.maintext_adhyayid\n"
                + "and m.shlokanum = w.maintext_shlokanum\n"
                + "and m.shlokaline = w.maintext_shlokaline\n"
                + "and p.id = m.parva_id\n"
                + "and u.id = m.ubacha_id\n"
                + "order by wordtext limit ?2,?3";
        
        Query query = em.createNativeQuery(myQuery);
        //System.out.println(myQuery);
        query.setParameter(1, firstChar);
        query.setParameter(2, first);
        query.setParameter(3, pagesize);
        List<Object[]> wordTexts = query.getResultList();
        List<Words> wordsList = new ArrayList<>();
        
        for (Object[] wordText : wordTexts) {
            Words words = new Words();
            WordsPK wordsPK = new WordsPK();
            MaintextPK maintextPK = new MaintextPK();
            
            int parvaId = (Integer) wordText[0];
            int adhyayid = (Integer) wordText[1];
            int shlokanum = (Integer) wordText[2];
            int shlokaline = (Integer) wordText[3];
            String wordtext = (String) wordText[4];
            String firstchar = (String) wordText[5];
            int ubachaId = (Integer) wordText[6];
            String ubachaName = (String) wordText[7];
            String ubachaBachan = (String) wordText[8];
            String parvaName = (String) wordText[9];
            String shlokaText = (String) wordText[10];
            
            maintextPK.setParvaId(parvaId);
            maintextPK.setAdhyayid(adhyayid);
            maintextPK.setShlokanum(shlokanum);
            maintextPK.setShlokaline(shlokaline);
            
            Maintext maintext = new Maintext(maintextPK);
            Parva parva = new Parva(parvaId, parvaName);
            Ubacha ubacha = new Ubacha(ubachaId, ubachaName, ubachaBachan);
            
            wordsPK.setMaintextParvaId(parvaId);
            wordsPK.setMaintextAdhyayid(adhyayid);
            wordsPK.setMaintextShlokanum(shlokanum);
            wordsPK.setMaintextShlokaline(shlokaline);
            
            words.setWordsPK(wordsPK);
            words.setWordtext(wordtext);
            words.setFirstchar(firstchar);
            
            maintext.setUbachaId(ubacha);
            maintext.setParva(parva);
            maintext.setShlokatext(shlokaText);
            words.setMaintext(maintext);
            
            wordsList.add(words);
        }
        
        return wordsList;
    }
}
