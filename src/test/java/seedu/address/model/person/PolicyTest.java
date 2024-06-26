package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class PolicyTest {

    @Test
    public void isValidExpiryDate() {
        // null expiry date
        assertThrows(NullPointerException.class, () -> Policy.isValidExpiryDate(null));

        // invalid expiry date
        assertFalse(Policy.isValidExpiryDate("")); // empty string
        assertFalse(Policy.isValidExpiryDate(" ")); // spaces only
        assertFalse(Policy.isValidExpiryDate("2020-10-10"));
        assertFalse(Policy.isValidExpiryDate("10/10/2020"));
        assertFalse(Policy.isValidExpiryDate("01-01-2020")); // past date

        // valid expiry date
        assertTrue(Policy.isValidExpiryDate("01-01-2040"));
        assertTrue(Policy.isValidExpiryDate("31-12-2028"));
    }
    @Test
    public void isValidPremium() {
        // null premium
        assertThrows(NullPointerException.class, () -> Policy.isValidPremium(null));

        // invalid premium
        assertFalse(Policy.isValidPremium("")); // empty string
        assertFalse(Policy.isValidPremium(" ")); // spaces only
        assertFalse(Policy.isValidPremium("-1"));
        assertFalse(Policy.isValidPremium("invalid"));

        // valid premium
        assertTrue(Policy.isValidPremium("1.0"));
        assertTrue(Policy.isValidPremium("1"));
        assertTrue(Policy.isValidPremium("1.1")); // uppercase
    }

    @Test
    public void equals() {
        Policy policy = new Policy("Hello", LocalDate.of(2023, 01, 01), 100.0);

        // same object -> returns true
        assertTrue(policy.equals(policy));

        // same values -> returns true
        Policy policyCopy = new Policy(policy.value, policy.expiryDate, policy.premium);
        assertTrue(policy.equals(policyCopy));

        // different types -> returns false
        assertFalse(policy.equals(1));

        // null -> returns false
        assertFalse(policy.equals(null));

        // different policy -> returns false
        Policy differentPolicy = new Policy("Bye");
        assertFalse(policy.equals(differentPolicy));
    }

    @Test
    public void equals_additionalScenarios() {
        Policy policy = new Policy("Hello", LocalDate.of(2023, 01, 01), 100.0);

        // Additional scenarios for equals method
        assertTrue(policy.equals(policy)); // Policy compared to itself
        assertFalse(policy.equals(new Policy("Hello", LocalDate.of(2023, 01, 02),
                100.0))); // Different expiry date
        assertFalse(policy.equals(new Policy("Hello", LocalDate.of(2023, 01, 01),
                200.0))); // Different premium
        assertFalse(policy.equals(new Policy("Hello", null, 100.0))); // Null expiry date
        assertFalse(policy.equals(new Policy("Hello", LocalDate.of(2023, 01, 01),
                0.0))); // Null premium
    }

    @Test
    public void hashCode_equalPolicy_sameHashCode() {
        Policy policy1 = new Policy("Hello", LocalDate.of(2023, 01, 01), 100.0);

        Policy policy2 = new Policy("Hello", LocalDate.of(2023, 01, 01), 100.0);

        assertEquals(policy1.hashCode(), policy2.hashCode());
    }
}
