package com.szbk.amirweb.model;

public class FastaResult {

    private Integer number;
    private String fasta;
    private String blastUrl;

    public FastaResult(Integer number, String fasta, String blastUrl) {
        this.number = number;
        this.fasta = fasta;
        this.blastUrl = blastUrl;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getFasta() {
        return fasta;
    }

    public void setFasta(String fasta) {
        this.fasta = fasta;
    }

    public String getBlastUrl() {
        return blastUrl;
    }

    public void setBlastUrl(String blastUrl) {
        this.blastUrl = blastUrl;
    }
}
