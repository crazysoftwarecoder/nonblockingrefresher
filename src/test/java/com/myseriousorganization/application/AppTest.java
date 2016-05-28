package com.myseriousorganization.application;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by asfernando on 7/30/15.
 */
public class AppTest {

    private App application = null;

    @Before
    public void init() {
        application = new App();
    }

    @Test
    public void test() {
        Assert.assertEquals(application.getBasicString(), "Sample");
    }
}
