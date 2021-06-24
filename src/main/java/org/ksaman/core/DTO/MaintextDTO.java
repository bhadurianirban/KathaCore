/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ksaman.core.DTO;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author dgrfiv
 */
public class MaintextDTO implements Serializable{
    
    private int parvaId;
    private int adhyayId;
    private int ubachaId;
    private int shlokaNum;
    private int shlokaLine;
    private String shlokaText;
    private String firstChar;
    private String endChar;
    private String anubadText;
    private String ubachaName;
    private String ubachaBachan;
    private String parvaName;
    private Date lastEditTS;
    private int shlokaNumCount;
    private int shlokaNumTranslatedCount;
    
    private int maxShlokaNum;
    private int maxShlokaLine;
    private List<TikaDTO> tikaDTOList;
    private TikaDTO referencetextDTO;

    public int getParvaId() {
        return parvaId;
    }

    public void setParvaId(int parvaId) {
        this.parvaId = parvaId;
    }

    public int getAdhyayId() {
        return adhyayId;
    }

    public void setAdhyayId(int adhyayId) {
        this.adhyayId = adhyayId;
    }

    public int getUbachaId() {
        return ubachaId;
    }

    public void setUbachaId(int ubachaId) {
        this.ubachaId = ubachaId;
    }

    public int getShlokaNum() {
        return shlokaNum;
    }

    public void setShlokaNum(int shlokaNum) {
        this.shlokaNum = shlokaNum;
    }

    public int getShlokaLine() {
        return shlokaLine;
    }

    public void setShlokaLine(int shlokaLine) {
        this.shlokaLine = shlokaLine;
    }

    public String getShlokaText() {
        return shlokaText;
    }

    public void setShlokaText(String shlokaText) {
        this.shlokaText = shlokaText;
    }

    public String getFirstChar() {
        return firstChar;
    }

    public void setFirstChar(String firstChar) {
        this.firstChar = firstChar;
    }

    public String getEndChar() {
        return endChar;
    }

    public void setEndChar(String endChar) {
        this.endChar = endChar;
    }

    public String getAnubadText() {
        return anubadText;
    }

    public void setAnubadText(String anubadText) {
        this.anubadText = anubadText;
    }

    public Date getLastEditTS() {
        return lastEditTS;
    }

    public void setLastEditTS(Date lastEditTS) {
        this.lastEditTS = lastEditTS;
    }

    public String getUbachaName() {
        return ubachaName;
    }

    public void setUbachaName(String ubachaName) {
        this.ubachaName = ubachaName;
    }

    public String getUbachaBachan() {
        return ubachaBachan;
    }

    public void setUbachaBachan(String ubachaBachan) {
        this.ubachaBachan = ubachaBachan;
    }

    public int getMaxShlokaNum() {
        return maxShlokaNum;
    }

    public void setMaxShlokaNum(int maxShlokaNum) {
        this.maxShlokaNum = maxShlokaNum;
    }

    public int getMaxShlokaLine() {
        return maxShlokaLine;
    }

    public void setMaxShlokaLine(int maxShlokaLine) {
        this.maxShlokaLine = maxShlokaLine;
    }

    public String getParvaName() {
        return parvaName;
    }

    public void setParvaName(String parvaName) {
        this.parvaName = parvaName;
    }

    public int getShlokaNumCount() {
        return shlokaNumCount;
    }

    public void setShlokaNumCount(int shlokaNumCount) {
        this.shlokaNumCount = shlokaNumCount;
    }

    public int getShlokaNumTranslatedCount() {
        return shlokaNumTranslatedCount;
    }

    public void setShlokaNumTranslatedCount(int shlokaNumTranslatedCount) {
        this.shlokaNumTranslatedCount = shlokaNumTranslatedCount;
    }

    public List<TikaDTO> getTikaDTOList() {
        return tikaDTOList;
    }

    public void setTikaDTOList(List<TikaDTO> tikaDTOList) {
        this.tikaDTOList = tikaDTOList;
    }

    public TikaDTO getReferencetextDTO() {
        return referencetextDTO;
    }

    public void setReferencetextDTO(TikaDTO referencetextDTO) {
        this.referencetextDTO = referencetextDTO;
    }

    
    
}
