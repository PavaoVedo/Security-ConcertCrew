package hr.algebra.concertcrew.controller.mvc;

import hr.algebra.concertcrew.dto.ConcertDto;
import hr.algebra.concertcrew.entity.User;
import hr.algebra.concertcrew.enums.Genre;
import hr.algebra.concertcrew.enums.ShowType;
import hr.algebra.concertcrew.enums.VenueSize;
import hr.algebra.concertcrew.enums.ViewingArea;
import hr.algebra.concertcrew.service.ConcertService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.NoSuchElementException;

@Controller
@RequestMapping("/concerts")
public class ConcertMvcController {

    private final ConcertService concertService;

    public ConcertMvcController(ConcertService concertService) {
        this.concertService = concertService;
    }

    @GetMapping
    public String list(
        @RequestParam(required = false) String query,
        @RequestParam(required = false) Genre genre,
        @RequestParam(required = false) ShowType showType,
        @RequestParam(defaultValue = "false") boolean tearJerkerOnly,
        Model model
    ) {
        boolean searching = query != null || genre != null || showType != null || tearJerkerOnly;
        model.addAttribute("concerts", searching
            ? concertService.search(query, genre, showType, tearJerkerOnly)
            : concertService.findAll());
        addEnumsToModel(model);
        model.addAttribute("query", query);
        model.addAttribute("selectedGenre", genre);
        model.addAttribute("selectedShowType", showType);
        model.addAttribute("tearJerkerOnly", tearJerkerOnly);
        return "concerts/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        try {
            model.addAttribute("concert", concertService.findById(id));
            return "concerts/detail";
        } catch (NoSuchElementException e) {
            return "redirect:/concerts";
        }
    }

    @GetMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String newForm(Model model) {
        model.addAttribute("concert", emptyDto());
        addEnumsToModel(model);
        model.addAttribute("editMode", false);
        return "concerts/form";
    }

    @PostMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String create(
        @Valid @ModelAttribute("concert") ConcertDto dto,
        BindingResult result,
        @AuthenticationPrincipal User currentUser,
        Model model,
        RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors()) {
            addEnumsToModel(model);
            model.addAttribute("editMode", false);
            return "concerts/form";
        }
        concertService.create(dto, currentUser);
        redirectAttributes.addFlashAttribute("successMessage", "Concert added to your gig log.");
        return "redirect:/concerts";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String editForm(@PathVariable Long id, Model model) {
        try {
            model.addAttribute("concert", concertService.findById(id));
            addEnumsToModel(model);
            model.addAttribute("editMode", true);
            return "concerts/form";
        } catch (NoSuchElementException e) {
            return "redirect:/concerts";
        }
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String update(
        @PathVariable Long id,
        @Valid @ModelAttribute("concert") ConcertDto dto,
        BindingResult result,
        Model model,
        RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors()) {
            addEnumsToModel(model);
            model.addAttribute("editMode", true);
            return "concerts/form";
        }
        concertService.update(id, dto);
        redirectAttributes.addFlashAttribute("successMessage", "Concert updated.");
        return "redirect:/concerts";
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        concertService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Concert removed.");
        return "redirect:/concerts";
    }

    private void addEnumsToModel(Model model) {
        model.addAttribute("genres", Genre.values());
        model.addAttribute("showTypes", ShowType.values());
        model.addAttribute("viewingAreas", ViewingArea.values());
        model.addAttribute("venueSizes", VenueSize.values());
    }

    /**
     * Build an empty ConcertDto for the "new" form. Argument count and order MUST match
     * the 40 components in {@link ConcertDto}; the numbers below correspond to the
     * numbered comments in that record's declaration.
     */
    private static ConcertDto emptyDto() {
        return new ConcertDto(
            null,   //  1 id
            "",     //  2 mainArtist
            "",     //  3 tourName
            "",     //  4 openingActs
            "",     //  5 venue
            "",     //  6 city
            "",     //  7 country
                Genre.values()[0],   //  8 genre
            null,   //  9 secondaryGenre
                ShowType.values()[0],   // 10 showType
            null,   // 11 viewingArea
            null,   // 12 venueSize
                java.time.LocalDate.now(),   // 13 dateAttended
            null,   // 14 ticketPriceEur
            null,   // 15 merchSpentEur
            null,   // 16 travelCostEur
            null,   // 17 rating
            null,   // 18 vibeScore
            null,   // 19 setlistScore
            null,   // 20 productionScore
            null,   // 21 crowdEnergyScore
            null,   // 22 vocalFormScore
            null,   // 23 durationMinutes
            null,   // 24 songsPlayed
            null,   // 25 encoreCount
            false,  // 26 phoneFreeShow
            false,  // 27 firstTimeSeen
            false,  // 28 tearJerker
            false,  // 29 dancedAllNight
            false,  // 30 boughtMerch
            "",     // 31 crewWith
            "",     // 32 favoriteSong
            "",     // 33 bestMoment
            "",     // 34 moodTags
            "",     // 35 setlistNotes
            "",     // 36 personalNotes
            null,   // 37 totalSpentEur (derived, read-only)
            null,   // 38 addedBy
            null,   // 39 createdAt
            null    // 40 updatedAt
        );
    }
}
