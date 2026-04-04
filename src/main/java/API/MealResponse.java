package API;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties (ignoreUnknown = true)
public class MealResponse {
    private List<MealApiModel> meals;

    public MealResponse(){
    }

    public List<MealApiModel> getMeals() {
        return meals;
    }
}
