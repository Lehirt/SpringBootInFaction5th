package com.spring.tacocloud.web;

import com.spring.tacocloud.Ingredient;
import com.spring.tacocloud.Order;
import com.spring.tacocloud.Taco;
import com.spring.tacocloud.data.IngredientRepository;
import com.spring.tacocloud.data.TacoRepositroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.spring.tacocloud.Ingredient.Type;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("order")
public class DesignTacoController {

    private final IngredientRepository ingredientRepo;
    private TacoRepositroy designRepo;

    @Autowired
    public DesignTacoController(IngredientRepository ingredientRepo,
                                TacoRepositroy tacoRepo ){
        this.ingredientRepo = ingredientRepo;
        this.designRepo = tacoRepo;
    }

    @GetMapping
    public String showDesignForm(Model model){
        List<Ingredient> ingredients = new ArrayList<>();
        ingredientRepo.findAll().forEach(ingredient -> ingredients.add(ingredient));

        Type[] types = Ingredient.Type.values();
        for (Type type : types) {
            model.addAttribute(type.toString().toLowerCase(),
                    filterByType(ingredients,type));
        }

    //    model.addAttribute("design",new Taco());

        return "design";
    }
    private List<Ingredient> filterByType(List<Ingredient> ingredients, Type type){
       return  ingredients
               .stream()
               .filter(x->x.getType().equals(type))
               .collect(Collectors.toList());
    }

    @ModelAttribute(name = "order")
    public Order order(){
        return new Order();
    }

    @ModelAttribute(name = "taco")
    public Taco taco(){
        return new Taco();
    }

    @PostMapping
    public String processDesign(@Valid Taco design, Errors errors,
                                @ModelAttribute Order order ){

        log.info("   --- Saving taco");

        if (errors.hasErrors()){
            return "design";
        }

        Taco saved = designRepo.save(design);
        order.addDesign(saved);

        return "redirect:/orders/current";
    }


}
