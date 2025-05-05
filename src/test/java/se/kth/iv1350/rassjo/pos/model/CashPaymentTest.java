package se.kth.iv1350.rassjo.pos.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CashPaymentTest {

    private CashPayment cpCostFivePayTen;
    private CashPayment cpCostTenPayFive;
    private CashPayment cpCostFivePayFive;

    @BeforeEach
    void setUp() {
        cpCostFivePayTen = new CashPayment(new Amount(5), new Amount(10));
        cpCostTenPayFive = new CashPayment(new Amount(10), new Amount(5));
        cpCostFivePayFive = new CashPayment(new Amount(5), new Amount(5));
    }

    @AfterEach
    void tearDown() {
        cpCostTenPayFive = null;
        cpCostFivePayFive = null;
    }

    @Test
    void testCalculatesCorrectChange () {
        Amount expectedChange = new Amount(5);
        Amount actualChange = cpCostFivePayTen.getChange();
        assertEquals(expectedChange, actualChange, "Change should be calculated correctly.");
    }

    @Test
    void testZeroChange () {
        Amount expectedChange = new Amount(0);
        Amount actualChange = cpCostFivePayFive.getChange();
        assertEquals(expectedChange, actualChange, "Change should be 0 when paid amount equals the total cost.");
    }

    @Test
    void testNegativeChange () {
        Amount expectedChange = new Amount(-5);
        Amount actualChange = cpCostTenPayFive.getChange();
        assertEquals(expectedChange, actualChange, "Change should be negative when paid amount is less than the total cost.");
    }

    @Test
    void testNotEqualsNull() {
        boolean result = cpCostTenPayFive.equals(null);
        assertFalse(result, "Equals should return false when compared with null.");
    }

    @Test
    void testNotEqualsDifferentClass() {
        String differentClassObject = "This is not a CashPayment";
        boolean result = cpCostTenPayFive.equals(differentClassObject);
        assertFalse(result, "Equals should return false when compared to an object of a different class.");
    }

    @Test
    void testNotEqualsPaymentDifferentValues() {
        boolean result = cpCostTenPayFive.equals(cpCostFivePayFive);
        assertFalse(result, "Equals should return false when compared with null.");
    }

    @Test
    void testEqualsSameObject() {
        boolean result = cpCostTenPayFive.equals(cpCostTenPayFive);
        assertTrue(result, "Equals should return true when comparing the object to itself.");
    }

    @Test
    void testEqualsEquivalentObject() {
        CashPayment other = new CashPayment(new Amount(10), new Amount(5));
        boolean result = cpCostTenPayFive.equals(other);
        assertTrue(result, "Equals should return true for different objects with equivalent values.");
    }

    @Test
    void testHashCodeSameForSameObjects() {
        int hashCode1 = cpCostTenPayFive.hashCode();
        int hashCode2 = cpCostTenPayFive.hashCode();
        assertEquals(hashCode1, hashCode2, "Hash codes should match for same objects.");
    }

    @Test
    void testHashCodeSameForEquivalentObjects() {
        CashPayment other = new CashPayment(new Amount(10), new Amount(5));
        int hashCode1 = cpCostTenPayFive.hashCode();
        int hashCode2 = other.hashCode();
        assertEquals(hashCode1, hashCode2, "Hash codes should match for equivalent objects.");
    }

    @Test
    void testHashCodeDifferentForNonEquivalentObjects() {
        CashPayment different = new CashPayment(new Amount(50), new Amount(100));
        int hashCode1 = cpCostTenPayFive.hashCode();
        int hashCode2 = different.hashCode();
        assertNotEquals(hashCode1, hashCode2, "Hash codes should not match for non-equivalent objects.");
    }
}