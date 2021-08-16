package com.szbk.amirweb.controller;

import com.szbk.amirweb.model.FastaRequest;
import com.szbk.amirweb.model.FastaResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.awt.*;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class AmirController {

    @GetMapping({"/","/index"})
    public String home(Model model){
        model.addAttribute("fastaRequest", new FastaRequest());
        return "index";
    }

    @PostMapping("/index")
    public String parse(@ModelAttribute("fastaRequest")  FastaRequest fastaRequest, BindingResult bindingResult,
                        Model model) throws UnsupportedEncodingException {

        if (fastaRequest != null && StringUtils.hasText(fastaRequest.getFastaText())) {
            List<FastaResult> fastaResults = new ArrayList<>();
            String encodedSpeciesString = URLEncoder.encode("Mus musculus (taxid:10090)", StandardCharsets.UTF_8.toString());
            int number = 1;
            String rna = fastaRequest.getFastaText().toUpperCase().trim().replace("\n", "").replace("\r", "");
            for (int i = 0; i <= rna.length() - 22; i++) {
                String testString = rna.substring(i, i + 22);

                if (startWithGC(testString) && isCorrectTAPosition(testString) && thirdCharNotTA(testString) &&
                        correctAPosition(testString) && gcContentCheck19(testString) && gcContentCheck1022(testString) &&
                        consecutiveLetter(testString)) {
                    FastaResult fastaResult = new FastaResult(number, testString, "https://blast.ncbi.nlm.nih.gov/Blast.cgi?QUERY="+testString+"&CMD=Put&DATABASE=refseq_rna&PROGRAM=blastn&MEGABLAST=on&EQ_MENU="+encodedSpeciesString);
                    fastaResults.add(fastaResult);
                    number++;
                }
            }

            String queryString = fastaResults.stream().map(a -> "> " + a.getNumber() + " " + a.getFasta() + "\n" + a.getFasta() + "\n").collect(Collectors.joining());
            String encodedQueryString = URLEncoder.encode(queryString, StandardCharsets.UTF_8.toString());
            String blastAllUrl = "https://blast.ncbi.nlm.nih.gov/Blast.cgi?QUERY=" + encodedQueryString + "&CMD=Put&DATABASE=refseq_rna&PROGRAM=blastn&MEGABLAST=on&EQ_MENU="+encodedSpeciesString+"&BLAST_PROGRAMS=megaBlast&PAGE=MegaBlast&DEFAULT_PROG=megaBlast&SELECTED_PROG_TYPE=megaBlast";
            String copyString = fastaResults.stream().map(f -> f.getNumber().toString() + ": " + f.getFasta()).collect(Collectors.joining("\n"));

            model.addAttribute("fastaResults", fastaResults);
            model.addAttribute("fastaRequest", fastaRequest);
            model.addAttribute("blastAllUrl", blastAllUrl);
            model.addAttribute("copyString", copyString);
        } else {
            model.addAttribute("fastaResults", null);
            model.addAttribute("fastaRequest", new FastaRequest());
        }

        return "index";
    }

    private boolean startWithGC(String testString) {
        return testString.startsWith("G") || testString.startsWith("C");
    }

    private boolean thirdCharNotTA(String testString) {
        return testString.charAt(2)!='A';
    }

    private boolean gcContentCheck19(String testString) {
        int gcNumber = (int) testString.substring(0, 9).chars().filter(ch -> ch == 'G' || ch == 'C').count();
        return gcNumber >= 5;
    }

    private boolean correctAPosition(String testString) {
        return testString.charAt(9)=='A' || testString.charAt(9)=='T';
    }

    private boolean gcContentCheck1022(String testString) {
        int gcNumber = (int) testString.substring(9).chars().filter(ch -> ch == 'G' || ch == 'C').count();
        return gcNumber <= 6;
    }

    private boolean consecutiveLetter(String testString) {
        return !testString.contains("AAAAA") && !testString.contains("GGGG") && !testString.contains("TTTTT") && !testString.contains("CCCC");
    }
    private boolean isCorrectTAPosition(String testString) {
        return testString.charAt(21)=='T' || testString.charAt(21)=='A';
    }
}
