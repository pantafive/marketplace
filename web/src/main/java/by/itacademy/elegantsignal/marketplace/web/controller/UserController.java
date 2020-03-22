package by.itacademy.elegantsignal.marketplace.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import by.itacademy.elegantsignal.marketplace.daoapi.entity.table.IUser;
import by.itacademy.elegantsignal.marketplace.daoapi.filter.UserFilter;
import by.itacademy.elegantsignal.marketplace.service.IUserService;
import by.itacademy.elegantsignal.marketplace.web.converter.UserFromDTOConverter;
import by.itacademy.elegantsignal.marketplace.web.converter.UserToDTOConverter;
import by.itacademy.elegantsignal.marketplace.web.dto.GridStateDTO;
import by.itacademy.elegantsignal.marketplace.web.dto.UserDTO;


@Controller
@RequestMapping(value = "/users")
public class UserController extends AbstractController {

	@Autowired
	private IUserService userService;
	@Autowired
	private UserToDTOConverter toDtoConverter;
	@Autowired
	private UserFromDTOConverter fromDtoConverter;

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView index(final HttpServletRequest req,
			@RequestParam(name = "page", required = false) final Integer pageNumber,
			@RequestParam(name = "sort", required = false) final String sortColumn) {

		final GridStateDTO gridState = getListDTO(req);
		gridState.setPage(pageNumber);
		gridState.setSort(sortColumn, "id");

		final UserFilter filter = new UserFilter();

		final List<IUser> entities = userService.find(filter);
		final List<UserDTO> dtos = entities.stream().map(toDtoConverter).collect(Collectors.toList());
		gridState.setTotalCount(userService.getCount(filter));

		final Map<String, Object> models = new HashMap<>();
		models.put("gridItems", dtos);
		System.out.println(models);
		return new ModelAndView("users.list", models);
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public ModelAndView showForm() {
		final Map<String, Object> hashMap = new HashMap<>();
		final IUser newEntity = userService.createEntity();
		hashMap.put("formModel", toDtoConverter.apply(newEntity));

		return new ModelAndView("users.edit", hashMap);
	}

	@RequestMapping(method = RequestMethod.POST)
	public String save(@Valid @ModelAttribute("formModel") final UserDTO formModel, final BindingResult result) {
		if (result.hasErrors()) {
			return "users.edit";
		} else {
			final IUser entity = fromDtoConverter.apply(formModel);
			userService.save(entity);
			return "redirect:/users";
		}
	}

	@RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
	public String delete(@PathVariable(name = "id", required = true) final Integer id) {
		userService.delete(id);
		return "redirect:/users";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ModelAndView viewDetails(@PathVariable(name = "id", required = true) final Integer id) {
		final IUser dbModel = userService.get(id);
		final UserDTO dto = toDtoConverter.apply(dbModel);
		final Map<String, Object> hashMap = new HashMap<>();
		hashMap.put("formModel", dto);
		hashMap.put("readonly", true);

		return new ModelAndView("users.edit", hashMap);
	}

	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public ModelAndView edit(@PathVariable(name = "id", required = true) final Integer id) {
		final UserDTO dto = toDtoConverter.apply(userService.get(id));

		final Map<String, Object> hashMap = new HashMap<>();
		hashMap.put("formModel", dto);

		return new ModelAndView("users.edit", hashMap);
	}

}