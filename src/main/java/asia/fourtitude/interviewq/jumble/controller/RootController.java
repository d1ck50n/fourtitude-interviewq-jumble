package asia.fourtitude.interviewq.jumble.controller;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import asia.fourtitude.interviewq.jumble.core.JumbleEngine;
import asia.fourtitude.interviewq.jumble.model.ExistsForm;
import asia.fourtitude.interviewq.jumble.model.PrefixForm;
import asia.fourtitude.interviewq.jumble.model.ScrambleForm;
import asia.fourtitude.interviewq.jumble.model.SearchForm;
import asia.fourtitude.interviewq.jumble.model.SubWordsForm;

@Controller
@RequestMapping(path = "/")
public class RootController {

    private static final Logger LOG = LoggerFactory.getLogger(RootController.class);

    private final JumbleEngine jumbleEngine;

    @Autowired(required = true)
    public RootController(JumbleEngine jumbleEngine) {
        this.jumbleEngine = jumbleEngine;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("timeNow", ZonedDateTime.now());
        return "index";
    }

    @GetMapping("scramble")
    public String doGetScramble(Model model) {
        model.addAttribute("form", new ScrambleForm());
        return "scramble";
    }

    @PostMapping("scramble")
    public String doPostScramble(
            @ModelAttribute(name = "form") ScrambleForm form,
            BindingResult bindingResult, Model model) {
        String word = form.getWord(); // Assuming ScrambleForm has a getWord() method

        // Validate the input `form`
        if (word == null || word.length() < 3 || word.length() > 30) {
            bindingResult.rejectValue("word", "error.word", "size must be between 3 and 30");
        } else {
            form.setScramble(jumbleEngine.scramble(word));
        }
        return "scramble"; // Return to the same view with error message
    }

    @GetMapping("palindrome")
    public String doGetPalindrome(Model model) {
        model.addAttribute("words", this.jumbleEngine.retrievePalindromeWords());
        return "palindrome";
    }

    @GetMapping("exists")
    public String doGetExists(Model model) {
        model.addAttribute("form", new ExistsForm());
        return "exists";
    }

    @PostMapping("exists")
    public String doPostExists(
            @ModelAttribute(name = "form") ExistsForm form,
            BindingResult bindingResult, Model model) {
        String word = form.getWord().trim(); // Trim spaces around the word

        if (word.isEmpty()) {
            bindingResult.rejectValue("word", "error.word", "Word must not be empty");
            return "exists"; // Return to the same view with an error
        }

        // b) Call JumbleEngine#exists() to check if the word exists
        boolean exists = jumbleEngine.exists(word);

        // c) Prepare the response in the form
        form.setExists(exists); // Assuming ExistsForm has a setExists method
        form.setWord(word); // Ensure the word is set in the form

        return "exists"; // Return the view to display the result
    }

    @GetMapping("prefix")
    public String doGetPrefix(Model model) {
        model.addAttribute("form", new PrefixForm());
        return "prefix";
    }

    @PostMapping("prefix")
    public String doPostPrefix(
            @ModelAttribute(name = "form") PrefixForm form,
            BindingResult bindingResult, Model model) {
        // a) Validate the input `form`
        String prefix = form.getPrefix().trim(); // Trim spaces around the prefix

        if (prefix.isEmpty()) {
            bindingResult.rejectValue("prefix", "error.prefix", "Prefix must not be empty");
            return "prefix"; // Return to the same view with an error
        }

        Collection<String> matchingWords = jumbleEngine.wordsMatchingPrefix(prefix);
        form.setWords(matchingWords);

        model.addAttribute("form", form); // Update the form in the model with results

        return "prefix"; // Return the view to display the result
    }

    @GetMapping("search")
    public String doGetSearch(Model model) {
        model.addAttribute("form", new SearchForm());
        return "search";
    }

    @PostMapping("search")
    public String doPostSearch(
            @ModelAttribute(name = "form") SearchForm form,
            BindingResult bindingResult, Model model) {
        // Validate startChar
        if (form.getStartChar() == null || form.getStartChar().length() != 1) {
            bindingResult.rejectValue("startChar", "error.startChar", "Invalid start character.");
        }

        // Validate endChar
        if (form.getEndChar() == null || form.getEndChar().length() != 1) {
            bindingResult.rejectValue("endChar", "error.endChar", "Invalid end character.");
        }

        // Validate length
        if (form.getLength() == null || form.getLength() <= 0) {
            bindingResult.rejectValue("length", "error.length", "Invalid length.");
        }

        // If there are errors, return to the same view
        if (bindingResult.hasErrors()) {
            return "search";
        }

        // Call the search method from JumbleEngine
        Character startChar = form.getStartChar().charAt(0);
        Character endChar = form.getEndChar().charAt(0);
        Integer length = form.getLength();

        // Assuming searchWords returns a Collection<String>
        Collection<String> words = jumbleEngine.searchWords(startChar, endChar, length);
        form.setWords(words); // Assuming SearchForm has a setWords method

        return "search"; // Return to the same view to display results
    }

    @GetMapping("subWords")
    public String goGetSubWords(Model model) {
        model.addAttribute("form", new SubWordsForm());
        return "subWords";
    }

    @PostMapping("subWords")
    public String doPostSubWords(
            @ModelAttribute(name = "form") SubWordsForm form,
            BindingResult bindingResult, Model model) {
        /*
         * TODO:
         * a) Validate the input `form`
         * b) To call JumbleEngine#generateSubWords()
         * c) Presentation page to show the result
         * d) Must pass the corresponding unit tests
         */

        return "subWords";
    }

}
