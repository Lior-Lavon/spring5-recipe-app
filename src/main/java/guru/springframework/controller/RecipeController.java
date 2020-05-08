package guru.springframework.controller;

import guru.springframework.command.RecipeCommand;
import guru.springframework.exceptions.NotFoundException;
import guru.springframework.models.Recipe;
import guru.springframework.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Slf4j
@Controller
public class RecipeController {

    public static final String RECIPE_RECIPEFORM_URL = "/recipe/recipeform";
    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/recipe/{id}/show")
    public String showById(Model model, @PathVariable String id){

        Recipe recipe = recipeService.findById(Long.valueOf(id));
        model.addAttribute("recipe", recipe);

        return "recipe/show";
    }

    // load an empty recipe form for creat
    @GetMapping("/recipe/new")
    public String loadNewRecipeForm(Model model){

        model.addAttribute("recipe", new RecipeCommand());

        return RECIPE_RECIPEFORM_URL;
    }

    // load a filed form with recipe
    @GetMapping("/recipe/{id}/update")
    public String loadFiledRecipeFormForUpdate(Model model, @PathVariable String id){
        RecipeCommand recipeCommand = recipeService.findCommandById(Long.valueOf(id));

        model.addAttribute("recipe", recipeCommand);

        return RECIPE_RECIPEFORM_URL;
    }

    // handle the post
    @PostMapping("/recipe")
    public String saveOrUpdate(@Valid @ModelAttribute("recipe") RecipeCommand command, BindingResult bindingResult){

        // BindingResult holds validation result
        if(bindingResult.hasErrors()){
            bindingResult.getAllErrors().forEach(objectError -> {
                log.error(objectError.toString());
            });

            // send back the received command including the error
            return RECIPE_RECIPEFORM_URL;
        }

        RecipeCommand savedCommand = recipeService.saveRecipeCommand(command);

        return "redirect:/recipe/" + savedCommand.getId() + "/show";
    }

    @GetMapping("/recipe/{id}/delete")
    public String deleteRecipe(@PathVariable String id){
        log.debug("deleteRecipe :" + id);

        recipeService.deleteById(Long.valueOf(id));
        return "redirect:/";
    }

    // How to handle an error Not Found exception with a greas full manner
    // Exception Handler using the NotFoundException exception class
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ModelAndView HandleNotFound(Exception exception){

        log.error("Handling not found exception");
        log.error(exception.getMessage());

        ModelAndView modelAndView = new ModelAndView();

        // 404error match the view name html
        modelAndView.setViewName("404error");
        modelAndView.addObject("exception", exception);

        return modelAndView;
    }

}
