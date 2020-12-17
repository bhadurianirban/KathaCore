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
public class ReferencetextDTO {
    private int refTextId;
    private int parvaId;
    private int adhyayId;
    private int shlokaNum;
    private int shlokaLine;
    
    private String refText;
    private int refTextPosition;

    public int getRefTextId() {
        return refTextId;
    }

    public void setRefTextId(int refTextId) {
        this.refTextId = refTextId;
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

    public String getRefText() {
        return refText;
    }

    public void setRefText(String refText) {
        this.refText = refText;
    }

    public int getRefTextPosition() {
        return refTextPosition;
    }

    public void setRefTextPosition(int refTextPosition) {
        this.refTextPosition = refTextPosition;
    }

    
}
