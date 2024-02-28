package org.example.controller;

import java.io.IOException;

import org.example.runtime.GetMaping;
import org.example.runtime.PostMaping;
import org.example.runtime.RequestMapping;
import org.example.runtime.RestController;
import org.example.services.MovieAPI;
import com.google.gson.JsonObject;

@RestController
public class MovieController {

    @GetMaping(value = "/movie")
    public byte[] getMovie(String nameMovie) {
        JsonObject response = null;
        try {
            response = MovieAPI.getMovie(nameMovie);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.toString().getBytes();
    }

    @PostMaping(value = "/movie")
    public byte[] postMovie() {
        return "{\"mensaje\": \"Se añadió el nombre\"}".getBytes();
    }

}
