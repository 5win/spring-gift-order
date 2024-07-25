package gift.controller;

import gift.dto.OrderRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @PostMapping
    public ResponseEntity<String> order(@RequestBody OrderRequest orderRequest,
        HttpServletRequest httpServletRequest) {

        String email = (String) httpServletRequest.getAttribute("email");


    }
}
