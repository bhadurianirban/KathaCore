/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ksaman.core.DTO;

/**
 *
 * @author dgrfiv
 */
public class WordsDTO {

    private int wordNum;
    private int parvaId;
    private int adhyayId;
    private int shlokaNum;
    private int shlokaLine;
    private String shlokaText;
    private String wordText;
    private String wordFirstChar;
    private String parvaName;
    private int ubachaId;
    private String ubachaName;
    private String ubachaBachan;

    public int getWordNum() {
        return wordNum;
    }

    public void setWordNum(int wordNum) {
        this.wordNum = wordNum;
    }

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

    public String getWordText() {
        return wordText;
    }

    public void setWordText(String wordText) {
        this.wordText = wordText;
    }

    public String getWordFirstChar() {
        return wordFirstChar;
    }

    public void setWordFirstChar(String wordFirstChar) {
        this.wordFirstChar = wordFirstChar;
    }

    public String getParvaName() {
        return parvaName;
    }

    public void setParvaName(String parvaName) {
        this.parvaName = parvaName;
    }

    public int getUbachaId() {
        return ubachaId;
    }

    public void setUbachaId(int ubachaId) {
        this.ubachaId = ubachaId;
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

    public String getShlokaText() {
        return shlokaText;
    }

    public void setShlokaText(String shlokaText) {
        this.shlokaText = shlokaText;
    }

}
