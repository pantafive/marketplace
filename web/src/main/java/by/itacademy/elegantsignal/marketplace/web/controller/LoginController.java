package by.itacademy.elegantsignal.marketplace.web.controller;

import by.itacademy.elegantsignal.marketplace.service.IUserService;
import by.itacademy.elegantsignal.marketplace.web.converter.UserFromDTOConverter;
import by.itacademy.elegantsignal.marketplace.web.converter.UserToDTOConverter;
import by.itacademy.elegantsignal.marketplace.web.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Locale;


@Controller
public class LoginController {

	@Autowired private MessageSource messageSource;

	@Autowired private IUserService userService;
	@Autowired private UserToDTOConverter userToDTOConverter;
	@Autowired private UserFromDTOConverter userFromDTOConverter;

	@GetMapping("/login") public ModelAndView login(
		final Locale locale,
		@RequestParam(value = "error", required = false) final String error,
		@RequestParam(value = "logout", required = false) final String logout) {

		final ModelAndView model = new ModelAndView();
		model.setViewName("login");

		if (error != null) {
			model.addObject("error", messageSource.getMessage("login.error.loginFailed", null, locale));
		}

		if (logout != null) {
			model.addObject("msg", messageSource.getMessage("login.msg.loggedOutSuccessfully", null, locale));
		}

		model.addObject("formModel", userToDTOConverter.apply(userService.createEntity()));

		return model;
	}

	@PostMapping("/singup") public ModelAndView save(
		final Locale locale,
		@Valid @ModelAttribute("formModel") final UserDTO formModel,
		final BindingResult result) {

		final ModelAndView model = new ModelAndView();
		model.setViewName("login");

		if (result.hasErrors()) {
			model.addObject("error", messageSource.getMessage("login.error.registrationFailed", null, locale));
			return model;
		}

		try {
			userService.addNewUser(userFromDTOConverter.apply(formModel));
		} catch (final IllegalArgumentException e) {
			model.addObject("error", e.getMessage());
			return model;
		}

		model.addObject("msg", formModel.getEmail() + " " + messageSource.getMessage("login.msg.userCreated", null, locale));
		return model;
	}

}