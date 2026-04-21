package TheMealDbAPI;
import DTO.TheMealDbDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

//denna klass speglar strukturen i json texten (man måste ha en sådan klass när man används json) och håller lista med recepten
@JsonIgnoreProperties (ignoreUnknown = true)
public record TheMealDbResponse (List<TheMealDbDTO> meals){}

//denna klass tar bara emot meals
