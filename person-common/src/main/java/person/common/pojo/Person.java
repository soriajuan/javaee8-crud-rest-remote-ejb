package person.common.pojo;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Person implements Serializable {

	private static final long serialVersionUID = -2739400189823455495L;

	private Integer id;
	private String firstName;
	private String lastName;

}
