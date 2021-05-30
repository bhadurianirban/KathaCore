/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ksaman.core.bl.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hedwig.cloud.response.HedwigResponseCode;
import org.ksaman.core.DTO.MaintextDTO;
import org.ksaman.core.DTO.ParvaDTO;
import org.ksaman.core.DTO.TikaDTO;
import org.ksaman.core.DTO.UbachaDTO;
import org.ksaman.core.DTO.WordsDTO;
import org.ksaman.core.db.DAO.MaintextDAO;
import org.ksaman.core.db.DAO.ParvaDAO;
import org.ksaman.core.db.DAO.ReferencetextDAO;
import org.ksaman.core.db.DAO.UbachaDAO;
import org.ksaman.core.db.DAO.WordsDAO;
import org.ksaman.core.db.JPA.exceptions.IllegalOrphanException;
import org.ksaman.core.db.JPA.exceptions.NonexistentEntityException;
import org.ksaman.core.db.JPA.exceptions.PreexistingEntityException;
import org.ksaman.core.db.entities.Maintext;
import org.ksaman.core.db.entities.MaintextPK;
import org.ksaman.core.db.entities.Parva;
import org.ksaman.core.db.entities.Referencetext;
import org.ksaman.core.db.entities.ReferencetextPK;
import org.ksaman.core.db.entities.Ubacha;
import org.ksaman.core.db.entities.Words;
import org.ksaman.core.db.entities.WordsPK;

/**
 *
 * @author bhaduri
 */
public class KSCoreService {

//    public void getAllParva() {
//        ParvaDAO parvaDAO = new ParvaDAO(DatabaseConnection.EMF);
//        List<Parva> parvaList = parvaDAO.findParvaEntities();
//        parvaList.stream().forEach(x->System.out.println(x.getName()));
//    }
    public ParvaDTO getParvaDTO(ParvaDTO parvaDTO) {
        ParvaDAO parvaDAO = new ParvaDAO(DatabaseConnection.EMF);
        Parva parva = parvaDAO.findParva(parvaDTO.getParvaId());

        parvaDTO.setParvaId(parva.getId());
        parvaDTO.setParvaName(parva.getName());

        return parvaDTO;
    }

    public UbachaDTO getUbachaDTO(UbachaDTO ubachaDTO) {
        UbachaDAO ubachaDAO = new UbachaDAO(DatabaseConnection.EMF);
        Ubacha ubacha = ubachaDAO.findUbacha(ubachaDTO.getUbachaId());

        ubachaDTO.setUbachaId(ubacha.getId());
        ubachaDTO.setUbachaName(ubacha.getName());
        ubachaDTO.setUbachaBachan(ubacha.getBachan());

        return ubachaDTO;
    }

    public WordsPK getWordsPK(WordsDTO wordsDTO) {
        WordsPK wordsPK = new WordsPK();

        wordsPK.setMaintextParvaId(wordsDTO.getParvaId());
        wordsPK.setMaintextAdhyayid(wordsDTO.getAdhyayId());
        wordsPK.setMaintextShlokanum(wordsDTO.getShlokaNum());
        wordsPK.setMaintextShlokaline(wordsDTO.getShlokaLine());
        wordsPK.setWordnum(wordsDTO.getWordNum());

        return wordsPK;
    }

    public WordsDTO getWordsDTO(WordsDTO wordsDTO) {
        WordsDAO wordsDAO = new WordsDAO(DatabaseConnection.EMF);
        WordsPK wordsPK = getWordsPK(wordsDTO);

        Words words = wordsDAO.findWords(wordsPK);

        wordsDTO.setWordText(words.getWordtext());
        wordsDTO.setWordFirstChar(words.getFirstchar());

        return wordsDTO;
    }
    
    public ReferencetextPK prepareReftextPK(TikaDTO tikaDTO) {
        ReferencetextPK referencetextPK = new ReferencetextPK();
        
        referencetextPK.setMaintextParvaId(tikaDTO.getParvaId());
        referencetextPK.setMaintextAdhyayid(tikaDTO.getAdhyayId());
        referencetextPK.setMaintextShlokanum(tikaDTO.getShlokaNum());
        referencetextPK.setMaintextShlokaline(tikaDTO.getShlokaLine());
        referencetextPK.setReftextid(tikaDTO.getRefTextId());
        
        return referencetextPK;
    }
    
    public TikaDTO getTikaDetails(TikaDTO tikaDTO) {
        ReferencetextDAO referencetextDAO = new ReferencetextDAO(DatabaseConnection.EMF);
        ReferencetextPK referencetextPK = prepareReftextPK(tikaDTO);
        
        Referencetext referencetext = referencetextDAO.findReferencetext(referencetextPK);
        
        tikaDTO.setRefText(referencetext.getText());
        //referencetextDTO.setRefTextPosition(referencetext.getReferencetextpos());
        
        return tikaDTO;
    }

    public MaintextPK getMaintextPK(MaintextDTO maintextDTO) {
        MaintextPK maintextPK = new MaintextPK();

        maintextPK.setParvaId(maintextDTO.getParvaId());
        maintextPK.setAdhyayid(maintextDTO.getAdhyayId());
        maintextPK.setShlokanum(maintextDTO.getShlokaNum());
        maintextPK.setShlokaline(maintextDTO.getShlokaLine());

        return maintextPK;
    }

    public MaintextDTO getMaintextDTO(MaintextDTO maintextDTO) {

        MaintextDAO maintextDAO = new MaintextDAO(DatabaseConnection.EMF);
        MaintextPK maintextPK = getMaintextPK(maintextDTO);

        Maintext maintext = maintextDAO.findMaintext(maintextPK);

        maintextDTO.setShlokaText(maintext.getShlokatext());
        maintextDTO.setFirstChar(maintext.getFirstchar());
        maintextDTO.setEndChar(maintext.getEndchar());
        maintextDTO.setAnubadText(maintext.getTranslatedtext());
        maintextDTO.setUbachaId(maintext.getUbachaId().getId());
        maintextDTO.setUbachaName(maintext.getUbachaId().getName());
        maintextDTO.setUbachaBachan(maintext.getUbachaId().getBachan());

        return maintextDTO;
    }

    public List<ParvaDTO> getParvaDTOList() {
        ParvaDAO parvaDAO = new ParvaDAO(DatabaseConnection.EMF);
        List<Parva> parvaList = parvaDAO.findParvaEntities();

        List<ParvaDTO> parvaDTOList = new ArrayList<>();

        for (int i = 0; i < parvaList.size(); i++) {
            ParvaDTO parvaDTO = new ParvaDTO();

            parvaDTO.setParvaId(parvaList.get(i).getId());
            parvaDTO.setParvaName(parvaList.get(i).getName());

            parvaDTOList.add(parvaDTO);
        }
        return parvaDTOList;
    }

    public List<UbachaDTO> getUbachaDTOList() {
        UbachaDAO ubachaDAO = new UbachaDAO(DatabaseConnection.EMF);
        List<Ubacha> ubachaList = ubachaDAO.findUbachaEntities();

        List<UbachaDTO> ubachaDTOList = new ArrayList<>();

        for (int i = 0; i < ubachaList.size(); i++) {
            UbachaDTO ubachaDTO = new UbachaDTO();

            ubachaDTO.setUbachaId(ubachaList.get(i).getId());
            ubachaDTO.setUbachaName(ubachaList.get(i).getName());
            ubachaDTO.setUbachaBachan(ubachaList.get(i).getBachan());

            ubachaDTOList.add(ubachaDTO);
        }
        return ubachaDTOList;
    }

    public List<WordsDTO> getWordsDTOList() {
        WordsDAO wordsDAO = new WordsDAO(DatabaseConnection.EMF);
        List<Words> wordsList = wordsDAO.findWordsEntities();

        List<WordsDTO> wordsDTOList = new ArrayList<>();

        for (int i = 0; i < wordsList.size(); i++) {
            WordsDTO wordsDTO = new WordsDTO();

            wordsDTO.setAdhyayId(wordsList.get(i).getWordsPK().getMaintextAdhyayid());
            wordsDTO.setParvaId(wordsList.get(i).getWordsPK().getMaintextParvaId());
            wordsDTO.setShlokaNum(wordsList.get(i).getWordsPK().getMaintextShlokanum());
            wordsDTO.setShlokaLine(wordsList.get(i).getWordsPK().getMaintextShlokaline());
            wordsDTO.setWordNum(wordsList.get(i).getWordsPK().getWordnum());
            wordsDTO.setWordText(wordsList.get(i).getWordtext());
            wordsDTO.setWordFirstChar(wordsList.get(i).getFirstchar());

            wordsDTOList.add(wordsDTO);
        }
        return wordsDTOList;
    }
    
    public List<TikaDTO> getReferencetextDTOList() {
        ReferencetextDAO referencetextDAO = new ReferencetextDAO(DatabaseConnection.EMF);
        List<Referencetext> referencetextList = referencetextDAO.findReferencetextEntities();
        
        List<TikaDTO> referencetextDTOList = new ArrayList<>();
        
        for (int i = 0; i < referencetextList.size(); i++) {
            TikaDTO referencetextDTO = new TikaDTO();
            
            referencetextDTO.setRefText(referencetextList.get(i).getText());
            
            referencetextDTOList.add(referencetextDTO);
        }
        return referencetextDTOList;
    }
    
    

    public List<MaintextDTO> getMaintextDTOList() {
        MaintextDAO maintextDAO = new MaintextDAO(DatabaseConnection.EMF);
        List<Maintext> maintextList = maintextDAO.findMaintextEntities();

        List<MaintextDTO> maintextDTOList = new ArrayList<>();

        for (int i = 0; i < maintextList.size(); i++) {
            MaintextDTO maintextDTO = new MaintextDTO();

            maintextDTO.setAdhyayId(maintextList.get(i).getMaintextPK().getAdhyayid());
            maintextDTO.setParvaId(maintextList.get(i).getMaintextPK().getParvaId());
            maintextDTO.setShlokaNum(maintextList.get(i).getMaintextPK().getShlokanum());
            maintextDTO.setShlokaLine(maintextList.get(i).getMaintextPK().getShlokaline());
            maintextDTO.setUbachaId(maintextList.get(i).getUbachaId().getId());
            maintextDTO.setShlokaText(maintextList.get(i).getShlokatext());
            maintextDTO.setAnubadText(maintextList.get(i).getTranslatedtext());
            maintextDTO.setFirstChar(maintextList.get(i).getFirstchar());
            maintextDTO.setEndChar(maintextList.get(i).getEndchar());
            maintextDTO.setLastEditTS(maintextList.get(i).getLastupdatedts());

            maintextDTOList.add(maintextDTO);
        }
        return maintextDTOList;
    }

    //////////////////// PARVA CRUD ////////////////////
    public int addParva(ParvaDTO parvaDTO) {
        int responseCode;

        ParvaDAO parvaDAO = new ParvaDAO(DatabaseConnection.EMF);
        Parva parva = new Parva();

        parva.setId(parvaDTO.getParvaId());
        parva.setName(parvaDTO.getParvaName());

        try {
            parvaDAO.create(parva);
            responseCode = HedwigResponseCode.SUCCESS;

        } catch (PreexistingEntityException ex) {
            Logger.getLogger(KSCoreService.class.getName()).log(Level.SEVERE, null, ex);
            responseCode = HedwigResponseCode.DB_DUPLICATE;

        } catch (Exception ex) {
            Logger.getLogger(KSCoreService.class.getName()).log(Level.SEVERE, null, ex);
            responseCode = HedwigResponseCode.DB_SEVERE;
        }
        return responseCode;
    }

    public int updateParva(ParvaDTO parvaDTO) {
        int responseCode;

        ParvaDAO parvaDAO = new ParvaDAO(DatabaseConnection.EMF);
        Parva parva = parvaDAO.findParva(parvaDTO.getParvaId());

        parva.setName(parvaDTO.getParvaName());

        try {
            parvaDAO.edit(parva);
            responseCode = HedwigResponseCode.SUCCESS;
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(KSCoreService.class.getName()).log(Level.SEVERE, null, ex);
            responseCode = HedwigResponseCode.DB_NON_EXISTING;
        } catch (Exception ex) {
            Logger.getLogger(KSCoreService.class.getName()).log(Level.SEVERE, null, ex);
            responseCode = HedwigResponseCode.DB_SEVERE;
        }
        return responseCode;
    }

    public int removeParva(ParvaDTO parvaDTO) {
        int responseCode;

        ParvaDAO parvaDAO = new ParvaDAO(DatabaseConnection.EMF);
        Parva parva = parvaDAO.findParva(parvaDTO.getParvaId());

        try {
            parvaDAO.destroy(parva.getId());
            responseCode = HedwigResponseCode.SUCCESS;

        } catch (IllegalOrphanException ex) {
            Logger.getLogger(KSCoreService.class.getName()).log(Level.SEVERE, null, ex);
            responseCode = HedwigResponseCode.DB_ILLEGAL_ORPHAN;

        } catch (NonexistentEntityException ex) {
            Logger.getLogger(KSCoreService.class.getName()).log(Level.SEVERE, null, ex);
            responseCode = HedwigResponseCode.DB_NON_EXISTING;
        }
        return responseCode;
    }

    //////////////////// UBACHA CRUD ////////////////////
    public int addUbacha(UbachaDTO ubachaDTO) {
        int responseCode;

        UbachaDAO ubachaDAO = new UbachaDAO(DatabaseConnection.EMF);
        Ubacha ubacha = new Ubacha();

        ubacha.setId(ubachaDTO.getUbachaId());
        ubacha.setName(ubachaDTO.getUbachaName());
        ubacha.setBachan(ubachaDTO.getUbachaBachan());

        try {
            ubachaDAO.create(ubacha);
            responseCode = HedwigResponseCode.SUCCESS;

        } catch (PreexistingEntityException ex) {
            Logger.getLogger(KSCoreService.class.getName()).log(Level.SEVERE, null, ex);
            responseCode = HedwigResponseCode.DB_DUPLICATE;

        } catch (Exception ex) {
            Logger.getLogger(KSCoreService.class.getName()).log(Level.SEVERE, null, ex);
            responseCode = HedwigResponseCode.DB_SEVERE;
        }
        return responseCode;
    }

    public int updateUbacha(UbachaDTO ubachaDTO) {
        int responseCode;

        UbachaDAO ubachaDAO = new UbachaDAO(DatabaseConnection.EMF);
        Ubacha ubacha = ubachaDAO.findUbacha(ubachaDTO.getUbachaId());

        ubacha.setName(ubachaDTO.getUbachaName());
        ubacha.setBachan(ubachaDTO.getUbachaBachan());

        try {
            ubachaDAO.edit(ubacha);
            responseCode = HedwigResponseCode.SUCCESS;
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(KSCoreService.class.getName()).log(Level.SEVERE, null, ex);
            responseCode = HedwigResponseCode.DB_NON_EXISTING;
        } catch (Exception ex) {
            Logger.getLogger(KSCoreService.class.getName()).log(Level.SEVERE, null, ex);
            responseCode = HedwigResponseCode.DB_SEVERE;
        }
        return responseCode;
    }

    public int removeUbacha(UbachaDTO ubachaDTO) {
        int responseCode;

        UbachaDAO ubachaDAO = new UbachaDAO(DatabaseConnection.EMF);
        Ubacha ubacha = ubachaDAO.findUbacha(ubachaDTO.getUbachaId());

        try {
            ubachaDAO.destroy(ubacha.getId());
            responseCode = HedwigResponseCode.SUCCESS;

        } catch (IllegalOrphanException ex) {
            Logger.getLogger(KSCoreService.class.getName()).log(Level.SEVERE, null, ex);
            responseCode = HedwigResponseCode.DB_ILLEGAL_ORPHAN;

        } catch (NonexistentEntityException ex) {
            Logger.getLogger(KSCoreService.class.getName()).log(Level.SEVERE, null, ex);
            responseCode = HedwigResponseCode.DB_NON_EXISTING;
        }
        return responseCode;
    }

    //////////////////// WORDS OPERATIONS ////////////////////
    public void insertWordsToWordsTable(Maintext maintext) {
        WordsDAO wordsDAO = new WordsDAO(DatabaseConnection.EMF);

        String shloka = maintext.getShlokatext().trim();
        String[] shlokaWords = shloka.split(" ");

        for (int i = 0; i < shlokaWords.length; i++) {

            Words words = new Words();
            WordsPK wordsPK = new WordsPK();

            wordsPK.setMaintextParvaId(maintext.getMaintextPK().getParvaId());
            wordsPK.setMaintextAdhyayid(maintext.getMaintextPK().getAdhyayid());
            wordsPK.setMaintextShlokanum(maintext.getMaintextPK().getShlokanum());
            wordsPK.setMaintextShlokaline(maintext.getMaintextPK().getShlokaline());
            wordsPK.setWordnum(i);
            words.setWordsPK(wordsPK);
            words.setMaintext(maintext);
            words.setFirstchar(Character.toString(shlokaWords[i].charAt(0)));
            words.setWordtext(shlokaWords[i]);

            try {
                wordsDAO.create(words);
            } catch (Exception ex) {
                Logger.getLogger(KSCoreService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public List<WordsDTO> getShlokaWordUniqueFirstCharList() {
        WordsDAO wordsDAO = new WordsDAO(DatabaseConnection.EMF);
        
        List<String> firstCharList;
        firstCharList = wordsDAO.getUniqueShlokaWordsFirstCharList();
        List<WordsDTO> wordsDTOList = new ArrayList<>();
        
        for(int i = 0; i<firstCharList.size(); i++) {
            WordsDTO wordsDTO = new WordsDTO();
            
            wordsDTO.setWordFirstChar(firstCharList.get(i));
            
            wordsDTOList.add(wordsDTO);
        }
        return wordsDTOList;
    }
    
    public int getWordsCountByFirstChar(String firstChar){
        WordsDAO wordsDAO = new WordsDAO(DatabaseConnection.EMF);
        int wordsCount = wordsDAO.getWordsCountByFirstChar(firstChar).intValue();
        return wordsCount;
    }
    
    public List<WordsDTO> getWordsListByFirstChar(String selectedFirstChar,int first,int pageSize){
        
        WordsDAO wordsDAO = new WordsDAO(DatabaseConnection.EMF);
        
        List<Words> words = wordsDAO.getWordsByFirstChar(selectedFirstChar, first, pageSize);
        
        List<WordsDTO> wordsDTOList = new ArrayList<>();
        for(int i = 0; i<words.size(); i++) {
            WordsDTO wordsDTO = new WordsDTO();
            
            wordsDTO.setParvaId(words.get(i).getWordsPK().getMaintextParvaId());
            wordsDTO.setAdhyayId(words.get(i).getWordsPK().getMaintextAdhyayid());
            wordsDTO.setShlokaNum(words.get(i).getWordsPK().getMaintextShlokanum());
            wordsDTO.setShlokaLine(words.get(i).getWordsPK().getMaintextShlokaline());
            wordsDTO.setWordText(words.get(i).getWordtext());
            wordsDTO.setWordFirstChar(words.get(i).getFirstchar());
            wordsDTO.setParvaName(words.get(i).getMaintext().getParva().getName());
            wordsDTO.setUbachaId(words.get(i).getMaintext().getUbachaId().getId());
            wordsDTO.setUbachaName(words.get(i).getMaintext().getUbachaId().getName());
            wordsDTO.setUbachaBachan(words.get(i).getMaintext().getUbachaId().getBachan());
            wordsDTO.setShlokaText(words.get(i).getMaintext().getShlokatext());
            
            wordsDTOList.add(wordsDTO);
        }
        return wordsDTOList;
    }

    //////////////////// MAINTEXT OPERATIONS ////////////////////
    
    public List<MaintextDTO> getAdhyayIdList(int parvaId) {
        MaintextDAO maintextDAO = new MaintextDAO(DatabaseConnection.EMF);

        List<Integer> adhyayList = maintextDAO.getAdhyayByParvaId(parvaId);
        List<MaintextDTO> adhyayDTOList = new ArrayList<>();

        for (int i = 0; i < adhyayList.size(); i++) {

            MaintextDTO maintextDTO = new MaintextDTO();

            maintextDTO.setAdhyayId(adhyayList.get(i));

            adhyayDTOList.add(maintextDTO);
        }
        return adhyayDTOList;
    }

    public List<MaintextDTO> getShlokaList(int parvaId, int adhyayId) {
        MaintextDAO maintextDAO = new MaintextDAO(DatabaseConnection.EMF);

        List<Maintext> shlokaList = maintextDAO.getShlokaByParvaAndAdhyayId(parvaId, adhyayId);
        List<MaintextDTO> shlokaDTOList = new ArrayList<>();
        UbachaDTO ubachaDTO = new UbachaDTO();

        for (int i = 0; i < shlokaList.size(); i++) {
            MaintextDTO shlokaDTO = new MaintextDTO();

            ubachaDTO.setUbachaId(shlokaList.get(i).getUbachaId().getId());
            UbachaDTO ubachaData = getUbachaDTO(ubachaDTO);

            shlokaDTO.setUbachaName(ubachaData.getUbachaName());
            shlokaDTO.setUbachaBachan(ubachaData.getUbachaBachan());
            shlokaDTO.setShlokaText(shlokaList.get(i).getShlokatext());
            shlokaDTO.setShlokaLine(shlokaList.get(i).getMaintextPK().getShlokaline());
            shlokaDTO.setShlokaNum(shlokaList.get(i).getMaintextPK().getShlokanum());
            shlokaDTO.setUbachaId(shlokaList.get(i).getUbachaId().getId());

            shlokaDTOList.add(shlokaDTO);
        }
        return shlokaDTOList;
    }

    public int getMaxShlokaNumber(MaintextDTO maintextDTO) {
        MaintextDAO maintextDAO = new MaintextDAO(DatabaseConnection.EMF);

        int maintextMaxShlokaNum;

        maintextMaxShlokaNum = maintextDAO.getMaxShlokaNum(maintextDTO.getParvaId(), maintextDTO.getAdhyayId());

        return maintextMaxShlokaNum;
    }

    public int getMaxShlokaLine(MaintextDTO maintextDTO) {
        MaintextDAO maintextDAO = new MaintextDAO(DatabaseConnection.EMF);

        int maintextMaxShlokaLine;

        maintextMaxShlokaLine = maintextDAO.getMaxShlokaLine(maintextDTO.getParvaId(), maintextDTO.getAdhyayId(), maintextDTO.getMaxShlokaNum());

        return maintextMaxShlokaLine;
    }
    
    public List<MaintextDTO> getMaintextTranslation(int parvaId, int adhyayId, int shlokaNum) {
        MaintextDAO maintextDAO = new MaintextDAO(DatabaseConnection.EMF);
        
        List<Maintext> shlokaList = maintextDAO.getShlokaTranslation(parvaId, adhyayId, shlokaNum);
        List<MaintextDTO> translationDTOList = new ArrayList<>();
        UbachaDTO ubachaDTO = new UbachaDTO();

        for (int i = 0; i < shlokaList.size(); i++) {
            MaintextDTO translationDTO = new MaintextDTO();

            ubachaDTO.setUbachaId(shlokaList.get(i).getUbachaId().getId());
            UbachaDTO ubachaData = getUbachaDTO(ubachaDTO);

            translationDTO.setUbachaName(ubachaData.getUbachaName());
            translationDTO.setUbachaBachan(ubachaData.getUbachaBachan());
            translationDTO.setShlokaText(shlokaList.get(i).getShlokatext());
            translationDTO.setAdhyayId(shlokaList.get(i).getMaintextPK().getAdhyayid());
            translationDTO.setShlokaLine(shlokaList.get(i).getMaintextPK().getShlokaline());
            translationDTO.setShlokaNum(shlokaList.get(i).getMaintextPK().getShlokanum());
            translationDTO.setUbachaId(shlokaList.get(i).getUbachaId().getId());
            translationDTO.setAnubadText(shlokaList.get(i).getTranslatedtext());
            translationDTO.setParvaId(shlokaList.get(i).getMaintextPK().getParvaId());
            translationDTO.setParvaName(shlokaList.get(i).getParva().getName());

            translationDTOList.add(translationDTO);
        }
        return translationDTOList;
    }
    
    public int updateMaintextTranslation(MaintextDTO maintextDTO) {
        int responseCode;

        MaintextDAO maintextDAO = new MaintextDAO(DatabaseConnection.EMF);
        MaintextPK maintextPK = new MaintextPK();
        Date updatedDate = new Date();

        maintextPK.setParvaId(maintextDTO.getParvaId());
        maintextPK.setAdhyayid(maintextDTO.getAdhyayId());
        maintextPK.setShlokanum(maintextDTO.getShlokaNum());
        maintextPK.setShlokaline(1);

        Maintext maintext = maintextDAO.findMaintext(maintextPK);
        
        maintext.setTranslatedtext(maintextDTO.getAnubadText());
        maintext.setLastupdatedts(updatedDate);

        try {
            maintextDAO.edit(maintext);
            responseCode = HedwigResponseCode.SUCCESS;

        } catch (NonexistentEntityException ex) {
            Logger.getLogger(KSCoreService.class.getName()).log(Level.SEVERE, null, ex);
            responseCode = HedwigResponseCode.DB_NON_EXISTING;

        } catch (Exception ex) {
            Logger.getLogger(KSCoreService.class.getName()).log(Level.SEVERE, null, ex);
            responseCode = HedwigResponseCode.DB_SEVERE;
        }
        return responseCode;
    }

    public int addNewShloka(MaintextDTO maintextDTO) {
        int responseCode;

        MaintextDAO maintextDAO = new MaintextDAO(DatabaseConnection.EMF);
        MaintextPK maintextPK = new MaintextPK();
        Maintext maintext = new Maintext();
        Date createdDate = new Date();
        UbachaDAO ubachaDAO = new UbachaDAO(DatabaseConnection.EMF);
        Ubacha ubacha = ubachaDAO.findUbacha(maintextDTO.getUbachaId());
        ParvaDAO parvaDAO = new ParvaDAO(DatabaseConnection.EMF);
        Parva parva = parvaDAO.findParva(maintextDTO.getParvaId());

        maintextPK.setParvaId(maintextDTO.getParvaId());
        maintextPK.setAdhyayid(maintextDTO.getAdhyayId());
        maintextPK.setShlokaline(maintextDTO.getShlokaLine());
        maintextPK.setShlokanum(maintextDTO.getShlokaNum());

        maintext.setMaintextPK(maintextPK);
        maintext.setUbachaId(ubacha);
        maintext.setShlokatext(maintextDTO.getShlokaText());
        maintext.setTranslatedtext(maintextDTO.getAnubadText());
        maintext.setLastupdatedts(createdDate);
        maintext.setParva(parva);

        String startChar = maintextDTO.getShlokaText();
        String endChar = maintextDTO.getEndChar();

        maintext.setFirstchar(Character.toString(startChar.charAt(0)));
        maintext.setEndchar(endChar);

        try {
            maintextDAO.create(maintext);
            insertWordsToWordsTable(maintext);
            responseCode = HedwigResponseCode.SUCCESS;

        } catch (PreexistingEntityException ex) {
            Logger.getLogger(KSCoreService.class.getName()).log(Level.SEVERE, null, ex);
            responseCode = HedwigResponseCode.DB_DUPLICATE;

        } catch (Exception ex) {
            Logger.getLogger(KSCoreService.class.getName()).log(Level.SEVERE, null, ex);
            responseCode = HedwigResponseCode.DB_SEVERE;
        }
        return responseCode;
    }

    public int updateShloka(MaintextDTO maintextDTO) {
        int responseCode;

        MaintextDAO maintextDAO = new MaintextDAO(DatabaseConnection.EMF);
        MaintextPK maintextPK = new MaintextPK();
        Date updatedDate = new Date();
        UbachaDAO ubachaDAO = new UbachaDAO(DatabaseConnection.EMF);
        Ubacha ubacha = ubachaDAO.findUbacha(maintextDTO.getUbachaId());
        ParvaDAO parvaDAO = new ParvaDAO(DatabaseConnection.EMF);
        Parva parva = parvaDAO.findParva(maintextDTO.getParvaId());
        WordsDAO wordsDAO = new WordsDAO(DatabaseConnection.EMF);

        maintextPK.setParvaId(maintextDTO.getParvaId());
        maintextPK.setAdhyayid(maintextDTO.getAdhyayId());
        maintextPK.setShlokaline(maintextDTO.getShlokaLine());
        maintextPK.setShlokanum(maintextDTO.getShlokaNum());
        
        wordsDAO.deleteAllWordsAndChars(maintextDTO.getParvaId(), maintextDTO.getAdhyayId(), maintextDTO.getShlokaNum(), maintextDTO.getShlokaLine());
        Maintext maintext = maintextDAO.findMaintext(maintextPK);

        maintext.setMaintextPK(maintextPK);
        maintext.setUbachaId(ubacha);
        maintext.setShlokatext(maintextDTO.getShlokaText());
        maintext.setTranslatedtext(maintextDTO.getAnubadText());
        maintext.setLastupdatedts(updatedDate);
        maintext.setParva(parva);

        String startChar = maintextDTO.getShlokaText();
        String endChar = maintextDTO.getEndChar();

        maintext.setFirstchar(Character.toString(startChar.charAt(0)));
        maintext.setEndchar(endChar);

        try {
            maintextDAO.edit(maintext);
            
            insertWordsToWordsTable(maintext);
            responseCode = HedwigResponseCode.SUCCESS;

        } catch (NonexistentEntityException ex) {
            Logger.getLogger(KSCoreService.class.getName()).log(Level.SEVERE, null, ex);
            responseCode = HedwigResponseCode.DB_NON_EXISTING;

        } catch (Exception ex) {
            Logger.getLogger(KSCoreService.class.getName()).log(Level.SEVERE, null, ex);
            responseCode = HedwigResponseCode.DB_SEVERE;
        }
        return responseCode;
    }

    public int removeShloka(MaintextDTO maintextDTO) {
        int responseCode;

        MaintextDAO maintextDAO = new MaintextDAO(DatabaseConnection.EMF);
        WordsDAO wordsDAO = new WordsDAO(DatabaseConnection.EMF);
        MaintextPK maintextPK = new MaintextPK();

        maintextPK.setParvaId(maintextDTO.getParvaId());
        maintextPK.setAdhyayid(maintextDTO.getAdhyayId());
        maintextPK.setShlokaline(maintextDTO.getShlokaLine());
        maintextPK.setShlokanum(maintextDTO.getShlokaNum());

        try {
            wordsDAO.deleteAllWordsAndChars(maintextDTO.getParvaId(), maintextDTO.getAdhyayId(), maintextDTO.getShlokaNum(), maintextDTO.getShlokaLine());
            maintextDAO.destroy(maintextPK);
            responseCode = HedwigResponseCode.SUCCESS;

        } catch (NonexistentEntityException ex) {
            Logger.getLogger(KSCoreService.class.getName()).log(Level.SEVERE, null, ex);
            responseCode = HedwigResponseCode.DB_NON_EXISTING;

        } catch (IllegalOrphanException ex) {
            Logger.getLogger(KSCoreService.class.getName()).log(Level.SEVERE, null, ex);
            responseCode = HedwigResponseCode.DB_ILLEGAL_ORPHAN;

        }
        return responseCode;
    }
    
    public List<MaintextDTO> getShlokaUniqueFirstCharList() {
        MaintextDAO maintextDAO = new MaintextDAO(DatabaseConnection.EMF);
        
        List<String> firstCharList;
        firstCharList = maintextDAO.getUniqueShlokaFirstCharList();
        List<MaintextDTO> maintextDTOList = new ArrayList<>();
        
        for(int i = 0; i<firstCharList.size(); i++) {
            MaintextDTO maintextDTO = new MaintextDTO();
            
            maintextDTO.setFirstChar(firstCharList.get(i));
            
            maintextDTOList.add(maintextDTO);
        }
        return maintextDTOList;
    }
    
    public List<MaintextDTO> getShlokaListByFirstChar(String selectedFirstChar,int first,int pageSize){
        
        MaintextDAO maintextDAO = new MaintextDAO(DatabaseConnection.EMF);
        
        List<Maintext> maintext = maintextDAO.getShlokaByFirstChar(selectedFirstChar, first, pageSize);
        
        List<MaintextDTO> maintextDTOList = new ArrayList<>();
        for(int i = 0; i<maintext.size(); i++) {
            MaintextDTO maintextDTO = new MaintextDTO();
            
            maintextDTO.setParvaId(maintext.get(i).getMaintextPK().getParvaId());
            maintextDTO.setAdhyayId(maintext.get(i).getMaintextPK().getAdhyayid());
            maintextDTO.setShlokaNum(maintext.get(i).getMaintextPK().getShlokanum());
            maintextDTO.setShlokaLine(maintext.get(i).getMaintextPK().getShlokaline());
            maintextDTO.setShlokaText(maintext.get(i).getShlokatext());
            maintextDTO.setParvaName(maintext.get(i).getParva().getName());
            maintextDTO.setUbachaId(maintext.get(i).getUbachaId().getId());
            maintextDTO.setUbachaName(maintext.get(i).getUbachaId().getName());
            maintextDTO.setUbachaBachan(maintext.get(i).getUbachaId().getBachan());
            
            maintextDTOList.add(maintextDTO);
        }
        return maintextDTOList;
    }
    
    public int getShlokaCountByFirstChar(String firstChar){
        MaintextDAO maintextDAO = new MaintextDAO(DatabaseConnection.EMF);
        int shlokaCount = maintextDAO.getShlokaCountByFirstChar(firstChar).intValue();
        return shlokaCount;
    }
    
    //////////////////// REFERENCETEXT OPERATIONS ////////////////////
 
    public List<TikaDTO> getReftextList(int parvaId, int adhyayId, int shlokaNum, int shlokaLine) {
        ReferencetextDAO referencetextDAO = new ReferencetextDAO(DatabaseConnection.EMF);
        List<Referencetext> referencetextList = referencetextDAO.getAnubadTika(parvaId, adhyayId, shlokaNum, shlokaLine);
        
        List<TikaDTO> tikaDTOList = new ArrayList<>();
        
        for(int i =0; i<referencetextList.size(); i++){
            TikaDTO tikaDTO = new TikaDTO();
            
            tikaDTO.setParvaId(parvaId);
            tikaDTO.setAdhyayId(adhyayId);
            tikaDTO.setShlokaNum(shlokaNum);
            tikaDTO.setShlokaLine(shlokaLine);
            tikaDTO.setRefTextId(referencetextList.get(i).getReferencetextPK().getReftextid());
            tikaDTO.setRefText(referencetextList.get(i).getText());
            //referencetextDTO.setRefTextPosition(referencetextList.get(i).getReferencetextpos());
            
            tikaDTOList.add(tikaDTO);
        }
        
        return tikaDTOList;
    }
    
    public int addTika(TikaDTO tikaDTO) {
        int responseCode;

        ReferencetextDAO referencetextDAO = new ReferencetextDAO(DatabaseConnection.EMF);
        ReferencetextPK referencetextPK = new ReferencetextPK();
        Referencetext referencetext = new Referencetext();
        MaintextPK maintextPK = new MaintextPK();
        MaintextDAO maintextDAO = new MaintextDAO(DatabaseConnection.EMF);
        
        referencetextPK.setMaintextParvaId(tikaDTO.getParvaId());
        referencetextPK.setMaintextAdhyayid(tikaDTO.getAdhyayId());
        referencetextPK.setMaintextShlokanum(tikaDTO.getShlokaNum());
        referencetextPK.setMaintextShlokaline(tikaDTO.getShlokaLine());
        referencetextPK.setReftextid(tikaDTO.getRefTextId());

        maintextPK.setParvaId(tikaDTO.getParvaId());
        maintextPK.setAdhyayid(tikaDTO.getAdhyayId());
        maintextPK.setShlokaline(tikaDTO.getShlokaLine());
        maintextPK.setShlokanum(tikaDTO.getShlokaNum());
        
        Maintext maintext = maintextDAO.findMaintext(maintextPK);
        
        referencetext.setReferencetextPK(referencetextPK);
        referencetext.setMaintext(maintext);
        referencetext.setText(tikaDTO.getRefText());

        try {
            referencetextDAO.create(referencetext);
            responseCode = HedwigResponseCode.SUCCESS;

        } catch (PreexistingEntityException ex) {
            Logger.getLogger(KSCoreService.class.getName()).log(Level.SEVERE, null, ex);
            responseCode = HedwigResponseCode.DB_DUPLICATE;

        } catch (Exception ex) {
            Logger.getLogger(KSCoreService.class.getName()).log(Level.SEVERE, null, ex);
            responseCode = HedwigResponseCode.DB_SEVERE;
        }
        return responseCode;
    }
    
    public int updateTika(TikaDTO tikaDTO) {
        int responseCode;

        ReferencetextDAO referencetextDAO = new ReferencetextDAO(DatabaseConnection.EMF);
        ReferencetextPK referencetextPK = new ReferencetextPK();
        MaintextPK maintextPK = new MaintextPK();
        MaintextDAO maintextDAO = new MaintextDAO(DatabaseConnection.EMF);
        
        referencetextPK.setMaintextParvaId(tikaDTO.getParvaId());
        referencetextPK.setMaintextAdhyayid(tikaDTO.getAdhyayId());
        referencetextPK.setMaintextShlokanum(tikaDTO.getShlokaNum());
        referencetextPK.setMaintextShlokaline(tikaDTO.getShlokaLine());
        referencetextPK.setReftextid(tikaDTO.getRefTextId());

        maintextPK.setParvaId(tikaDTO.getParvaId());
        maintextPK.setAdhyayid(tikaDTO.getAdhyayId());
        maintextPK.setShlokaline(tikaDTO.getShlokaLine());
        maintextPK.setShlokanum(tikaDTO.getShlokaNum());
        
        Maintext maintext = maintextDAO.findMaintext(maintextPK);
        Referencetext referencetext = referencetextDAO.findReferencetext(referencetextPK);
        
        referencetext.setReferencetextPK(referencetextPK);
        referencetext.setMaintext(maintext);
        referencetext.setText(tikaDTO.getRefText());

        try {
            referencetextDAO.edit(referencetext);
            responseCode = HedwigResponseCode.SUCCESS;

        } catch (NonexistentEntityException ex) {
            Logger.getLogger(KSCoreService.class.getName()).log(Level.SEVERE, null, ex);
            responseCode = HedwigResponseCode.DB_NON_EXISTING;

        } catch (Exception ex) {
            Logger.getLogger(KSCoreService.class.getName()).log(Level.SEVERE, null, ex);
            responseCode = HedwigResponseCode.DB_SEVERE;
        }
        return responseCode;
    }
    
        public int deleteTika(TikaDTO tikaDTO) {
        int responseCode;

        ReferencetextDAO referencetextDAO = new ReferencetextDAO(DatabaseConnection.EMF);
        ReferencetextPK referencetextPK = new ReferencetextPK();
//        MaintextPK maintextPK = new MaintextPK();
//        MaintextDAO maintextDAO = new MaintextDAO(DatabaseConnection.EMF);
        
        referencetextPK.setMaintextParvaId(tikaDTO.getParvaId());
        referencetextPK.setMaintextAdhyayid(tikaDTO.getAdhyayId());
        referencetextPK.setMaintextShlokanum(tikaDTO.getShlokaNum());
        referencetextPK.setMaintextShlokaline(tikaDTO.getShlokaLine());
        referencetextPK.setReftextid(tikaDTO.getRefTextId());
//
//        maintextPK.setParvaId(tikaDTO.getParvaId());
//        maintextPK.setAdhyayid(tikaDTO.getAdhyayId());
//        maintextPK.setShlokaline(tikaDTO.getShlokaLine());
//        maintextPK.setShlokanum(tikaDTO.getShlokaNum());
//        
//        Maintext maintext = maintextDAO.findMaintext(maintextPK);
//        Referencetext referencetext = referencetextDAO.findReferencetext(referencetextPK);
        
//        referencetext.setReferencetextPK(referencetextPK);
//        referencetext.setMaintext(maintext);
//        referencetext.setText(tikaDTO.getRefText());

        try {
            referencetextDAO.destroy(referencetextPK);
            responseCode = HedwigResponseCode.SUCCESS;

        } catch (NonexistentEntityException ex) {
            Logger.getLogger(KSCoreService.class.getName()).log(Level.SEVERE, null, ex);
            responseCode = HedwigResponseCode.DB_NON_EXISTING;

        } catch (Exception ex) {
            Logger.getLogger(KSCoreService.class.getName()).log(Level.SEVERE, null, ex);
            responseCode = HedwigResponseCode.DB_SEVERE;
        }
        return responseCode;
    }
    
    //////////////////// LANDING OPERATIONS ////////////////////
    
    
    public MaintextDTO getTranslationPercentage() {
        MaintextDAO maintextDAO = new MaintextDAO(DatabaseConnection.EMF);
        
        int shlokaCount = (maintextDAO.getNotTranslatedShlokaCount().intValue() + maintextDAO.getTranslatedShlokaCount().intValue());
        int shlokaTranslationCount = maintextDAO.getTranslatedShlokaCount().intValue();
        
        MaintextDTO maintextDTO = new MaintextDTO();
        
        maintextDTO.setShlokaNumCount(shlokaCount);
        maintextDTO.setShlokaNumTranslatedCount(shlokaTranslationCount);
        
        return maintextDTO;
    }
}
