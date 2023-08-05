package com.icsd.annotation;

import com.icsd.apiresponce.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class FrequencyController {


    @PostMapping("/checkFrequency")
    public ResponseEntity<ApiResponse> checkFrequency(@RequestBody @Valid FrequencyDTO validDto) {
        return  new ResponseEntity<>(new ApiResponse(1,"",validDto),HttpStatus.OK);
    }

}
