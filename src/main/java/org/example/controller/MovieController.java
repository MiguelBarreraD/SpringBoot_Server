package org.example.Controller;

import java.io.IOException;

import org.example.runtime.GetMapping;
import org.example.runtime.PostMapping;
import org.example.runtime.RestController;
import org.example.services.MovieAPI;
import com.google.gson.JsonObject;

@RestController
public class MovieController {

    @GetMapping(value = "/movie")
    public static byte[] getMovie(String nameMovie) {
        JsonObject response = null;
        try {
            response = MovieAPI.getMovie(nameMovie);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.toString().getBytes();
    }

    @PostMapping(value = "/Addmovie")
    public static byte[] postMovie() {
        return "{\"mensaje\": \"Se añadió el nombre\"}".getBytes();
    }

}
