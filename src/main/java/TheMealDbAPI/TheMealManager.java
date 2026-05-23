package TheMealDbAPI;

import Model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class TheMealManager {
        private MealRepository repository;
        private MealMapper mapper = new MealMapper();

        public TheMealManager(MealRepository repository){
            this.repository = repository;
        }

        public List<Recipe> searchForRecepie (String mainIngredient, String cuisine, String diet){
            //List<TheMealDbDTO> mealsMatching = new ArrayList<>();
            List<Recipe> mealsMatching = new ArrayList<>(); // skapar lista med alla matchade recept

            try{
                List<TheMealDbDTO> allMeals = repository.getMealsByIngredient(mainIngredient);
                System.out.println("Antal måltider hittade: " + allMeals.size());

                for (TheMealDbDTO meal : allMeals){

                    TheMealDbDTO specificMeal = repository.getMealById(meal.idMeal);
                    System.out.println("Område: " + specificMeal.strArea);

                    Recipe recipe = mapper.toDomain(specificMeal);

                    boolean hasCuisine = specificMeal.strArea.equalsIgnoreCase(cuisine);
                    boolean hasDiet = specificMeal.strCategory.equalsIgnoreCase(diet);

                    if (hasCuisine && hasDiet){
                        mealsMatching.add(recipe);
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
