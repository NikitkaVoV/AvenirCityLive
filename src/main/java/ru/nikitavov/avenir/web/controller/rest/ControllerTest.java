package ru.nikitavov.avenir.web.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("api/test")
@RequiredArgsConstructor
public class ControllerTest {

    @GetMapping("text")
    public String get() {
        return "text";
    }

    @GetMapping("aud")
    public ResponseEntity<List<String>> getAud(LinkedHashMap<String, String> map) {
        ArrayList<String> strings = new ArrayList<>();
        strings.add("1");
        strings.add("2");
        strings.add("3");
        return ResponseEntity.ok(strings);
    }

    public static class Request<ID> {
        private ID id;

        public ID getId() {
            return id;
        }

        public void setId(ID id) {
            this.id = id;
        }
    }
}
