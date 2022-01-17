package person.web.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonGetResponsePayload {

	private Integer id;
	private String firstName;
	private String lastName;

}
