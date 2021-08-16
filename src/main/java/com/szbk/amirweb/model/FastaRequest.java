package com.szbk.amirweb.model;

public class FastaRequest {

    private String fastaText;

    public FastaRequest() {
    }

    public FastaRequest(String fastaText) {
        this.fastaText = fastaText;
    }

    public String getFastaText() {
        return fastaText;
    }

    public void setFastaText(String fastaText) {
        this.fastaText = fastaText;
    }
}
