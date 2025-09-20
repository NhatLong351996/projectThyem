package com.example.handlingformsubmission;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class GreetingController {

	@GetMapping("/greeting")
	public String greetingForm(Model model) {
		model.addAttribute("greeting", new Greeting());
		model.addAttribute("errors", java.util.Collections.emptyList()); // luôn truyền errors rỗng để test
		return "greeting";
	}

	@GetMapping("/greetingEror")
	public String greetingFormErr(Model model) {
		model.addAttribute("greeting", new Greeting());
		java.util.List<String> errors = new java.util.ArrayList<>();
		errors.add("Lỗi mẫu: Trường dữ liệu không hợp lệ!");
		model.addAttribute("errors", errors); // truyền errors có phần tử để test
		return "greeting";
	}

	@PostMapping("/greeting")
	public String greetingSubmit(@ModelAttribute Greeting greeting, Model model) {
		model.addAttribute("greeting", greeting);
		return "result";
	}

}
