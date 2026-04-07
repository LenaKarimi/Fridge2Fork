package API;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties (ignoreUnknown = true)
public class MealResponse {
    private List<MealApiModel> meals; //matchar med jsonfältet "meals", så jacksson kan  skapa mealApiManager objektem

    public MealResponse(){
    }

    public List<MealApiModel> getMeals() {
        return meals;
    }
}
