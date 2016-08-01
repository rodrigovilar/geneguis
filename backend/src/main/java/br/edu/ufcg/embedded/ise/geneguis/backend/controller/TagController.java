package br.edu.ufcg.embedded.ise.geneguis.backend.controller;

import static br.edu.ufcg.embedded.ise.geneguis.backend.EntryPoint.getContainer;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import br.edu.ufcg.embedded.ise.geneguis.TagRule;

@Controller
@RequestMapping(value = "/tags")
public class TagController {

	@RequestMapping(value = "rules", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<TagRuleRest> createTag(@RequestBody String input) {
		TagRuleRest rest = (TagRuleRest) new Gson().fromJson(input, TagRuleRest.class);
		
		TagRule domain = getContainer().saveTagRule(Converter.toDomain(rest));
		
        return new ResponseEntity<TagRuleRest>(Converter.toRest(domain), HttpStatus.CREATED);
	}

	
}
