package com.forkexec.hub.cc.it;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

/**
 * Class that tests ValidateIT operation
 */

public class ValidateIT extends BaseIT {

    @Test
    public void ValidateEmptyNumberTest() {
        assertFalse(client.validateNumber(""));
    }

    @Test
    public void ValidateNullNumberTest() {
        assertFalse(client.validateNumber(null));
    }

    @Test
    public void ValidateShorterNumberTest() {
        assertFalse(client.validateNumber("453293546223668"));
    }

    @Test
    public void ValidateBiggerNumberTest() {
        assertFalse(client.validateNumber("45329653546223668"));
    }

    @Test
    public void ValidateValidNumberTest() {
        assertTrue(client.validateNumber("4532935462236684"));
    }
}