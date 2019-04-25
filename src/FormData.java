import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to represent the form data filled in by a user. Data is may be written out to a file and may
 * be loaded back in from a file. However, the social security number of the user should not be recorded.
 *
 * @author Stephen
 * @version 2019-04-24
 */
public class FormData implements Serializable
{
	/**
	 * Serialization ID - indicates which version of the code was used to write out the data to file.
	 */
	private static final long serialVersionUID = 5L;

	/**
	 * Identifying Information:
	 */
	private String firstName;
	private char middleInitial;
	private String lastName;
	private String displayName;
	private String SSN;  // TODO: ensure that SSN does not serialize!

	/**
	 * Contact information:
	 */
	private String phone;
	private String email;
	private String address;

	/**
	 * User verification:
	 */
	private List<Point> signature;

	/**
	 * Set the fields in the formdata object. Emails must end with "@ou.edu". SSNs must be 9 digits long, and phone
	 * numbers must be 10 digits long. Returns true if the values were verified and set. False otherwise. Values
	 * are not set if they are not valid (as determined by the verify function).
	 */
	public boolean setValues(String firstName, char middleInitial, String lastName, String displayName,
			String SSN, String phone, String email, String address, List<Point> signature)
	{
		if (!this.verify(SSN, phone, email))
		{
			return false;
		}

		this.firstName = new String(firstName);
		this.middleInitial = middleInitial;
		this.lastName = new String(lastName);
		this.displayName = new String(displayName);
		this.SSN = new String(SSN);

		this.phone = new String(phone);
		this.email = new String(email);
		this.address = new String(address);

		this.signature = new ArrayList<Point>(signature);

		return true;
	}

	/**
	 * Verifies that the information is correct (relatively).
	 * Real verification would take more effort, but for the purposes of this lab this will
	 * be sufficient.
	 */
	private boolean verify(String SSN, String phone, String email)
	{
		if (SSN.length() != 9)
		{
			return false;
		}
		if (phone.length() != 10)
		{
			return false;
		}
		if (!email.endsWith("@ou.edu"))
		{
			return false;
		}

		return true;
	}

	/**
	 * Reset all fields in the formdata object.
	 */
	public void reset()
	{
		this.firstName = "";
		this.middleInitial = '\0';
		this.lastName = "";
		this.displayName = "";
		this.SSN = "";

		this.phone = "";
		this.email = "";
		this.address = "";

		this.signature.clear();
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getFirstName() {
		return firstName;
	}

	public char getMiddleInitial() {
		return middleInitial;
	}

	public String getLastName() {
		return lastName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getSSN() {
		return SSN;
	}

	public String getPhone() {
		return phone;
	}

	public String getEmail() {
		return email;
	}

	public String getAddress() {
		return address;
	}

	public List<Point> getSignature() {
		return signature;
	}
}