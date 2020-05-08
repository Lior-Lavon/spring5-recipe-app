package guru.springframework.controller;

import guru.springframework.command.RecipeCommand;
import guru.springframework.exceptions.NotFoundException;
import guru.springframework.models.Recipe;
import guru.springframework.services.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class RecipeControllerTest {

    @InjectMocks
    RecipeController recipeController;

    @Mock
    RecipeService recipeService;

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {

        // create mockito mock
        MockitoAnnotations.initMocks(this);

        recipeController = new RecipeController(recipeService);

        // build Mock Mvc from indexController
        mockMvc = MockMvcBuilders.standaloneSetup(recipeController)
                .setControllerAdvice(new ControllerExceptionHandler()) // Wire in the ControllerAdvise as global handler
                .build();

    }

    @Test
    public void testGetRecipe() throws Exception {

        Recipe recipe = new Recipe();
        recipe.setId(1L);

        when(recipeService.findById(anyLong())).thenReturn(recipe);

        mockMvc.perform(MockMvcRequestBuilders.get("/recipe/1/show"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/show"))
                .andExpect(model().attributeExists("recipe"));
    }

    // test a Not Found exception
    @Test
    public void testGetRecipeNotFound() throws Exception{

        when(recipeService.findById(anyLong())).thenThrow(NotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/recipe/1/show"))
                .andExpect(status().isNotFound());

    }

    @Test
    public void testGetRecipeNumberFormatException() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders.get("/recipe/aaa/show"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("400error"));

    }

    @Test
    public void testGetNewRecipeForm() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/recipe/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("/recipe/recipeform"))
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    public void testPostNewRecipeForm() throws Exception {

        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(2L);

        when(recipeService.saveRecipeCommand(any())).thenReturn(recipeCommand);

        mockMvc.perform(MockMvcRequestBuilders.post("/recipe")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)    // mimic a form post
                .param("id", "")                        // mimic an empty string
                .param("description", "some string")
                .param("directions", "some string")
//                .param("url", "lior@leaplines.com")
        )   // some description
                .andExpect(status().is3xxRedirection()) // expect 3-2 status of redirection
                .andExpect(view().name("redirect:/recipe/2/show")); // redirection string
    }

    // validation test
    @Test
    public void testPostNewRecipeFormValidationFail() throws Exception {

        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(2L);

        when(recipeService.saveRecipeCommand(any())).thenReturn(recipeCommand);

        mockMvc.perform(MockMvcRequestBuilders.post("/recipe")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)    // mimic a form post
                .param("id", "")                        // mimic an empty string
                .param("description", "some string")
                .param("directions", "some string")
                .param("cookTime", "3000")
        )   // some description
                .andExpect(status().isOk()) // expect 3-2 status of redirection
                .andExpect(view().name("/recipe/recipeform")); // redirection string

    }

    @Test
    public void testGetUpdateView() throws Exception {

        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(1L);

        when(recipeService.findCommandById(anyLong())).thenReturn(recipeCommand);

        mockMvc.perform(MockMvcRequestBuilders.get("/recipe/1/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("/recipe/recipeform"))
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    public void testDeleteAction() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/recipe/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/")); // redirection string
    }


}