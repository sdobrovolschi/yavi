package am.ik.yavi.core;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

import am.ik.yavi.Address;
import am.ik.yavi.Country;
import am.ik.yavi.PhoneNumber;

public class NestedValidatorTest {

	Validator<Address> validator() {
		return Validator.<Address> builder()
				.constraint(Address::street, "street", c -> c.notBlank().lessThan(32))
				.constraint(Address::country, "country", Country.validator())
				.constraintIfNotNull(Address::phoneNumber, "phoneNumber",
						PhoneNumber.validator())
				.build();
	}

	@Test
	public void valid() {
		Validator<Address> addressValidator = validator();
		Address address = new Address(new Country("JP"), "tokyo",
				new PhoneNumber("0123456789"));
		ConstraintViolations violations = addressValidator.validate(address);
		assertThat(violations.isValid()).isTrue();
	}

	@Test
	public void invalid() {
		Validator<Address> addressValidator = validator();
		Address address = new Address(new Country(null), null, new PhoneNumber(""));
		ConstraintViolations violations = addressValidator.validate(address);
		assertThat(violations.isValid()).isFalse();
		assertThat(violations.size()).isEqualTo(4);
		assertThat(violations.get(0).message()).isEqualTo("\"street\" must not be blank");
		assertThat(violations.get(1).message())
				.isEqualTo("\"country.name\" must not be blank");
		assertThat(violations.get(2).message())
				.isEqualTo("\"phoneNumber.value\" must not be blank");
		assertThat(violations.get(3).message())
				.isEqualTo("The size of \"phoneNumber.value\" must be greater than 8");
	}

	@Test
	public void nestedValueIsNull() {
		Validator<Address> addressValidator = validator();
		Address address = new Address(null, null, null /* nullValidity */);
		ConstraintViolations violations = addressValidator.validate(address);
		assertThat(violations.isValid()).isFalse();
		assertThat(violations.size()).isEqualTo(2);
		assertThat(violations.get(0).message()).isEqualTo("\"street\" must not be blank");
		assertThat(violations.get(1).message()).isEqualTo("\"country\" must not be null");
	}
}