package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient;

import user.User;


@Controller
public class FooClientController<FooModel> {
	/*
    @Value("${resourceserver.api.url}")
    private String fooApiUrl;

    @Autowired
    private WebClient webClient;

    @GetMapping("/foos")
    public String getFoos(Model model) {
        List<FooModel> foos = this.webClient.get()
            .uri(fooApiUrl)
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<List<FooModel>>() {
            })
            .block();
        System.out.println(foos);
        model.addAttribute("foos", foos);
        return "foos";
    }
    */
	@Value("${resourceserver.api.url}")
    private String resourceServerApiUrl;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @GetMapping("/foos")
    public String getFoos(Model model) {
        // Faceți o cerere GET către resource server pentru a obține datele utilizatorului
        User userData = webClientBuilder.build()
                .get()
                .uri(resourceServerApiUrl + "/user-data") // Specificați calea către resursa de date a utilizatorului
                .retrieve()
                .bodyToMono(User.class) // UserData ar trebui să fie o clasă care reprezintă datele utilizatorului
                .block();

        // Verificați dacă userData este null sau conține datele utilizatorului
        if (userData != null) {
            model.addAttribute("userData", userData);
        } else {
            // Tratați cazul în care nu s-au găsit date pentru utilizator
            model.addAttribute("userData", new User()); // Puteți crea o instanță goală a clasei UserData sau afișa un mesaj de eroare
        }
        return "foos";
    }
}