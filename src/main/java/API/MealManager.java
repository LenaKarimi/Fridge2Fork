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

    public List<MealApiModel> searchForRecepie (String mainIngredient, String category, ArrayList<String> userIngredient){
        mealsMatching = new ArrayList<>(); // alla matchade recept
        try{
            String jsonResponse = client.filterByIngredient(mainIngredient); // anropar API för att får json sträng med alla recept som innehåller ingrediensen

            ObjectMapper mapper = new ObjectMapper(); // skapar mapper objektet
            MealResponse responseIngredient = mapper.readValue(jsonResponse, MealResponse.class); //gör om jsonresponse från sträng till objekt (mealresponse) som har eb lista med alla mealApiModel
            List<MealApiModel> allMeals = responseIngredient.getMeals(); //får tillgång till listan med alla meals-som objekt då

            //går igenom alla recept
            for (MealApiModel meal : allMeals){
                //hämtar specifikt id för recpektive recept (för att kunna hämta hela receptet)
                String idMeal = meal.getIdMeal();

                String jsonResponseIDMeal = client.filterByLookUpId(idMeal); //anropar API med specifikt id, för att då json stränf för specifikt recept med alla ingredienser, lookup retunerar en lista med alla objekt drf 0
                MealResponse responseIDMeal = mapper.readValue(jsonResponseIDMeal,MealResponse.class); //får hela receptet i form av json sträng, gör om till objekt
                MealApiModel specificMeal = responseIDMeal.getMeals().get(0); // då vi får en "hel" lista med objet vill vi ha det första objektet, även om det bara är ett objekt - för att säkerställa 

                if (specificMeal.getStrArea().equalsIgnoreCase(category)){ // om område motsvarar kategori som valts

                    specificMeal.buildIngredientLists();// bygger listorna (ingredienser och mått) för det specifika objektet
                    List<String> ingredientList = specificMeal.getIngredientList(); //hämtar lista för ingredienser för specifikt recept

                    if (isMealMatching70Percent(userIngredient,ingredientList)){ // anropar metod som kontrollerar 70% gräns,
                        mealsMatching.add(specificMeal); //om ok lägg in i meals matching list
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
        //tar emot listorna
        int counter = 0;
        for (String i : ingredientList){
            for (String userIngredient : userList){ // loopar igenom listerna, om ng t i listan finns i den andra listan så lägger till i räknare
                if (i.equalsIgnoreCase(userIngredient)){
                    counter++;
                }
            }
        }
        Double percent = (double) counter/ingredientList.size(); // räknar ut delen (antalet matchningar) med det totala och får andelen så
        return percent>= 0.7; // retunerar Om 70% - true  eller ej - false
    }

}
