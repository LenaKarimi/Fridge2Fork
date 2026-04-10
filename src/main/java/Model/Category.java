package Model;

import TheMealDbAPI.MealRepository;
import TheMealDbAPI.TheMealDbDTO;

import java.util.ArrayList;
import java.util.List;

public enum Category {

    KOLHYDRATER,
    PROTEIN,
    GRÖNSAKER,
    MEJERI,
    FRUKT,
    SKAFFERI,
    OTHER;

    //denna sköter logik, frågar mealrepo om alla recetp som matchar en ingrediens, går igenom alla recept
    //filtrerar bort de som inte matchar med valda området utifrån kök
    public static class MealManager {
        private MealRepository repository;

        public MealManager(MealRepository repository){
            this.repository = repository;
        }

        public List<TheMealDbDTO> searchForRecepie (String mainIngredient, String category){
            List<TheMealDbDTO> mealsMatching = new ArrayList<>(); // skapar lista med alla matchade recept

            try{
                List<TheMealDbDTO> allMeals = repository.getMealsByIngredient(mainIngredient);

                //går igenom alla recept
                for (TheMealDbDTO meal : allMeals){

                    TheMealDbDTO specificMeal = repository.getMealById(meal.idMeal);

                    if (specificMeal.strArea.equalsIgnoreCase(category)){ // om område motsvarar kategori som valts
                        mealsMatching.add(specificMeal);
                    }
                }
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            return mealsMatching;
        }

    }
}
