package com.briceducardonnoy.client;

import com.google.gwt.junit.client.GWTTestCase;

public class SandboxGwtTest extends GWTTestCase {
    @Override
    public String getModuleName() {
        return "com.briceducardonnoy.ArtistShowcase";
    }

    public void testSandbox() {
        assertTrue(true);
    }
}
