package API;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties (ignoreUnknown = true)
public record TheMealDbResponse (List<TheMealDbDTO> meals){}

//denna klass tar bara emot meals
