package gs.controller;

import gs.controller.resource.RootResource;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.web.bind.annotation.*;

/**
 * <p>Rest controller to discover the asylum api.</p>
 */
@RestController
@RequestMapping("/")
public class RootRestController {

    @GetMapping
    public ResourceSupport start() {
        return new RootResource();
    }

}
