package io.esastack.test.pref;

import esa.restlight.spring.shaded.org.springframework.web.bind.annotation.PostMapping;
import esa.restlight.spring.shaded.org.springframework.web.bind.annotation.RequestBody;
import esa.restlight.spring.shaded.org.springframework.web.bind.annotation.RequestMapping;
import esa.restlight.spring.shaded.org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping("/http")
public class EchoController {

    @PostMapping("/echo")
    @ResponseBody
    public String responseWith1MBBody(@RequestBody String bodyString) {
        return bodyString;
    }
}
