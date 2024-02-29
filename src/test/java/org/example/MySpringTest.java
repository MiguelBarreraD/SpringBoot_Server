package org.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;

import org.example.Controller.MovieController;
import org.example.Spring.MySpring;
import org.junit.Before;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;

import java.io.*;

public class MySpringTest {

    @Test
    public void testLoadComponents() throws ClassNotFoundException {
        MySpring.loadComponents();
        assertFalse(MySpring.GetMethod.isEmpty());
        assertFalse(MySpring.PostMethod.isEmpty());

        Method getMethod = MySpring.GetMethod.get("/movie");

        assertNotNull(getMethod);
        assertEquals("getMovie", getMethod.getName());

        Method postMethod = MySpring.PostMethod.get("/Addmovie");
        assertNotNull(postMethod);
        assertEquals("postMovie", postMethod.getName());
    }

    @Test
    public void testCheckAndSave() throws Exception {

        Method getMethod = MovieController.class.getMethod("getMovie", String.class);
        MySpring.CheckAndSave(getMethod);

        Method postMethod = MovieController.class.getMethod("postMovie");
        MySpring.CheckAndSave(postMethod);

        assertFalse(MySpring.GetMethod.isEmpty());
        assertFalse(MySpring.PostMethod.isEmpty());

        Method savedGet = MySpring.GetMethod.get("/movie");
        assertNotNull(savedGet);
        assertEquals(getMethod, savedGet);

        Method savedPost = MySpring.PostMethod.get("/Addmovie");
        assertNotNull(savedPost);
        assertEquals(postMethod, savedPost);
    }
}
