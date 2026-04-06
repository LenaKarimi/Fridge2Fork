package API;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MealManager {
    private ApiClient client;
    private List<MealApiModel> mealsMatching;

    public MealManager(ApiClient client){
        this.client = client;
    }

    public List<MealApiModel>searchForRecepie(String ingredient, String category, ArrayList<String> userIngredient){
        mealsMatching = new ArrayList<>();
        try{
            String jsonResponse = client.filterByIngredient(ingredient); //skickar förfrågan med ingrediens som hämtar Json strängen, får lista med alla recept som innehåller ingredient

            ObjectMapper mapper = new ObjectMapper(); // skapar mapper objektet
            MealResponse responseIngredient = mapper.readValue(jsonResponse, MealResponse.class); //gör om denna klass från sträng till objekt
            List<MealApiModel> allMeals = responseIngredient.getMeals(); //får tillgång till listan med alla meals-som objekt då

            //går igenom alla recept
            for (MealApiModel meal : allMeals){
                //hämtar specifikt id för recpektive recept
                String idMeal = meal.getIdMeal();

                String jsonResponseIDMeal = client.filterByLookUpId(idMeal); //skickar ny förfrågan med id, för specifikt recept med alla ingredienser, lookup retunerar en lista med alla objekt drf 0
                MealResponse responseIDMeal = mapper.readValue(jsonResponseIDMeal,MealResponse.class);
                MealApiModel specificMeal = responseIDMeal.getMeals().get(0);

                if (specificMeal.getStrArea().equalsIgnoreCase(category)){

                    specificMeal.buildIngredientLists();// bygger listorna för det specifika objektet
                    List<String> ingredientList = specificMeal.getIngredientList(); //hämtar lista för ingredienser för specifikt recept
                    List<String> measureList = specificMeal.getMeasureList(); //hämtar lista för mått för specifikt recep

                    if (isMealMatching70Percent(userIngredient,ingredientList)){
                        mealsMatching.add(specificMeal);
                    }
                }
            }
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return mealsMatching;
    }

    public boolean isMealMatching70Percent(List<String> userList, List<String> ingredientList){
        int counter = 0;
        for (String i : ingredientList){
            for (String userIngredient : userList){
                if (i.equalsIgnoreCase(userIngredient)){
                    counter++;
                }
            }
        }
        Double percent = (double) counter/ingredientList.size();
        return percent>= 0.7;
    }

}
